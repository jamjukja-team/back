-- ================================
-- 공통 그룹 (COM_GROUP)
-- ================================
INSERT INTO com_group (com_group_cd, group_nm, in_emp_id, up_emp_id, create_at, update_at)
VALUES
('EMP_STATUS', '사원상태', '0000', '0000', NOW(), NOW()),
('ACCOUNT_STATUS', '계정상태', '0000', '0000', NOW(), NOW()),
('USE_STATUS', '사용여부', '0000', '0000', NOW(), NOW()),
('LEAVE_TYPE', '휴가유형', '0000', '0000', NOW(), NOW()),
('LEAVE_STATUS', '휴가상태', '0000', '0000', NOW(), NOW());

-- ================================
-- 공통 상세 (COM_DETAIL)
-- ================================
INSERT INTO com_detail (com_cd, com_group_cd, com_nm, in_emp_id, up_emp_id, create_at, update_at)
VALUES
('ACTIVE', 'EMP_STATUS', '재직', '0000', '0000', NOW(), NOW()),
('LEAVE', 'EMP_STATUS', '휴직', '0000', '0000', NOW(), NOW()),
('RETIRED', 'EMP_STATUS', '퇴사', '0000', '0000', NOW(), NOW()),

('NORMAL', 'ACCOUNT_STATUS', '정상', '0000', '0000', NOW(), NOW()),
('LOCK', 'ACCOUNT_STATUS', '잠금', '0000', '0000', NOW(), NOW()),
('DORMANT', 'ACCOUNT_STATUS', '휴면', '0000', '0000', NOW(), NOW()),

('Y', 'USE_STATUS', '사용', '0000', '0000', NOW(), NOW()),
('N', 'USE_STATUS', '미사용', '0000', '0000', NOW(), NOW()),
('AL',  'LEAVE_TYPE', '연차',      '0000','0000',NOW(),NOW()),
('BHL', 'LEAVE_TYPE', '오전반차',  '0000','0000',NOW(),NOW()),
('AHL', 'LEAVE_TYPE', '오후반차',  '0000','0000',NOW(),NOW()),
('SL',  'LEAVE_TYPE', '병가',      '0000','0000',NOW(),NOW()),
('EL',  'LEAVE_TYPE', '경조',      '0000','0000',NOW(),NOW());

-- ================================
-- 부서 (DEPARTMENT)
-- ================================
INSERT INTO department (dept_id, dept_nm, use_status_cd, in_emp_id, up_emp_id, create_at, update_at)
VALUES
('D001', '경영지원팀', 'Y', '0000', '0000', NOW(), NOW()),
('D002', '인사팀', 'Y', '0000', '0000', NOW(), NOW()),
('D003', '개발팀', 'Y', '0000', '0000', NOW(), NOW()),
('D004', '마케팅팀', 'Y', '0000', '0000', NOW(), NOW());

-- ================================
-- 직급 (GRADE)
-- ================================
INSERT INTO grade (grade_id, grade_nm, grade_level, use_status_cd, in_emp_id, up_emp_id, create_at, update_at)
VALUES
('G1', '사원', 1, 'Y', '0000', '0000', NOW(), NOW()),
('G2', '대리', 2, 'Y', '0000', '0000', NOW(), NOW()),
('G3', '과장', 3, 'Y', '0000', '0000', NOW(), NOW()),
('G4', '차장', 4, 'Y', '0000', '0000', NOW(), NOW()),
('G5', '부장', 5, 'Y', '0000', '0000', NOW(), NOW());

-- ================================
-- 사원번호 시퀀스 초기값
-- ================================
INSERT INTO emp_no_sequence (seq_id, next_no)
VALUES (1, 1011);
-- 관리자 1000 + 일반사원 1001~1010 사용되었음 → 다음 신규 사번은 1011부터

-- ================================
-- 관리자 계정 (EMPLOYEE 1명)
-- ================================
INSERT INTO employee (
    emp_no, emp_nm, birth_date, email, password,
    phone, photo, hire_date, dept_id, grade_id,
    role_cd, account_status_cd, emp_status_cd,
    in_emp_id, up_emp_id, remain_leave, create_at, update_at
)
VALUES
(1000, '관리자', '1990-01-01', 'admin@test.com',
 '$2a$10$7kBYcQnCzsS3YTUKNWO86eB9Kkp5S5eP0aWdyaKmLwEyGnTw3.dc.',
 '010-9999-9999', '', '2024-01-01', 'D001', 'G5',
 'ADMIN', 'NORMAL', 'ACTIVE',
 '0000', '0000', 15, NOW(), NOW());

-- ================================
-- 일반 사원 10명 (1001 ~ 1010)
-- 비밀번호: Test1234!
-- ================================
INSERT INTO employee (
    emp_no, emp_nm, birth_date, email, password,
    phone, photo, hire_date, dept_id, grade_id,
    role_cd, account_status_cd, emp_status_cd,
    in_emp_id, up_emp_id, remain_leave, create_at, update_at
) VALUES
(1001, '홍길동1', '1995-05-10', 'hong1@test.com', '$2a$10$7kBYcQnCzsS3YTUKNWO86eB9Kkp5S5eP0aWdyaKmLwEyGnTw3.dc.', '010-1111-1001', '', '2024-01-05', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1002, '홍길동2', '1994-04-12', 'hong2@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1002', '', '2024-01-06', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1003, '홍길동3', '1993-03-20', 'hong3@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1003', '', '2024-01-07', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1004, '홍길동4', '1992-01-15', 'hong4@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1004', '', '2024-01-08', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1005, '홍길동5', '1991-11-21', 'hong5@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1005', '', '2024-01-09', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1006, '홍길동6', '1990-09-18', 'hong6@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1006', '', '2024-01-10', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1007, '홍길동7', '1989-08-08', 'hong7@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1007', '', '2024-01-11', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1008, '홍길동8', '1988-07-17', 'hong8@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1008', '', '2024-01-12', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1009, '홍길동9', '1987-06-24', 'hong9@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1009', '', '2024-01-13', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW()),
(1010, '홍길동10', '1986-05-30', 'hong10@test.com', '$2a$10$sawUeZA9T6nCJt7uu6VwL.jT.yXmwLRgvr6I0wl.ff84GNjw6XNua', '010-1111-1010', '', '2024-01-14', 'D003', 'G1', 'USER', 'NORMAL', 'ACTIVE', '0000', '0000', 15, NOW(), NOW());
