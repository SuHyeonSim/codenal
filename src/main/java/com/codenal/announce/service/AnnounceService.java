package com.codenal.announce.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;
import com.codenal.admin.repository.DepartmentsRepository;
import com.codenal.admin.repository.JobsRepository;
import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.domain.AnnounceFile;
import com.codenal.announce.domain.AnnounceFileDto;
import com.codenal.announce.domain.AnnounceReadAuthority;
import com.codenal.announce.repository.AnnounceFileRepository;
import com.codenal.announce.repository.AnnounceReadAuthorityRepository;
import com.codenal.announce.repository.AnnounceRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;


@Service
public class AnnounceService {

	
	private final AnnounceRepository announceRepository;
	private final AnnounceFileRepository announceFileRepository;
	private final AnnounceReadAuthorityRepository announceReadAuthorityRepository;
	private final EmployeeRepository employeeRepository;
	private final DepartmentsRepository departmentsRepository;
	private final JobsRepository jobsRepository;
	
	@Autowired
	public AnnounceService(AnnounceRepository announceRepository, AnnounceFileRepository announceFileRepository
			, AnnounceReadAuthorityRepository announceReadAuthorityRepository, EmployeeRepository employeeRepository
			, DepartmentsRepository departmentsRepository, JobsRepository jobsRepository) {
		this.announceRepository = announceRepository;
		this.announceFileRepository = announceFileRepository;
		this.announceReadAuthorityRepository = announceReadAuthorityRepository;
		this.employeeRepository = employeeRepository;
		this.departmentsRepository=departmentsRepository;
		this.jobsRepository=jobsRepository;
	}
	
	// 메인화면 최신 게시글 정보 출력
	public List<AnnounceDto> dashboardAnnounceList(Long userNo){
		List<Announce> announce = announceRepository.findMainAnnounce();
		List<AnnounceDto> announceDto = new ArrayList<AnnounceDto>();
		Employee employee = employeeRepository.findByEmpId(userNo);
		Long userJobNo = employee.getJobs().getJobNo();
		Long userDeptNo = employee.getDepartments().getDeptNo();
		for(Announce a : announce) {
			AnnounceDto dto = new AnnounceDto().toDto(a);
	        boolean canAccess = checkAccess(a, userNo, userJobNo, userDeptNo); // 권한 체크
	        dto.setCanAccess(canAccess); // DTO에 접근 권한 설정
			announceDto.add(dto);
		}
		return announceDto;
	}
	
	// 메인화면 전체 게시글 개수 카운트
	public int announceCount() {
		int count = announceRepository.findAllCount();
		return count;
	}
	
	
	public Page<AnnounceDto> selectAnnounceList(AnnounceDto searchDto, Pageable pageable, Long userNo) {
	    Page<Announce> announceList = null;
	    String searchText = searchDto.getSearch_text();
	    
	    if (searchText != null && !"".equals(searchText)) {
	        int searchType = searchDto.getSearch_type();
	        switch (searchType) {
	            case 1:
	                announceList = announceRepository.findByAnnounceTitleContaining(searchText, pageable);
	                break;
	            case 2:
	                announceList = announceRepository.findByEmployeeEmpNameContaining(searchText, pageable);
	                break;
	            case 3:
	                announceList = announceRepository.findByAnnounceTitleOrAnnounceWriterContaining(searchText, pageable);
	                break;
	        }
	    } else {
	        announceList = announceRepository.findAll(pageable);
	    }
		
	    Employee employee = employeeRepository.findByEmpId(userNo);
		Long userJobNo = employee.getJobs().getJobNo();
		Long userDeptNo = employee.getDepartments().getDeptNo();
		
	    List<AnnounceDto> announceDtoList = new ArrayList<>();
	    for (Announce a : announceList) {
	        AnnounceDto dto = new AnnounceDto().toDto(a);
	        boolean canAccess = checkAccess(a, userNo, userJobNo, userDeptNo); // 권한 체크
	        dto.setCanAccess(canAccess); // DTO에 접근 권한 설정
	        announceDtoList.add(dto);
	    }
	    
	    return new PageImpl<>(announceDtoList, pageable, announceList.getTotalElements());
	}
	
	
	private boolean checkAccess(Announce announce, Long userNo, Long userJobNo, Long userDeptNo) {
	    // readAuthorityStatus가 'N'인 경우, 무조건 접근 허용
	    if (announce.getReadAuthorityStatus() == 'N') {
	        return true;  // 권한 있음
	    }
	    
	    // readAuthorityStatus가 'Y'인 경우
	    if (announce.getReadAuthorityStatus() == 'Y') {
	        // 작성자와 로그인 사용자가 같으면 접근 허용
	        if (announce.getAnnounceWriter().equals(userNo)) {
	            return true; // 작성자가 로그인 사용자와 동일하면 바로 권한 허용
	        }

	        // 직급과 부서 비교
	        for (AnnounceReadAuthority authority : announce.getReadAuthorities()) {
	            if (authority.getJobs().getJobNo().equals(userJobNo) && 
	                authority.getDepartments().getDeptNo().equals(userDeptNo)) {
	                return true;  // 직급과 부서가 일치하면 접근 허용
	            }
	        }
	    }

	    // 접근이 허용되지 않으면 false 반환
	    return false;  // 권한 없음
	    
	}
	
	
	@Transactional
	public void updateViewCount(int announceNo, String username) {
		Long userId = Long.parseLong(username);
		Employee empId = Employee.builder()
				.empId(userId)
				.build();
		announceRepository.updateViewCount(announceNo,empId);
	}
	
