package com.codenal.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;
import com.codenal.security.vo.SecurityUser;


@Service
@Primary 
public class SecurityService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long empId;
        try {
             empId = Long.parseLong(username); // String을 int로 변환
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("잘못된 ID형식입니다. : " + username);
        }

        Employee employee = employeeRepository.findByEmpId(empId);
        if (employee == null) {
            throw new UsernameNotFoundException("ID가 존재하지 않습니다. : " + empId);
        }

        return new SecurityUser(EmployeeDto.fromEntity(employee));  // SecurityUser로 반환
    }
    }
