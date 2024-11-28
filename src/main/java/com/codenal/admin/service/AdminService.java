package com.codenal.admin.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.domain.Jobs;
import com.codenal.admin.domain.JobsDto;
import com.codenal.admin.repository.AdminRepository;
import com.codenal.admin.repository.DepartmentsRepository;
import com.codenal.admin.repository.JobsRepository;
import com.codenal.annual.repository.AnnualLeaveManageRepository;
import com.codenal.annual.service.AnnualLeaveManageService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final AnnualLeaveManageRepository annualLeaveManageRepository;
    private final DepartmentsRepository departmentsRepository;
    private final JobsRepository jobsRepository;
    private final AnnualLeaveManageService annualLeaveManageService;

    @Autowired
    public AdminService(AdminRepository adminRepository, DepartmentsRepository departmentsRepository,
                        JobsRepository jobsRepository, AnnualLeaveManageRepository annualLeaveManageRepository,
                        AnnualLeaveManageService annualLeaveManageService, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.annualLeaveManageRepository = annualLeaveManageRepository;
        this.departmentsRepository = departmentsRepository;
        this.jobsRepository = jobsRepository;
        this.annualLeaveManageService = annualLeaveManageService;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------ 신규 직원 등록
    public int createEmployee(EmployeeDto dto) {
        int result = -1;
        try {
            // 기본 비밀번호 'work1234' 설정 및 암호화
            if (dto.getEmpPw() == null || dto.getEmpPw().isEmpty()) {
                dto.setEmpPw(passwordEncoder.encode("work1234"));
            } else {
                // 비밀번호가 제공되었을 경우 암호화
                dto.setEmpPw(passwordEncoder.encode(dto.getEmpPw()));
            }

            // 1. emp_hire 날짜를 기반으로 6자리 값 생성 (YYMMDD)
            LocalDate hireDate = dto.getEmpHire();  // DTO에서 `emp_hire` 값을 가져옴
            String hireDateString = hireDate.format(DateTimeFormatter.ofPattern("yyMMdd"));

            // 2. 랜덤 숫자 4자리 생성
            String randomDigits = String.format("%04d", new Random().nextInt(10000));

            // 3. emp_hire 6자리 + 랜덤 숫자 4자리로 emp_id 생성
            Long empId = Long.parseLong(hireDateString + randomDigits);

            dto.setEmpId(empId);

            Employee employee = dto.toEntity();

            // 부서 셀렉트박스
            Departments department = Departments.builder().deptNo(dto.getDeptNo()).build();
            employee.setDepartments(department);

            // 직무 셀렉트 박스
            Jobs job = Jobs.builder().jobNo(dto.getJobNo()).build();
            employee.setJobs(job);

            adminRepository.save(employee); // DB 저장
            
            // 연차 초기화: AnnualLeaveManageService를 통해 연차 계산 및 저장
            annualLeaveManageService.updateAnnualLeaveForEmployee(employee);

            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

 // 비밀번호 암호화 및 저장
    public void encryptAndSaveEmployeePassword(Employee employee) {
        if (employee.getEmpPw() != null && !employee.getEmpPw().startsWith("$2a$")) {
            employee.setEmpPw(passwordEncoder.encode(employee.getEmpPw()));
            adminRepository.save(employee);
        }
    }
    
    
    // 부서 셀렉트
    public List<DepartmentsDto> getAllDepartments() {
        List<Departments> departments = departmentsRepository.findAll(); // 모든 부서 정보 가져오기
        return departments.stream()
                .map(DepartmentsDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 직급 셀렉트
    public List<JobsDto> getAllJobs() {
        List<Jobs> jobs = jobsRepository.findAll(); // 모든 직급 정보 가져오기
        return jobs.stream()
                .map(JobsDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ---------------- 직원 목록 검색 1 (재직 or 퇴사)
    public Page<EmployeeDto> searchByStatus(EmployeeDto searchDto, Pageable pageable) {
        Page<Employee> adminSearchOne = null;

        String empStatus = searchDto.getEmpStatus();

        if (empStatus != null && !"".equals(empStatus)) {
            switch (empStatus) {
                case "ALL":
                    // 전체 검색
                    adminSearchOne = adminRepository.searchByMultipleFields("USER", null, null, "", pageable);
                    break;
                case "Y":
                    // 재직자 검색
                    adminSearchOne = adminRepository.searchByEmpStatusAndEmpAuth("Y", "USER", pageable);
                    break;
                case "N":
                    // 퇴사자 검색
                    adminSearchOne = adminRepository.searchByEmpStatusAndEmpAuth("N", "USER", pageable);
                    break;
                default:
                    adminSearchOne = adminRepository.searchByMultipleFields("USER", null, null, "", pageable);
            }
        } else {
            // 기본값으로 전체 검색
            adminSearchOne = adminRepository.searchByMultipleFields("USER", null, null, "", pageable);
        }

        List<EmployeeDto> adminSearchOneList = adminSearchOne.stream()
                .map(EmployeeDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(adminSearchOneList, pageable, adminSearchOne.getTotalElements());
    }

    // ------------------ 직원 목록 검색 2 (직원 정보)
    public Page<EmployeeDto> searchByEmployeeInfo(EmployeeDto searchDto, Pageable pageable) {
        Page<Employee> adminSearchTwo = null;

        String searchText = searchDto.getSearch_text();

        if (searchText != null && !"".equals(searchText)) {
            int searchType = searchDto.getSearch_type();

            switch (searchType) {
            case 1: // 전체 검색
                adminSearchTwo = adminRepository.searchByMultipleFields("USER", null, null, "", pageable);
                break;
            case 2: // 사번 검색 
                adminSearchTwo = adminRepository.searchByMultipleFields("USER", null, null, searchText, pageable);
                break;
            case 3: // 직원명 검색
                adminSearchTwo = adminRepository.findByEmpNameContainingAndEmpAuth(searchText, "USER", pageable);
                break;
            case 4: // 부서명 검색
                adminSearchTwo = adminRepository.findByDepartments_DeptNameContainingAndEmpAuth(searchText, "USER", pageable);
                break;
            case 5: // 직급명 검색
                adminSearchTwo = adminRepository.findByJobs_JobNameContainingAndEmpAuth(searchText, "USER", pageable);
                break;
            case 6: // 전화번호 검색
                adminSearchTwo = adminRepository.findByEmpPhoneContainingAndEmpAuth(searchText, "USER", pageable);
                break;
            }
        } else {
            adminSearchTwo = adminRepository.searchByMultipleFields("USER", null, null, "", pageable);  // 전체 검색
        }

        List<EmployeeDto> adminSearchTwoList = new ArrayList<>();
        for (Employee e : adminSearchTwo) {
            EmployeeDto dto = EmployeeDto.fromEntity(e);
            adminSearchTwoList.add(dto);
        }

        return new PageImpl<>(adminSearchTwoList, pageable, adminSearchTwo.getTotalElements());
    }

    // ---------------- 직원 검색 통합 (재직/퇴사 + 직원 정보)
    public Page<EmployeeDto> searchAll(EmployeeDto searchDto, Pageable pageable) {
        if (searchDto.getSearch_type() == 0) {
            // 재직 상태 검색
            return searchByStatus(searchDto, pageable);
        } else {
            // 직원 정보 검색
            return searchByEmployeeInfo(searchDto, pageable);
        }
    }

    // ---------------- 직원 정보 상세 조회
    public EmployeeDto employeeDetail(Long empId) {
        Employee e = adminRepository.findByEmpId(empId);
        return EmployeeDto.fromEntity(e);
    }
    

    // ---------------- 직원 비밀번호 변경
    public void resetEmployeePassword(Long empId, String newPw) {
        Employee e = adminRepository.findByEmpId(empId);
        e.setEmpPw(passwordEncoder.encode(newPw)); // 비밀번호 암호화하여 저장
        adminRepository.save(e);
    }


	// ---------------- 직원 퇴사
	@Transactional
	public boolean emdEndDate(Long empId , LocalDate empEnd) {
		try {
			Employee e = adminRepository.findById(empId).get();

			e.setEmpEnd(empEnd);  // 퇴사일 설정
			e.setEmpStatus("N");  // empStatus 'N'으로 변경

			adminRepository.save(e);	
			return true;
		} catch (Exception e) {

			return false;
		}
	}


	// ---------------- 직원 정보 수정
	@Transactional
	public Employee employeeUpdate(Long empId, EmployeeDto dto) { 
		Employee e = adminRepository.findByEmpId(empId);

		e.setEmpName(dto.getEmpName());

		Departments department = departmentsRepository.findByDeptNo(dto.getDeptNo());
		e.setDepartments(department);

		Jobs job = jobsRepository.findByJobNo(dto.getJobNo());
		e.setJobs(job);

		return adminRepository.save(e);
	}


	// 직원 정보 수정
	 @Transactional
	    public Employee updateEmployee(Long empId, EmployeeDto dto) { 
	        Employee e = adminRepository.findByEmpId(empId);

	        e.setEmpName(dto.getEmpName());

	        Departments department = departmentsRepository.findByDeptNo(dto.getDeptNo());
	        e.setDepartments(department);

	        Jobs job = jobsRepository.findByJobNo(dto.getJobNo());
	        e.setJobs(job);

	        return adminRepository.save(e);
	    }
}