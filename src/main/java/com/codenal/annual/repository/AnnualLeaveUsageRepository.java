package com.codenal.annual.repository;



import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.annual.domain.AnnualLeaveUsage;

public interface AnnualLeaveUsageRepository extends JpaRepository <AnnualLeaveUsage ,Long> {
	
	AnnualLeaveUsage getAnnualLeaveUsageByApproval_ApprovalNo(Long approvalNo);

	AnnualLeaveUsage findByAnnualUsageNo(Long annualUsageNo);
	
	    // 사용자의 모든 연차 사용 내역 조회 (페이지네이션 적용)
	    Page<AnnualLeaveUsage> findByEmployee_EmpId(Long empId, Pageable pageable);
	    
	    
	 // 특정 월의 연차 사용 내역만 가져오는 메소드 추가
	    Page<AnnualLeaveUsage> findByEmployee_EmpIdAndAnnualUsageStartDateBetween(
	        Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable);

	}

