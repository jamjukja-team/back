package com.supercoding.hrms.emp.repository;

import com.supercoding.hrms.emp.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);

    //@Query("SELECT COALESCE(MAX(e.empNo) + 1, 1001) FROM Employee e") -> RACE CONDITION!
    //Long findMaxEmpNo();

    @Override
    @EntityGraph(attributePaths = {"department", "grade"})
    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    Optional<Employee> findByEmail(String email);
}
