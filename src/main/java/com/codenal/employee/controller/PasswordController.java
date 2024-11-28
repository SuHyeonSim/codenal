package com.codenal.employee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;

@Controller
@RequestMapping("/mypage")
public class PasswordController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public PasswordController(EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updatePassword(
                                @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        Long empId = Long.parseLong(authentication.getName());

        // 직원 정보 가져오기
        Employee employee = employeeService.getEmployeeById(empId);

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(oldPassword, employee.getEmpPw())) {
            response.put("success", false);
            response.put("error", "기존 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        // 비밀번호 변경 로직
        employee.setEmpPw(passwordEncoder.encode(newPassword));
        employeeService.saveEmployee(employee);

        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}
