package com.codenal.admin.controller;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenal.admin.service.AdminService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;

@Controller
@RequestMapping("/admin/api") 
public class AdminApiController {

	private static final Logger logger = LoggerFactory.getLogger(AdminApiController.class);
	private final AdminService adminService;

	@Autowired
	public AdminApiController(AdminService adminService) {
		this.adminService = adminService;
	}

	// 신규 직원 등록
	@ResponseBody
	@PostMapping("/join")
	public Map<String, String> joinEmployee(@RequestBody EmployeeDto dto) {
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "등록 중 오류가 발생했습니다.");

		if (adminService.createEmployee(dto) > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "신규 직원이 성공적으로 등록되었습니다.");
		}

		return resultMap;
	}

	// 직원 상세 정보
	@ResponseBody
	@GetMapping("/detail/{empId}")
	public Map<String, Object> getEmployeeDetail(@PathVariable("empId") Long empId) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			EmployeeDto employeeDetail = adminService.employeeDetail(empId);
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "성공적으로 조회되었습니다.");
			resultMap.put("data", employeeDetail);
		} catch (Exception e) {
			resultMap.put("res_code", "404");
			resultMap.put("res_msg", "직원을 찾을 수 없습니다.");
		}
		return resultMap;
	}

	// 직원 정보 수정
	  @PostMapping("/update/{empId}")
	    public Map<String, String> updateEmployeeApi(@PathVariable Long empId, @RequestBody EmployeeDto dto) {
	        Map<String, String> resultMap = new HashMap<>();
	        resultMap.put("res_code", "404");
	        resultMap.put("res_msg", "직원 정보 수정 중 오류가 발생했습니다.");

	        logger.info("api 1: " + empId);
	        logger.info("api 2: " + dto);

	        Employee updatedEmployee = adminService.updateEmployee(empId, dto);
	        if (updatedEmployee != null) {
	            resultMap.put("res_code", "200");
	            resultMap.put("res_msg", "직원 정보가 성공적으로 수정되었습니다.");
	            logger.info("api 3: " + empId);
	        } else {
	            logger.error("api 4: " + empId);
	        }
	        return resultMap;
	    }
}