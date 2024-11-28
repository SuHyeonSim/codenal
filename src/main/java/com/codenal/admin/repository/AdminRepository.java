package com.codenal.admin.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.employee.domain.Employee;

public interface AdminRepository extends JpaRepository<Employee, Long> {

	// 1. 검색
	// 재직 상태
	@Query("SELECT e FROM Employee e WHERE e.empStatus = :empStatus AND e.empAuth = :empAuth")
	Page<Employee> searchByEmpStatusAndEmpAuth(@Param("empStatus") String empStatus, @Param("empAuth") String empAuth, Pageable pageable);

	// 사번
	@Query("SELECT e FROM Employee e WHERE e.empId = :empId AND e.empAuth = :empAuth")
	Page<Employee> searchByEmpId(@Param("empId") Long empId, @Param("empAuth") String empAuth, Pageable pageable);


	// 이름
	Page<Employee> findByEmpNameContainingAndEmpAuth(String empName, String empAuth, Pageable pageable);

	// 부서명
	Page<Employee> findByDepartments_DeptNameContainingAndEmpAuth(String deptName, String empAuth, Pageable pageable);	

	// 직급명
	Page<Employee> findByJobs_JobNameContainingAndEmpAuth(String jobName, String empAuth, Pageable pageable);

	// 전화번호
	Page<Employee> findByEmpPhoneContainingAndEmpAuth(String empPhone, String empAuth, Pageable pageable);	

	// 전체
	@Query("SELECT e FROM Employee e " +
		       "WHERE e.empAuth = :empAuth " +
		       "AND (:empStatus IS NULL OR e.empStatus = :empStatus) " + 
		       "AND (" +
		       "(COALESCE(:empId, NULL) IS NULL OR e.empId = :empId) " +  // 사번은 숫자로 정확하게 매칭
		       "OR e.empName LIKE %:keyword% " +       
		       "OR e.departments.deptName LIKE %:keyword% " +
		       "OR e.jobs.jobName LIKE %:keyword% " +  
		       "OR e.empPhone LIKE %:keyword%" +       
		       ")")
		Page<Employee> searchByMultipleFields(
		    @Param("empAuth") String empAuth, 
		    @Param("empStatus") String empStatus,
		    @Param("empId") Long empId,  // 사번 검색 (필요 없을 경우 null)
		    @Param("keyword") String keyword,  // 검색어 (사번, 직원명 등)
		    Pageable pageable);





	// 2. 퇴사
	// 직원의 상태(empStatus)를 'N'으로 업데이트하는 메서드
	@Modifying
	@Query("UPDATE Employee e SET e.empStatus = 'N' WHERE e.empId = :employeeId")
	void updateEmployeeStatusToN(@Param("employeeId") Long employeeId);


	// 3. 상세 조회 // 정보 수정
	Employee findByEmpId(Long empId);


	// 4. TreeMenu
	// 부서 번호로 직원 조회
	List<Employee> findByDepartments_DeptNoAndEmpAuthAndEmpStatus(Long deptNo, String empAuth, String empStatus);

	// 5. 주소록 (재직 중인 사람만)
	// 전체
	Page<Employee> findAllByEmpAuthAndEmpStatus(String empAuth, String empStatus, Pageable pageable);

	// 직원명
	Page<Employee> findByEmpNameContainingAndEmpAuthAndEmpStatus(String empName, String empAuth, String empStatus, Pageable pageable);

	// 부서명
	@Query("SELECT e FROM Employee e WHERE e.departments.deptName LIKE %:deptName% AND e.empAuth = :empAuth AND e.empStatus = :empStatus")
	Page<Employee> searchByDeptName(@Param("deptName") String deptName, @Param("empAuth") String empAuth, @Param("empStatus") String empStatus, Pageable pageable);

	// 직급명
	@Query("SELECT e FROM Employee e WHERE e.jobs.jobName LIKE %:jobName% AND e.empAuth = :empAuth AND e.empStatus = :empStatus")
	Page<Employee> searchByJobName(@Param("jobName") String jobName, @Param("empAuth") String empAuth, @Param("empStatus") String empStatus, Pageable pageable);

	// 전화번호
	Page<Employee> findByEmpPhoneContainingAndEmpAuthAndEmpStatus(String empPhone, String empAuth, String empStatus, Pageable pageable);


}