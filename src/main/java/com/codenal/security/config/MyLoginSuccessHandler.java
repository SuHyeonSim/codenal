package com.codenal.security.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import com.codenal.security.vo.SecurityUser;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final EmployeeService employeeService;

    public MyLoginSuccessHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 사용자 정보 가져오기
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        // 인증 성공 후 응답 처리
        response.sendRedirect("/");
    }
}
