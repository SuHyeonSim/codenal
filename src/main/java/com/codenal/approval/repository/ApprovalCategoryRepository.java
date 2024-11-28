package com.codenal.approval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalCategory;

public interface ApprovalCategoryRepository extends JpaRepository<ApprovalCategory, Integer> {

	
	@Query(value="select c from ApprovalCategory c where c.cateCode = ?1")
	ApprovalCategory findByCateCode(int cateCode);
	
}