	@Transactional
    public AnnounceDto selectAnnounceDetail(int announceNo) {
        Announce announce = announceRepository.findByAnnounceNo(announceNo);
        AnnounceDto dto = new AnnounceDto().toDto(announce);
        return dto;
    }
    
    
    public AnnounceDto selectAnnounceUpdateView(int announceNo) {
        Announce announce = announceRepository.findByAnnounceNo(announceNo);
        AnnounceDto dto = new AnnounceDto().toDto(announce);
        return dto;
    }

    // 게시글 단일 저장 로직
    public Announce createAnnounce(AnnounceDto dto, List<Long> deptIds, List<Integer> jobIds) {
        Announce announce = Announce.builder()
        		.announceWriter(dto.getAnnounce_writer())
                .announceTitle(dto.getAnnounce_title())
                .announceContent(dto.getAnnounce_content())
                .readAuthorityStatus(dto.getRead_authority_status())
                .build();
        
        // Announce 저장
        Announce savedAnnounce = announceRepository.save(announce);
        
        // 부서 ID와 직급 ID를 미리 한 번에 조회하여 매핑
        List<Departments> departmentsList = departmentsRepository.findAllById(deptIds);
        List<Jobs> jobsList = jobsRepository.findAllById(jobIds);
        // 부서와 직급을 함께 저장
        for (Departments dept : departmentsList) {
            for (Jobs job : jobsList) {
            	AnnounceReadAuthority announceReadAuthority = AnnounceReadAuthority.builder()
            			.announce(savedAnnounce)
                        .departments(dept)
                        .jobs(job)
                        .build();
                announceReadAuthorityRepository.save(announceReadAuthority);
            }
        }
        
        // 저장된 Announce 객체 반환
        return savedAnnounce;
    }
    
    
    // 게시글+File 저장 로직
    @Transactional
    public Announce createAnnounceAndFile(AnnounceDto dto, List<AnnounceFileDto> fileDtos, List<Long> deptIds, List<Integer> jobIds) {
        Announce announce = Announce.builder()
        		.announceWriter(dto.getAnnounce_writer())
                .announceTitle(dto.getAnnounce_title())
                .announceContent(dto.getAnnounce_content())
                .readAuthorityStatus(dto.getRead_authority_status())
                .build();
        
        // Announce 저장
        Announce savedAnnounce = announceRepository.save(announce);
        
        // 부서 ID와 직급 ID를 미리 한 번에 조회하여 매핑
        List<Departments> departmentsList = departmentsRepository.findAllById(deptIds);
        List<Jobs> jobsList = jobsRepository.findAllById(jobIds);
        // 부서와 직급을 함께 저장
        for (Departments dept : departmentsList) {
            for (Jobs job : jobsList) {
            	AnnounceReadAuthority announceReadAuthority = AnnounceReadAuthority.builder()
            			.announce(savedAnnounce)
                        .departments(dept)
                        .jobs(job)
                        .build();
                announceReadAuthorityRepository.save(announceReadAuthority);
            }
        }
        
     // AnnounceFile 객체 생성 및 저장
        for(AnnounceFileDto fileDto : fileDtos) {
            AnnounceFile aFile = AnnounceFile.builder()
                    .announce(savedAnnounce) // Announce와 연관 설정
                    .fileOriName(fileDto.getFile_ori_name())
                    .fileNewName(fileDto.getFile_new_name())
                    .filePath(fileDto.getFile_path())
                    .build();

            // AnnounceFile 저장
            announceFileRepository.save(aFile);
        }
        
        // 저장된 Announce 객체 반환
        return savedAnnounce;
    }

