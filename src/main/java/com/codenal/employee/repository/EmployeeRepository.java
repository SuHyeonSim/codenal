package com.codenal.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.employee.domain.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	// 필요시 커스텀 쿼리 추가 가능
    Employee findByEmpId(Long empId);
    

    @Query(value = "select e from Employee e where e.empName = ?1")
    Employee findByEmpName(String empName);
    
    @Query("SELECT e FROM Employee e WHERE e.empStatus = 'Y' AND e.empId != ?1 AND e.empAuth !='ADMIN' ORDER BY e.departments.deptName ")
    List<Employee> findAllActiveEmployees(Long empId);

    @Query(value="SELECT e FROM Employee e "
            + "WHERE (:searchText IS NULL OR :searchText = '' OR e.departments.deptName Like CONCAT('%', :searchText, '%') "
            + "OR e.empName LIKE CONCAT('%', :searchText, '%')) "
            + "AND e.empId != :empId "
            + "AND e.empStatus = 'Y' "
            + "AND e.empAuth != 'ADMIN' "
            + "ORDER BY e.departments.deptName DESC")
    List<Employee> findByEmpNameContainingOrDepartmentsDeptNameContaining(@Param("empId") Long empId, @Param("searchText") String searchText);
    
    
    @Query("SELECT e.empId FROM Employee e")
    List<Long> findAllEmployeeIds();
}