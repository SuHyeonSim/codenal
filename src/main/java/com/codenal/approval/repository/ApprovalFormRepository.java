package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.ApprovalCategory;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;

public interface ApprovalFormRepository extends JpaRepository<ApprovalForm, Integer>{
   
   List<ApprovalForm> findByApprovalBaseFormType_BaseFormCodeAndFormVisibility(int no,char visibility);
   
   ApprovalForm findByFormCode(int cateCode);
   
   ApprovalForm findByFormName(String title);
   
   @Modifying
   @Query("update ApprovalForm f set f.formVisibility = ?2 where f.formCode = ?1")
   int updateVisibility(int id, char result);
}