    @Transactional
	public int deleteAnnounce(int no) {
		int result = 0;
		try {
			announceReadAuthorityRepository.deleteByAnnounce_AnnounceNo(no);
			announceRepository.deleteById(no);
			result = 1;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
    
    
    // 게시글 단일 수정 로직
    @Transactional
    public Announce updateAnnounce(AnnounceDto dto, List<Long> deptIds, List<Integer> jobIds) {
        Announce announce = Announce.builder()
        		.announceWriter(dto.getAnnounce_writer())
                .announceTitle(dto.getAnnounce_title())
                .announceContent(dto.getAnnounce_content())
                .readAuthorityStatus(dto.getRead_authority_status())
                .build();
        
        // Announce 저장
        Announce savedAnnounce = announceRepository.save(announce);
        
        // 부서 ID와 직급 ID를 미리 한 번에 조회하여 매핑
        List<Departments> departmentsList = departmentsRepository.findAllById(deptIds);
        List<Jobs> jobsList = jobsRepository.findAllById(jobIds);
        announceReadAuthorityRepository.deleteByAnnounce_AnnounceNo(announce.getAnnounceNo());  // 기존 읽기 권한 삭제
        // 부서와 직급을 함께 저장
        for (Departments dept : departmentsList) {
            for (Jobs job : jobsList) {
            	AnnounceReadAuthority announceReadAuthority = AnnounceReadAuthority.builder()
            			.announce(savedAnnounce)
                        .departments(dept)
                        .jobs(job)
                        .build();
                announceReadAuthorityRepository.save(announceReadAuthority);
            }
        }
        
        // 저장된 Announce 객체 반환
        return savedAnnounce;
    }
    

    // 게시글+File 수정 로직
    @Transactional
    public Announce updateAnnounceAndFile(AnnounceDto dto, List<AnnounceFileDto> fileDtos, List<Long> deptIds, List<Integer> jobIds) {
        Announce announce = Announce.builder()
        		.announceNo(dto.getAnnounce_no())
        		.announceWriter(dto.getAnnounce_writer())
                .announceTitle(dto.getAnnounce_title())
                .announceContent(dto.getAnnounce_content())
                .regDate(dto.getReg_date())
                .modDate(LocalDateTime.now())
                .readAuthorityStatus(dto.getRead_authority_status())
                .build();
        
        // Announce 저장
        Announce savedAnnounce = announceRepository.save(announce);
        
        // 부서 ID와 직급 ID를 미리 한 번에 조회하여 매핑
        List<Departments> departmentsList = departmentsRepository.findAllById(deptIds);
        List<Jobs> jobsList = jobsRepository.findAllById(jobIds);
        announceReadAuthorityRepository.deleteByAnnounce_AnnounceNo(announce.getAnnounceNo());  // 기존 읽기 권한 삭제
        // 부서와 직급을 함께 저장
        for (Departments dept : departmentsList) {
            for (Jobs job : jobsList) {
            	AnnounceReadAuthority announceReadAuthority = AnnounceReadAuthority.builder()
            			.announce(savedAnnounce)
                        .departments(dept)
                        .jobs(job)
                        .build();
                announceReadAuthorityRepository.save(announceReadAuthority);
            }
        }
        
        // AnnounceFile 객체 생성 및 저장
        for(AnnounceFileDto fileDto : fileDtos) {
            AnnounceFile aFile = AnnounceFile.builder()
                    .announce(savedAnnounce) // Announce와 연관 설정
                    .fileOriName(fileDto.getFile_ori_name())
                    .fileNewName(fileDto.getFile_new_name())
                    .filePath(fileDto.getFile_path())
                    .build();

            // AnnounceFile 저장
            announceFileRepository.save(aFile);
        }
        
        // 저장된 Announce 객체 반환
        return savedAnnounce;
    }

    public void deleteFile(Integer fileNo) {
        // 파일 정보 조회
        AnnounceFile announceFile = announceFileRepository.findById(fileNo)
            .orElseThrow(() -> new IllegalArgumentException("해당 파일이 존재하지 않습니다."));
        
        // 파일 삭제 처리
        announceFileRepository.deleteById(announceFile.getFileNo());
        
        Path filePath = Paths.get(announceFile.getFilePath());

    }
}
