// src/main/java/com/codenal/employee/controller/GlobalControllerAdvice.java

package com.codenal.employee.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final EmployeeService employeeService;

    @Autowired
    public GlobalControllerAdvice(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ModelAttribute("empDto")
    public EmployeeDto populateEmployeeDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            try {
                // 현재 로그인한 사용자의 empId를 가져옴
                Long empId = Long.parseLong(authentication.getName());
                // empId를 기반으로 EmployeeDto를 가져옴
                EmployeeDto empDto = employeeService.getEmployeeDtoById(empId);
                return empDto;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null; // 비로그인 상태일 경우 null
    }
    
    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        LocalDateTime loginTime = (LocalDateTime) session.getAttribute("loginTime");
        model.addAttribute("loginTime", loginTime);
    }
}
