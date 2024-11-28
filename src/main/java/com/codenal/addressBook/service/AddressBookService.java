package com.codenal.addressBook.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;
import com.codenal.admin.repository.AdminRepository;
import com.codenal.admin.repository.DepartmentsRepository;
import com.codenal.admin.repository.JobsRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;

@Service
public class AddressBookService {

	private final  DepartmentsRepository departmentsRepository;
	private final AdminRepository adminRepository;
	private final JobsRepository jobsRepository;
	
	@Autowired
	public AddressBookService(DepartmentsRepository departmentsRepository, AdminRepository adminRepository, JobsRepository jobsRepository) {
		this.departmentsRepository = departmentsRepository;
		this.adminRepository = adminRepository;
		this.jobsRepository=jobsRepository;
	}

	// ------------------직원 목록 검색 (직원 정보)
	public Page<EmployeeDto> searchByEmployeeInfo(EmployeeDto searchDto, Pageable pageable) {
	    Page<Employee> addressBookSearch = null;

	    String searchText = searchDto.getSearch_text();

	    if (searchText != null && !"".equals(searchText)) {
	        int searchType = searchDto.getSearch_type();

	        switch (searchType) {
	            case 1:
	                addressBookSearch = adminRepository.findAllByEmpAuthAndEmpStatus("USER", "Y", pageable);
	                break;
	            case 2:
	                addressBookSearch = adminRepository.findByEmpNameContainingAndEmpAuthAndEmpStatus(searchText, "USER", "Y", pageable);
	                break;
	            case 3:
	                addressBookSearch = adminRepository.searchByDeptName(searchText, "USER", "Y", pageable);
	                break;
	            case 4:
	                addressBookSearch = adminRepository.searchByJobName(searchText, "USER", "Y", pageable);
	                break;
	            case 5:
	                addressBookSearch = adminRepository.findByEmpPhoneContainingAndEmpAuthAndEmpStatus(searchText, "USER", "Y", pageable);
	                break;
	        }
	    } else {
	        addressBookSearch = adminRepository.findAllByEmpAuthAndEmpStatus("USER", "Y", pageable);
	    }

	    List<EmployeeDto> addressBookSearchList = new ArrayList<>();
	    for (Employee e : addressBookSearch) {
	        EmployeeDto dto = EmployeeDto.fromEntity(e);
	        addressBookSearchList.add(dto);
	    }

	    return new PageImpl<>(addressBookSearchList, pageable, addressBookSearch.getTotalElements());
	}


	// TreeMenu(JsTree)
	public List<TreeMenuDto> getTreeMenu() {
		List<Departments> departments = departmentsRepository.findAll();	// 전 부서 조회
		// System.out.println("Departments Fetched: " + departments);  

		// 직원(자식) 리스트를 만들고 부서(부모)가 생성 -> 노드에 들어갈 데이터 미리 준비
		return departments.stream().map(department -> {
			// 부서 노드 추가
			List<TreeMenuDto> employeeNodes = adminRepository.findByDepartments_DeptNoAndEmpAuthAndEmpStatus(department.getDeptNo(), "USER", "Y").stream()
					.sorted(Comparator.comparing(employee -> employee.getJobs().getJobPriority())) // Comparator.comparing = 오름차순
					.map(employee -> {
						TreeMenuDto employeeNode = TreeMenuDto.builder()
								.nodeId(employee.getEmpId())   
								.nodeName(employee.getEmpName() + " (" + employee.getJobs().getJobName() + ")") // 직원(자식) + 직급명
								.nodeState(TreeMenuDto.NodeState.builder().opened(false).build()) // 직원은 닫혀있기
								.build();

						return employeeNode;
					})
					.collect(Collectors.toList());

			// 직원 노드 추가
			TreeMenuDto departmentNode = TreeMenuDto.builder()
					.nodeId(department.getDeptNo())  
					.nodeName(department.getDeptName())    // 부서명을 노드 이름으로
					.nodeState(TreeMenuDto.NodeState.builder().opened(true).build())    // 부서는 열려있기
					.nodeChildren(employeeNodes)    // 부서 노드의 자식 = 해당 부서의 직원들
					.build();

			return departmentNode;
		}).collect(Collectors.toList());
	}
	
	
	
	
	// TreeMenu(JsTree)
	public List<TreeMenuDto> getTreeMenuAnnounce() {
	    // 전 부서 조회
	    List<Departments> departments = departmentsRepository.findAll();    
	    // 전 직급 조회
	    List<Jobs> jobs = jobsRepository.findAll();  // 모든 직급을 한 번에 조회

	    // 부서 노드 생성
	    return departments.stream().map(department -> {
	        // 각 부서별 직급을 부서의 자식 노드로 추가
	        List<TreeMenuDto> jobNodes = jobs.stream()
	            .sorted(Comparator.comparing(Jobs::getJobPriority))  // 우선순위에 따라 직급을 정렬
	            .map(job -> {
	            	long uniqueId = department.getDeptNo() * 1000L + job.getJobNo();  // deptNo를 1000으로 곱하고 jobNo를 더해 고유한 ID 생성

	                TreeMenuDto jobNode = TreeMenuDto.builder()
	                        .nodeId(uniqueId)  // 직급 ID 그대로 사용
	                        .nodeName(job.getJobName())  // 직급명
	                        .deptId(department.getDeptNo())  // 부서 ID 저장
	                        .jobId(job.getJobNo())  // 직급 ID 저장
	                        .nodeState(TreeMenuDto.NodeState.builder().opened(false).build())  // 직급 노드는 기본적으로 닫혀 있음
	                        .build();
	                return jobNode;
	            })
	            .collect(Collectors.toList());

	        // 부서 노드 생성 및 직급을 자식으로 추가
	        TreeMenuDto departmentNode = TreeMenuDto.builder()
	                .nodeId(department.getDeptNo())  // 부서 ID
	                .nodeName(department.getDeptName())  // 부서명
	                .deptId(department.getDeptNo())  // 부서 ID 저장
	                .nodeChildren(jobNodes)  // 직급을 부서의 자식으로 추가
	                .nodeState(TreeMenuDto.NodeState.builder().opened(true).build())  // 부서 노드는 기본적으로 열려 있음
	                .build();

	        return departmentNode;
	    }).collect(Collectors.toList());

	}



}