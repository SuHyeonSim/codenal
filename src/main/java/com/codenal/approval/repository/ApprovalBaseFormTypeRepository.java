package com.codenal.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalBaseFormType;

public interface ApprovalBaseFormTypeRepository extends JpaRepository <ApprovalBaseFormType, Integer> {
	
	ApprovalBaseFormType findByBaseFormName(String formName);
	
	@Query(value="select b from ApprovalBaseFormType b where b.baseFormCode = ?1")
	ApprovalBaseFormType findByBaseFormCode(int no);
}