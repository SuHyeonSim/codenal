package com.codenal.alarms.controller;

import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alarms")
public class AlarmsViewController {

    private final AlarmsService alarmsService;
    
    @Autowired
	public AlarmsViewController(AlarmsService alarmsService) {
		this.alarmsService = alarmsService;
	}

    // 알림 리스트를 뷰에 전달
    @GetMapping("/topbar")
    public String getAlarmsForTopbar(Model model) {
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 로그인된 사용자의 empId 가져오기
        Long empId = Long.parseLong(userDetails.getUsername());
        
        List<AlarmsDto> alarms = alarmsService.getAlarmsByEmp(empId);
        
        System.out.println("알림 가니?");
        
        model.addAttribute("alarms", alarms);
        return "partials/topbar"; // 알림 목록을 보여줄 HTML 경로
    }
}

