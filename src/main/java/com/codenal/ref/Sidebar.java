package com.codenal.ref;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;

@Controller
public class Sidebar {
	
	private EmployeeService employeeService;
	
	@Autowired
	public Sidebar(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@GetMapping("/sidebar")
	public void sideBar(Model model) {
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("empId" , empId);
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = EmployeeDto.fromEntity(emp);
		model.addAttribute("empAuth" , empDto.getEmpAuth());
		System.out.println(empDto.getEmpAuth());
	}

}
