package com.codenal.admin.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.domain.JobsDto;
import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.EmployeeDto;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

	private static final Logger logger = LoggerFactory.getLogger(AdminViewController.class);
	private final AdminService adminService;

	@Autowired
	public AdminViewController(AdminService adminService) {
		this.adminService = adminService;
	}

	// 신규 직원 등록
	@GetMapping("/join")
	public String joinPage(Model model) {
		List<DepartmentsDto> departments = adminService.getAllDepartments();
		List<JobsDto> jobs = adminService.getAllJobs(); 

		EmployeeDto employeeDto = EmployeeDto.builder()
				.jobNo(null) 
				.search_type(1) 
				.build();

		model.addAttribute("departments", departments);
		model.addAttribute("jobs", jobs);
		model.addAttribute("employeeDto", employeeDto); 
		return "admin/join"; 
	}


	// 직원 목록 검색 (재직/퇴사 + 직원 정보)
	@GetMapping("/list")
	public String searchAll(Model model,
	        @PageableDefault(page = 0, size = 10, sort = "empId", direction = Sort.Direction.DESC) Pageable pageable,
	        @ModelAttribute("searchDto") EmployeeDto searchDto) {

	    // 기본 검색 조건 설정
	    if (searchDto.getEmpStatus() == null) {
	        searchDto.setEmpStatus("ALL");
	    }
	    if (searchDto.getSearch_type() == 0) {
	        searchDto.setSearch_type(1); // 전체 검색 기본값
	    }

	    // 검색 처리
	    Page<EmployeeDto> resultList = adminService.searchAll(searchDto, pageable);

	    model.addAttribute("resultList", resultList);
	    model.addAttribute("searchDto", searchDto);

	    return "admin/list";
	}


	// 직원 정보 상세 조회
	@GetMapping("/detail/{empId}")

	public String employeeListDetail(@PathVariable("empId") Long empId, 
			Model model) {

		EmployeeDto employeeDetail = adminService.employeeDetail(empId);
		model.addAttribute("employeeDetail", employeeDetail);

		// 부서 셀렉트 박스
		//List<DepartmentsDto> departments = adminService.getAllDepartments();
		//model.addAttribute("departments", departments);

		// 퇴사자는 임시 비밀번호 발급, 수정, 퇴사 설정 불가
		String empStatus = employeeDetail.getEmpStatus();
		model.addAttribute("empStatus", empStatus);

		return "admin/detail";
	}


	// 직원 비밀번호 변경 (work1234)
	@PostMapping("/reset-password")
	@ResponseBody
	public Map<String, Object> resetPassword(@RequestBody Map<String, String> requestData) {
		Map<String, Object> response = new HashMap<>();

		String adminPw = requestData.get("adminPw");
		Long empId = Long.parseLong(requestData.get("empId"));

		// 관리자 비밀번호 확인 
		if (!adminPw.equals("work1234")) {
			response.put("success", false);
			response.put("message", "비밀번호가 일치하지 않습니다.");
			return response;
		}

		// 직원 비밀번호 변경
		try {
			adminService.resetEmployeePassword(empId, "work1234");
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "비밀번호 변경 중 오류가 발생했습니다.");
		}

		return response;
	}


	// 직원 퇴사 
	@PostMapping("/emp-end")
	@ResponseBody
	public Map<String, Object> emdEndDatePwa(@RequestBody Map<String, String> requestData) {
		Map<String, Object> response = new HashMap<>();

		String adminPw= requestData.get("adminPw");
		Long empId = Long.parseLong(requestData.get("empId"));
		String empEndDateStr = requestData.get("empEnd");

		// 관리자 비밀번호 확인 
		if (!adminPw.equals("work1234")) {
			response.put("success", false);
			response.put("message", "관리자 비밀번호가 일치하지 않습니다.");
			return response;
		}

		if (empEndDateStr == null || empEndDateStr.isEmpty()) {
			response.put("success", false);
			response.put("message", "퇴사일이 입력되지 않았습니다.");
			return response;
		}

		try {
			// 퇴사일 String -> Date
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate empEndDate = LocalDate.parse(empEndDateStr, formatter);

			// empStatus 'N' 변경
			boolean success = adminService.emdEndDate(empId, empEndDate);

			if (success) {
				response.put("success", true);
				response.put("message", "직원이 성공적으로 퇴사 처리되었습니다.");
			} else {
				response.put("success", false);
				response.put("message", "퇴사 처리 중 오류가 발생했습니다.");
			}
		} catch (DateTimeParseException e) {
			response.put("success", false);
			response.put("message", "퇴사일 형식이 올바르지 않습니다.");
		}

		return response;
	}


	 // 직원 정보 수정 (수정페이지에 상세정보 남아있기)
    @GetMapping("/update/{empId}")
    public String showUpdateForm(@PathVariable("empId") Long empId, Model model) {

        EmployeeDto dto = adminService.employeeDetail(empId);
        List<DepartmentsDto> departments = adminService.getAllDepartments();
        List<JobsDto> jobs = adminService.getAllJobs();
        model.addAttribute("employeeDetail", dto);
        model.addAttribute("departments", departments);
        model.addAttribute("jobs", jobs);

        return "admin/update";
    }

    // 직원 정보 수정
    @PostMapping("/update/{empId}")
    public String updateEmployeeForm(@PathVariable("empId") Long empId,
                                     @ModelAttribute("employeeDetail") EmployeeDto dto,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        logger.info("view 1: " + empId);
        logger.info("view 2: " + dto);

        if (result.hasErrors()) {
            logger.error("view 3: " + result.getAllErrors());
            model.addAttribute("departments", adminService.getAllDepartments());
            model.addAttribute("jobs", adminService.getAllJobs());
            return "admin/update";
        }

        adminService.updateEmployee(empId, dto);
        logger.info("view 4: " + empId);

        redirectAttributes.addFlashAttribute("success", "true");
        return "redirect:/admin/detail/" + empId;
    }

}