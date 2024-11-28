package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Referrer;

public interface ReferrerRepository extends JpaRepository <Referrer, Long>{
	
	List<Referrer> findByApproval(Approval approval);
}
