package com.codenal.alarms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.service.AlarmsService;
import com.codenal.employee.service.EmployeeService;

@RestController
@RequestMapping("/api/alarms")
public class AlarmsApiController {

	private final AlarmsService alarmsService;

	@Autowired
	public AlarmsApiController(AlarmsService alarmsService) {
		this.alarmsService = alarmsService;
	}

	// 알림 읽음 처리 엔드포인트
	@PostMapping("/read/{alarmNo}")
	public void markAsRead(@PathVariable Long alarmNo) {
		alarmsService.markAsRead(alarmNo);
	}

	// 알림 목록 가져오기 (로그인한 사용자 정보 활용)
    @GetMapping("/list")
    public List<AlarmsDto> getAlarmsList() {
        // 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 로그인된 사용자의 empId 가져오기
        Long empId = Long.parseLong(userDetails.getUsername());
        
        System.out.println("알림 목록 불러오니");
        
        return alarmsService.getAlarmsByEmp(empId);
    }

	// 알림 삭제
	@DeleteMapping("/delete/{alarmNo}")
	public void deleteAlarm(@PathVariable Long alarmNo) {
		alarmsService.deleteAlarm(alarmNo);
	}

}