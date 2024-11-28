package com.codenal.approval.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalCategory;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
	
	// 카운트
	int countByEmployee_EmpId(Long empId);
	
	// 상신리스트
	@Query(value = "SELECT a, f, v FROM Approval a "
            + "JOIN a.employee e "
            + "JOIN a.approvalCategory c "
            + "JOIN c.approvalForm f "
            + "LEFT JOIN a.approver v "
            + "WHERE a.approvalStatus = ?1 AND e.empId = ?2 "
            + "AND (v.approvalDate = ( "
            + "      SELECT MAX(v2.approvalDate) "
            + "      FROM Approver v2 "
            + "      WHERE v2.approval.approvalNo = v.approval.approvalNo) "
            + "		or v.approvalDate IS NULL "
            + "  ) " // v가 null인 경우도 포함
            + "AND (a.approvalTitle LIKE %?3% OR ?3 IS NULL) "
            + "GROUP BY v.approval.approvalNo "
            + "ORDER BY v.approvalDate DESC",
            countQuery = "SELECT count(DISTINCT a) FROM Approval a "
                       + "JOIN a.employee e "
                       + "JOIN a.approvalCategory c "
                       + "JOIN c.approvalForm f "
                       + "LEFT JOIN a.approver v "
                       + "WHERE a.approvalStatus = ?1 AND e.empId = ?2 "
                       + "AND (a.approvalTitle LIKE %?3% OR ?3 IS NULL) "
                       + "GROUP BY v.approval.approvalNo")
		Page<Object[]> findList(int num, Long id, String title, Pageable pageable);


		
		// 수신리스트
		@Query(value = "SELECT a, f, v FROM Approval a "
				 + "JOIN a.approver v "
	             + "JOIN a.employee e "
	             + "JOIN a.approvalCategory c "
	             + "JOIN c.approvalForm f "
	             + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 AND a.approvalStatus != 4 "
	             + "AND (a.approvalTitle LIKE %?3% OR ?3 IS NULL) ",
	       countQuery = "SELECT count(distinct a) FROM Approval a "
	    		   	  + "JOIN a.approver v "
	                  + "JOIN a.employee e "
	                  + "JOIN a.approvalCategory c "
	                  + "JOIN c.approvalForm f "
	                  + "WHERE v.approvalStatus = ?1 AND v.employee.empId = ?2 AND a.approvalStatus != 4 "
	                  + "AND (a.approvalTitle LIKE %?3% OR ?3 IS NULL) ")
		Page<Object[]> findinboxList(int status, Long empId,String title,Pageable pageable);


		// 수신참조 리스트
		@Query(value = "SELECT a, f, r FROM Approval a "
		        + "JOIN a.employee e "
		        + "JOIN a.approvalCategory c "
		        + "JOIN c.approvalForm f "
		        + "JOIN a.referrer r "
		        + "WHERE r.employee.empId = ?1 "
		        + "AND (a.approvalTitle LIKE %?2% OR ?2 IS NULL)",
		        countQuery = "SELECT count(DISTINCT a) FROM Approval a "
		        + "JOIN a.employee e "
		        + "JOIN a.approvalCategory c "
		        + "JOIN c.approvalForm f "
		        + "JOIN a.referrer r "
		        + "WHERE r.employee.empId = ?1 "
		        + "AND (a.approvalTitle LIKE %?2% OR ?2 IS NULL)")
		Page<Object[]> findReferrerList(Long empId, String title, Pageable pageable);

		
		// usage값이 null일 수도 있어서 left join 처리
		@Query("SELECT a, e, t, u, f " +
			       "FROM Approval a " +
			       "JOIN a.employee e " +
			       "JOIN a.approvalCategory c " +
			       "JOIN c.approvalForm f "+
			       "JOIN f.approvalBaseFormType t "+
			       "LEFT JOIN a.annualLeaveUsage u "+
			       "WHERE a.approvalNo = ?1")
		List<Object[]> selectByapprovalNo(Long approvalNo);
		
		
		Approval findByApprovalNo(Long approvalNo);
		
		// 상태 수정
		@Modifying
		@Query(value="update Approval a set a.approvalStatus = ?1 where a.approvalNo = ?2")
		int updateStatus(int status,Long approvalNo);

		Long findByEmployeeEmpId(Long empId);
		
		
		@Query(value="SELECT a, e FROM Approval a "
					+ "JOIN a.employee e "
		         + "where a.approvalStatus = ?1")
		List<Approval> findByApprovalStatus(int i);
		

		
		 // 승인된 연차 신청서 조회
	    @Query("SELECT a FROM Approval a WHERE a.approvalStatus = 2 AND a.approvalCategory.approvalForm.approvalBaseFormType.baseFormCode = 1")
	    List<Approval> findApprovedAnnualLeaves();
	
		



		// 메인화면
		@Query("SELECT COUNT(a) FROM Approval a WHERE a.employee.empId = ?1 AND a.approvalStatus = ?2")
		int findByEmployeeEmpIdAndApprovalStatus(Long empId, int i);
		
}

