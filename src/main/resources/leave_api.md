# 📘 Leave(휴가) API 인터페이스

---

## 📘 휴가 신청 API

---

### 🔹 휴가 생성 API  
```http
# POST /api/leave

Body (multipart/form-data)
 1. leave <휴가 정보 JSON> : TblLeave
 2. file  <첨부파일>        : MultipartFile
```

**설명**  
- 휴가 정보를 등록합니다.  
- 파일이 있으면 S3 업로드 후 fileId가 매핑됩니다.  
- Response Type : `LeaveType`

---

### 🔹 휴가 상세 조회 API
```http
# GET /api/leave/{leaveId}

Parameter  
 1. leaveId <휴가 ID> : Long
```

**설명**  
- leaveId에 해당하는 휴가 상세 정보를 조회합니다.  
- 파일이 있을 경우 fileInfo 포함  
- Response Type : `LeaveType`

---

### 🔹 휴가 목록 조회 API
```http
# GET /api/leave

Parameter 없음
```

**설명**  
- 전체 휴가 목록을 조회합니다.  
- Response Type : `List<LeaveType>`

---

### 🔹 휴가 삭제 API
```http
# DELETE /api/leave/{leaveId}

Parameter  
 1. leaveId <휴가 ID> : Long
```

**설명**  
- leaveId에 해당하는 휴가 데이터를 삭제합니다.  
- Response Type : `Boolean`

---

### 🔹 휴가 다건 삭제 API
```http
# DELETE /api/leave

Parameter  
 1. leaveIds <삭제할 휴가 ID 리스트> : List<Long>
```

**설명**  
- 여러 건의 휴가 데이터를 한 번에 삭제합니다.  
- Response Type : `Boolean`

---

## 📘 휴가 코드 / 드롭다운 API

---

### 🔹 드롭다운 데이터 조회 API
```http
# GET /api/leave/select

Parameter
 1. grpCd <코드 그룹> : String  (leave_type / leave_status)
```

**설명**  
- 휴가 종류, 휴가 상태 등 SelectBox에 필요한 코드 리스트 조회  
- Response Type : `List<SelectType>`

---

## 📘 휴가 승인·반려 API

---

### 🔹 휴가 상태 변경 API (승인/반려)
```http
# PUT /api/leave/{leaveId}

Parameter  
 1. leaveId <휴가 ID> : Long  
 2. status  <변경할 상태> : String  (APPLY / APPROVE / REJECT)

Body  
 1. reason <반려사유> : String
```

**설명**  
- leaveId에 해당하는 휴가의 상태를 변경합니다.  
- status=REJECT 일 경우 body의 reason이 반려 사유가 됩니다.  
- Response Type : `String`

---

# 📘 데이터 구조

---

### 🔹 TblLeave  
휴가 정보 엔티티

| 필드명 | 타입 | 설명 |
|--------|------|------|
| leaveId | Long | PK |
| leaveRegDate | String | 신청일 |
| leaveStartDate | String | 시작일 |
| leaveEndDate | String | 종료일 |
| leaveType | String | 휴가 타입 |
| leaveReason | String | 사유 |
| fileId | String | 첨부파일 ID |
| leaveDuration | int | 휴가 기간 |
| leaveStatus | String | 상태 |
| empId | Long | 사번 |
| rejectReason | String | 반려 사유 |

---

### 🔹 TblFile  
파일 정보 엔티티

| 필드명 | 타입 | 설명 |
|--------|------|------|
| fileId | String | PK |
| fileNm | String | 원본 파일명 |
| fileType | String | 확장자 |
| fileLocation | String | S3 URL |

---

### 🔹 LeaveType  
응답 DTO

| 필드명 | 타입 | 설명 |
|--------|------|------|
| leaveInfo | TblLeave | 휴가 정보 |
| fileInfo | TblFile | 파일 정보 |
