package com.supercoding.hrms.leave.repository;

import com.supercoding.hrms.leave.domain.TblFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<TblFile, String> {
}
