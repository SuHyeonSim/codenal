package com.codenal.annual.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.annual.domain.AnnualLeaveManage;

public interface AnnualLeaveManageRepository extends JpaRepository <AnnualLeaveManage, Long>{
	
	AnnualLeaveManage findByEmployee_EmpId(Long empId);
	
	@Modifying
	@Query("update AnnualLeaveManage m set m.annualUsedDay = m.annualUsedDay + ?2 , m.annualRemainDay = m.annualTotalDay - m.annualUsedDay "
			+ " where m.employee.empId = ?1")
	int updateDay(Long empId, Double  day);
}
