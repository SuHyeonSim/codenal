package com.codenal.security.vo;

import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.codenal.employee.domain.EmployeeDto;

import lombok.Getter;

@Getter
public class SecurityUser extends User {

    private static final long serialVersionUID = 1L;
    
    private EmployeeDto employeeDto;
    
    public Long getEmpId() {
        return employeeDto.getEmpId();
    }

    public SecurityUser(EmployeeDto employeeDto) {
        super(String.valueOf(employeeDto.getEmpId()), 
              employeeDto.getEmpPw(), 
              employeeDto.getAuthorities() != null ? employeeDto.getAuthorities() : Collections.emptyList());
        this.employeeDto = employeeDto;
    }
}