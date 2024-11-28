package com.codenal.approval.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Approver;

public interface ApproverRepository extends JpaRepository<Approver, Long>{
	
	// 결재자, 합의자 조회
	List<Approver> findByApprovalApprovalNoAndApproverType(Long approvalNo, String approverType);

	
	// 결재 승인 시 상태 변경
	@Modifying
	@Query("UPDATE Approver a SET a.approvalStatus = ?1, a.approvalDate = ?2 "
	      + "WHERE a.approval.approvalNo = ?3 AND a.employee.empId = ?4")
	int updateStatus(int status, LocalDateTime ldt, Long approvalNo, Long loginId);
	
	// 현재 우선순위 확인 
	@Query("select a.approverPriority from Approver a where a.approval.approvalNo = ?1 and a.employee.empId = ?2")
	int findApproverPriority(Long approvalNo, Long empId);
	
	// 결재자 몇순위까지 있는지 카운트
	@Query("select count(a) from Approver a where a.approval.approvalNo =?1")
	Long countApprover(Long approvalNo);
	
	// 결재 처음 등록 시 첫번째 합의자 또는 결재자 상태 업데이트
	@Modifying
	@Query("update Approver a set a.approvalStatus = 1 where a.employee.empId =?1 and a.approval.approvalNo = ?2")
	int firstUpdateStatus(Long id, Long approvalNo);
	
	// 다음 결재자 status 변경
	@Modifying
	@Query("update Approver a set a.approvalStatus = 1 where a.approval.approvalNo =?1 and a.approverPriority = ?2")
	int updateNextEmpIdStatus(Long approvalNo, int nextCurrentPriority);
	
	// approver 조회
	List<Approver> findByApproval(Approval approval);
	
	// 반려 상태 update
	@Modifying
	@Query("update Approver a set a.approvalStatus = ?1 , a.rejectComment = ?2, a.approvalDate =?3 where "
			+ "  a.approval.approvalNo =?4 and a.employee.empId = ?5")
	int updateReject(int approverStatus, String rejectText, LocalDateTime ldt, Long approvalNo, Long loginId);
	
	
	// 반려 상태 가져오기
	Approver findByApprovalApprovalNoAndApprovalStatus(Long approvalNo, int approvalStatus);
	
	// 결재자 상태 1인 사람 조회
	Approver findByEmployeeEmpIdAndApprovalStatus(Long id,int status);
	
	// 메인화면
	@Query("SELECT COUNT(a) FROM Approver a WHERE a.employee.empId = ?1 AND a.approvalStatus = ?2")
	int findByEmployeeEmpIdAndApprovalStatusCount(Long id,int i);
	
	// approver 조회
	Approver findByApprovalApprovalNoAndEmployeeEmpId(Long no, Long empId);
	
	// 다음 결재자 정보 가져오기
	Approver findByApprovalApprovalNoAndApproverPriority(Long no, int currentPriority);
}

