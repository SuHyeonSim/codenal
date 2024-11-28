package com.codenal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;
import com.codenal.admin.service.AdminService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class CodenalApplication {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdminService adminService;

    public static void main(String[] args) {
        SpringApplication.run(CodenalApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // 모든 직원의 비밀번호를 확인하고 암호화하여 저장
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            adminService.encryptAndSaveEmployeePassword(employee);
        }
    }
}
