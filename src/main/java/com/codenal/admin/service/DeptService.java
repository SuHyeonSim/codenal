package com.codenal.admin.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.DepartmentsCount;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.repository.DepartmentsRepository;

@Service
public class DeptService {

	private final DepartmentsRepository departmentsRepository;

	@Autowired
	public DeptService (DepartmentsRepository departmentsRepository) {
		this.departmentsRepository = departmentsRepository;
	}


	// 부서 목록
	public Page<DepartmentsDto> searchDeptName(DepartmentsDto searchDto, Pageable pageable) {

		Page<DepartmentsCount> deptNameSearch;

		if (searchDto.getDeptName() == null || searchDto.getDeptName().isEmpty()) {
			// 부서명 검색어가 없을 경우 전체 조회
			deptNameSearch = departmentsRepository.findAllWithEmployeeCount(pageable);
		} else {
			// 부서명으로 검색
			deptNameSearch = departmentsRepository.findByDeptNameContainingWithEmployeeCount(searchDto.getDeptName(), pageable);
		}

		//System.out.println("(서비스)부서 수: " + deptNameSearch.getContent().size());

		Page<DepartmentsDto> deptNameSearchList = deptNameSearch.map(projection -> {
			DepartmentsDto dto = new DepartmentsDto();
			dto.setDeptNo(projection.getDeptNo());
			dto.setDeptName(projection.getDeptName());
			dto.setDeptCreateDate(projection.getDeptCreateDate());
			dto.setEmpCount(projection.getEmpCount() != null ? projection.getEmpCount().intValue() : 0); 
			return dto;
		});

		return deptNameSearchList;
	}


	// 부서 추가
	public int addDepartment(DepartmentsDto dto) {
		try {
			// 중복 체크 로직
			if (departmentsRepository.existsByDeptName(dto.getDeptName())) {
				throw new IllegalArgumentException("이미 존재하는 부서명입니다.");
			}

			Departments d = Departments.builder()
					.deptName(dto.getDeptName())
					.deptCreateDate(LocalDate.now())
					.build();
			departmentsRepository.save(d);

			return 1;

		} catch (IllegalArgumentException e) {
			// 중복된 부서명이 있을 때 처리
			//	System.out.println("Error: " + e.getMessage());
			return 0;
		} catch (Exception e) {
			// 그 외의 일반적인 에러 처리
			//System.out.println("Error: " + e.getMessage());
			return 0;
		}
	}


	// 부서 삭제
	@Transactional
	public int deleteDept(Long deptNo) {

		Departments department = departmentsRepository.findById(deptNo)
				.orElseThrow(() -> new IllegalStateException("존재하지 않는 부서입니다."));

		if (department.getEmpCount() > 0) {
			throw new IllegalStateException("부서에 직원이 존재하므로 삭제할 수 없습니다.");
		}

		departmentsRepository.delete(department);

		return 1; 
	}


	// 부서명 수정
	public int updateDepartment(DepartmentsDto dto) throws Exception {
		// 부서명 중복 체크
		Optional<Departments> existingDept = departmentsRepository.findByDeptName(dto.getDeptName());
		if (existingDept.isPresent() && !existingDept.get().getDeptNo().equals(dto.getDeptNo())) {
			// 이미 부서명이 존재 + 부서 번호가 다른 경우
			return 0; 
		}

		Departments departments = dto.toEntity();
		departmentsRepository.save(departments);
		return 1; 
	}
}
