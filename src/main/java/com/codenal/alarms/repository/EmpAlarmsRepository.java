package com.codenal.alarms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.employee.domain.Employee;

@Repository
public interface EmpAlarmsRepository extends JpaRepository <Employee, Long>{

	Optional<Employee> findByEmpId(Long empId);
	
}