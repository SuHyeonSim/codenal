package com.codenal.employee.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/mypage")
    public String showMyPage(Model model, Authentication authentication, HttpSession session) {
        // 로그인한 사용자의 ID를 가져온다고 가정 (이 예에서는 인증 객체에서 사용자 ID를 얻음)
        Long empId = Long.parseLong(authentication.getName());

        // 서비스에서 직원 정보 조회
        Employee employee = employeeService.getEmployeeById(empId);
        session.setAttribute("signatureImage", employee.getEmpSignImage() != null ? employee.getEmpSignImage() : null);
        session.setAttribute("profileImage", employee.getEmpProfilePicture());
        // 모델에 직원 정보 추가
        model.addAttribute("employee", employee);

        return "pages/mypage";  
    }
   
    @PostMapping("/mypage/updateProfile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(
                                @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                                @RequestParam(value = "empName", required = false) String empName,
                                @RequestParam(value = "empPhone", required = false) String empPhone,
                                @RequestParam(value = "empAddress", required = false) String empAddress,
                                @RequestParam(value = "empAddressDetail", required = false) String empAddressDetail,
                                @RequestParam(value = "signatureImage",required = false) String signatureImage,
                                Authentication authentication,HttpSession session) {
        Long empId = Long.parseLong(authentication.getName());

        Map<String, Object> response = new HashMap<>();
        
        try {
            // 직원 정보 가져오기
            Employee employee = employeeService.getEmployeeById(empId);
            
            // 프로필 이미지 처리 (Base64로 변환 후 DB에 저장)
            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    // 파일을 Base64로 변환
                    String base64ProfileImage = Base64.getEncoder().encodeToString(profileImage.getBytes());
                    // 데이터베이스에 저장 (Base64 포맷으로)
                    employee.setEmpProfilePicture("data:image/jpeg;base64," + base64ProfileImage);  // 프로필 이미지 저장
                    session.setAttribute("profileImage", "data:image/jpeg;base64," + base64ProfileImage);  // 세션에 저장
                } catch (IOException e) {
                    e.printStackTrace();
                    response.put("success", false);	
                    response.put("error", "Failed to process profile image.");
                    return ResponseEntity.status(500).body(response);
                }
            }

            // 서명 이미지 처리
            if (signatureImage != null) {
                try {
                    // 서명 이미지가 빈 문자열인 경우 (즉, 서명 삭제 요청)
                    if (signatureImage.trim().isEmpty()) {
                        employee.setEmpSignImage(null);  // 서명 이미지 삭제
                        session.setAttribute("signatureImage", null);  // 세션에서 제거
                    } else {
                        // 서명 이미지가 존재할 경우 저장
                        employee.setEmpSignImage(signatureImage);  // 서명 이미지 저장
                        session.setAttribute("signatureImage", signatureImage);  // 세션에 저장
                    }

                    response.put("filePath", signatureImage);  // 클라이언트에 반환할 서명 정보
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.put("success", false);
                    response.put("error", "Invalid Base64 format for signature image.");
                    return ResponseEntity.status(500).body(response);
                }
            }

            employee.setEmpName(empName);
            employee.setEmpPhone(empPhone);
            employee.setEmpAddress(empAddress);
            employee.setEmpAddressDetail(empAddressDetail);

            employeeService.saveEmployee(employee);

            response.put("success", true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("error", "Failed to save profile or signature image.");
            return ResponseEntity.status(500).body(response);
        }
    }
}