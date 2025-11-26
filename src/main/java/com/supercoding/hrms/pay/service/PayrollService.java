package com.supercoding.hrms.pay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supercoding.hrms.attendance.dto.request.read.ReadWorkhoursRequestDto;
import com.supercoding.hrms.attendance.dto.response.WorkhourResponseDto;
import com.supercoding.hrms.attendance.service.WorkhourService;
import com.supercoding.hrms.emp.dto.response.EmployeeDetailResponseDto;
import com.supercoding.hrms.emp.entity.Employee;
import com.supercoding.hrms.emp.repository.EmployeeRepository;
import com.supercoding.hrms.emp.service.EmpService;
import com.supercoding.hrms.pay.domain.*;
import com.supercoding.hrms.pay.dto.PayrollType;
import com.supercoding.hrms.pay.repository.ItemNmRepository;
import com.supercoding.hrms.pay.repository.PayRepository;
import com.supercoding.hrms.pay.repository.PayrollDetailRepository;
import com.supercoding.hrms.pay.repository.PayrollRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final PayrollDetailRepository payrollDetailRepository;
    private final ItemNmRepository itemNmRepository;
    private final PayRepository payRepository;

    private final EmpService empService;

    // 급여 json 파일 가져와서 타입 매핑을 해주기 위함
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final WorkhourService workhourService;
    private final EmployeeRepository employeeRepository;

    /**
     * resources/payroll-data.json 파일을 읽어서 DB에 저장
     */
    public void loadPayrollFromJson() {
        try {
            // resources/payrolls.json 파일 읽기
            InputStream inputStream = new ClassPathResource("payrolls.json").getInputStream();

            // JSON → List<PayrollType> 변환
            List<PayrollType> payrollList = objectMapper.readValue(inputStream,
                    new TypeReference<List<PayrollType>>() {});

            // DB 저장
            for (PayrollType dto : payrollList) {
                Payroll payroll = Payroll.builder()
                        .empId(dto.getEmpId())
                        .payDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .status(dto.getStatus())
                        .build();

                payrollRepository.save(payroll);

                for (PayrollDetail detail : dto.getItems()) {
                    payrollDetailRepository.save(detail);
                }
            }

            log.info("JSON 급여 데이터 {}건 저장 완료", payrollList.size());

        } catch (Exception e) {
            log.error("급여 데이터 JSON 로드 실패: {}", e.getMessage(), e);
        }
    }

    //C, R, R(L), U, D, D(L) 규칙에 따라
    // 지금 R 다건으로 만들었음

    // C (Create), 급여 이력 생성
    public PayrollType createPayroll(PayrollType request) {
        // PayrollCreateRequest → Payroll 변환

        // 프론트에서 준 정보(request) 가지고 Payroll에 맵핑해줌
        Payroll payroll = Payroll.builder()
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();
        // 맵핑한 payroll을 DB(MySQL)에 저장
        Payroll savedPayroll = payrollRepository.save(payroll);

        // payrollDetail 저장
        syncPayrollDetails(request.getItems(), savedPayroll.getEmpId());

        // 3️⃣ 다시 조회해서 응답 반환
        return getPayroll(savedPayroll.getPayHistId());
    }

    // 한사람의 detail을 저장하거나 업데이트 함
    public void syncPayrollDetails(List<PayrollDetail> items, Long empId){
        // empId 기반으로 detail 불러옴
        List<PayrollDetail> details = payrollDetailRepository.findByEmpId(empId);
        List<String> itemCds = details.stream().map(PayrollDetail::getItemCd).toList();;

        List<PayrollDetail> newDetails = items.stream().map(item ->{
            if(itemCds.contains(item.getItemCd())){
                //detail id가 포함되면 업데이트
                return PayrollDetail.builder()
                        .payrollDetailId(details.get(itemCds.indexOf(item.getItemCd())).getPayrollDetailId())
                        .empId(empId)
                        .itemCd(item.getItemCd())
                        .amount(item.getAmount())
                        .remark(item.getRemark())
                        .build();
            } else{
                //포함안되면 저장
                return PayrollDetail.builder()
                        .empId(empId)
                        .itemCd(item.getItemCd())
                        .amount(item.getAmount())
                        .remark(item.getRemark())
                        .build();
            }
        }).toList();

        payrollDetailRepository.saveAll(newDetails);
    }

    //R (단건 조회)
    //급여 상세 조회 (직원/관리자 공통)
    //특정 payHistId 기준으로 급여명세서 세부 항목 조회
    public PayrollType getPayroll(Long histId) {
        // 조회하고자 하는 Payroll의 id(histId)를 통해 조회하고자 하는 payroll 불러옴
        Payroll payroll = payrollRepository.findById(histId)
                .orElseThrow(() -> new RuntimeException("해당 급여이력이 없습니다."));

        return setPayrollType(payroll);
    }

    //R(L) (다건 조회)
    //[관리자용] 전체 급여 목록 조회
    public List<PayrollType> getPayrolls(String payMonth, String deptId, String status, String gradeId, String empNm) {

        List<Employee> employeeList = employeeRepository.findAll();

        if(!deptId.isEmpty()){
            employeeList = filterByDept(deptId, employeeList);
        }

        if(!gradeId.isEmpty()){
            employeeList = filterByGrade(gradeId, employeeList);
        }

        if(!empNm.isEmpty()){
            employeeList = filterByName(empNm, employeeList);
        }

        // substring(0,6) = YYYYMM
        List<Payroll> payrolls = payrollRepository.findAll().stream().filter(item -> item.getPayDate().substring(0,6).equals(payMonth)).toList();

        if(!employeeList.isEmpty()){
            // 모든 필터링 거쳐서 검색된것
            List<Long> empIds = employeeList.stream().map(Employee::getEmpId).toList();
            payrolls = payrolls.stream().filter(item -> empIds.contains(item.getEmpId())).toList(); //payrolls에서 filter를 걸어서 empIds 안에서 payrolls의 empId와 같은(교집합) 애들을 필터링 함.
        }

        if(!status.isEmpty()){
            payrolls = payrolls.stream().filter(item -> item.getStatus().equals(status)).toList();
        }

        return payrolls.stream().map(item -> setPayrollType(item)).toList();
    }

    // 단건과 다건 조회에서 둘다 쓰이니까 중복으로 쓰여서 분리해서 따로 작성함
    public PayrollType setPayrollType(Payroll payroll){

        PayrollType payrollType = getEmpInfo(payroll.getEmpId());
        payrollType.setPayHistId(payroll.getPayHistId());
        payrollType.setEmpId(payroll.getEmpId());
        payrollType.setStatus(PayrollStatus.from(payroll.getStatus()).getDisplayName());
        payrollType.setPayDate(payroll.getPayDate());
        payrollType.setItems(getDetails(payroll.getEmpId()));

        return payrollType;
    }

    // 사원쪽에서 가져온 애라서 따로 빼줌.
    public PayrollType getEmpInfo(Long empId){
        Integer workPay = payRepository.findById(empId)
                .map(Pay::getWorkPay)
                .orElse(0);

        EmployeeDetailResponseDto empInfo = empService.getEmployeeByAdmin(empId);

        PayrollType payrollType = new PayrollType(
                empInfo.getEmpNm(),
                empInfo.getDeptNm(),
                getHour(empId,false),
                getHour(empId,true),
                calcPay(160, workPay, false),
                calcPay(10, 0, true)
        );
        return payrollType;
    }

    // 총 근무시간 + 오버타임 계산
    public Integer getHour(Long empId, Boolean isOverHour){
        ReadWorkhoursRequestDto param = new ReadWorkhoursRequestDto();
        param.setEmpId(empId);
        param.setIsOvertime(isOverHour);

        List<WorkhourResponseDto> workHourList = workhourService.getWorkhours(param); // empId와 overHour, workHour 둘중 하나 선택하는 걸 받아서  해당 되는 WorkhourResponseDto의 리스트들 불러옴

        if (isOverHour){
            int overHour = (int) workHourList.stream().mapToLong(WorkhourResponseDto::getOvertimeHour).sum();
            int overMinute = (int) workHourList.stream().mapToLong(WorkhourResponseDto::getOvertimeMinute).sum();

            int lastMinute = overMinute%60>30?1:0;

            overHour = overHour + (overMinute/60) + lastMinute;

            return overHour;
        }else{
            int workHour = (int) workHourList.stream().mapToLong(WorkhourResponseDto::getHour).sum();
            int workMinute = (int) workHourList.stream().mapToLong(WorkhourResponseDto::getMinute).sum();

            int lastMinute = workMinute%60>30?1:0;

            workHour = workHour + (workMinute/60) + lastMinute;

            return workHour;
        }

    }

    //전체 사원 조회 받아서 부서별 필터링
    public List<Employee> filterByDept(String deptId, List<Employee> employees){
        return employees.stream().filter(item -> item.getDepartment().getDeptId().equals(deptId)).toList();
    }

    //전체 사원 조회 받아서 직급별 필터링
    public List<Employee> filterByGrade(String gradeId, List<Employee> employees){
        return employees.stream().filter(item -> item.getGrade().getGradeId().equals(gradeId)).toList();
    }

    //전체 사원 조회 받아서 이름별 필터링
    public List<Employee> filterByName(String nameId, List<Employee> employees){
        return employees.stream().filter(item -> item.getEmpNm().equals(nameId)).toList();
    }

    public List<PayrollDetail> getDetails(Long empId){
        // 조회하고자하는 payroll의 사원id를 사용하여 items 불러오는거
//        log.info("empId : {}", empId);
        return payrollDetailRepository.findByEmpId(empId);
    }

    //U (Update)
    @Transactional
    public void updatePayroll(PayrollType request) {
        Payroll payroll = Payroll.builder()
                .payHistId(request.getPayHistId())
                .empId(request.getEmpId())
                .payDate(request.getPayDate())
                .status(PayrollStatus.from(request.getStatus()).getDisplayName())
                .build();

        // 매핑한 payroll로 업데이트
        payrollRepository.save(payroll);

        // payrollDetail은 따로 업데이트 해야 함.
        syncPayrollDetails(request.getItems(), request.getEmpId());
    }

    //D (단건 삭제)
    public boolean deletePayroll(Long id) {
        if (!payrollRepository.existsById(id)) {
            return false; // 해당 ID 없음
        }

        try {
            payrollRepository.deleteById(id);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 삭제 중 오류
        }
    }

    //D(L) (다건 삭제)
    public boolean deletePayrolls(List<Long> payIds) {
        if (payIds == null || payIds.isEmpty()) {
            return false; // 삭제할 ID가 없음
        }

        try {
            payrollRepository.deleteAllById(payIds);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 중간에 오류 발생
        }
    }

    public Integer calcPay(int time, int pay, boolean isOver){
        int workPay = time * pay;
        if(isOver){
            workPay = (int)Math.round(time * ((double) workPay /209) * 1.5);
        }
        return workPay;
    }

    public List<PayrollItem> getPayrollItem(){
        return itemNmRepository.findAll();
    }


}
