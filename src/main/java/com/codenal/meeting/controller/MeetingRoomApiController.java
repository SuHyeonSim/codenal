package com.codenal.meeting.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;
import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomService;

@Controller
public class MeetingRoomApiController {
	
	private MeetingRoomService meetingRoomService;
	private EmployeeService employeeService;
	
	@Autowired
	public MeetingRoomApiController(MeetingRoomService meetingRoomService, EmployeeService employeeService) {
		this.meetingRoomService = meetingRoomService;
		this.employeeService = employeeService;
	}
	
	// 회의실 수정 화면 이동
	@GetMapping("/apps-meeting-room-modify/{meetingRoomNo}")
	public String selectMeetingRoom(@PathVariable("meetingRoomNo") Long meetingRoomNo, Model model) {
		MeetingRoomDto dto = meetingRoomService.selectMeetingRoom(meetingRoomNo);
		model.addAttribute("meetingRoom", dto);
		return "apps/meeting-room-modify";
	}
	
	// 예약 변경 페이지 이동
	@GetMapping("/reserveModify/{reserveNo}")
	public String meetingRoomModify(Model model, @PathVariable("reserveNo") Long reserveNo) {
		List<MeetingRoomDto> mr = meetingRoomService.meetingRoomList();
		List<MeetingRoomTimeDto> time = meetingRoomService.meetingRoomTimeList();
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("meetingRoom", mr);
		model.addAttribute("meetingRoomTime", time);
		model.addAttribute("empId", SecurityContextHolder.getContext().getAuthentication().getName());
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = EmployeeDto.fromEntity(emp);
		model.addAttribute("empAuth", empDto.getEmpAuth());
		model.addAttribute("nowDate", LocalDate.now());
		model.addAttribute("reserveNo", reserveNo);
		return "apps/meeting-room-reserve-modify";
	}
	
	// 회의실 예약
	@GetMapping("/apps-meeting-room")
	public String apps_ecommerce_product_details(Model model) {
		List<MeetingRoomDto> mr = meetingRoomService.meetingRoomList();
		List<MeetingRoomTimeDto> time = meetingRoomService.meetingRoomTimeList();
		Map<String, Object> result = new HashMap<String, Object>();
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("meetingRoom", mr);
		result.put("meetingRoom", mr);
		model.addAttribute("meetingRoomTime", time);
		result.put("meetingRoomTime", time);
		model.addAttribute("empId", SecurityContextHolder.getContext().getAuthentication().getName());
		Employee emp = employeeService.getEmployeeById(empId);
		EmployeeDto empDto = EmployeeDto.fromEntity(emp);
		model.addAttribute("empAuth", empDto.getEmpAuth());
		model.addAttribute("nowDate", LocalDate.now());
		return "apps/meeting-room";
	}
	
	// 회의실 추가
	@GetMapping("/apps-meeting-room-check")
	public String apps_chat() {
		return "apps/meeting-room-check";
	}
	
	// 회의실 예약 내역
	@GetMapping("/apps-meeting-room-reserve-list")
	public String apps_ecommerce_cart(Model model) {
		Long empId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("empId", empId);
		
		List<MeetingRoomReserveDto> dto = meetingRoomService.MeetingRoomReserveList(empId);
		Map<String, Object> result = new HashMap<String, Object>();
		if(dto != null) {
			model.addAttribute("reserveList", dto);
			result.put("reserveList", dto);
		}
		return "apps/meeting-room-reserve-list";
	}

}
