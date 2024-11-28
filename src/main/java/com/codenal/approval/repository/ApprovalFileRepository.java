package com.codenal.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalFile;

public interface ApprovalFileRepository extends JpaRepository <ApprovalFile, Long> {

	ApprovalFile findByApprovalApprovalNo(Long approvalNo);
	
	@Modifying
	@Query(value="delete from ApprovalFile f where f.approval.approvalNo = ?1")
	int deleteByApprovalNo(Long no);
	
}
