package com.codenal.meeting.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomImgFileService;
import com.codenal.meeting.service.MeetingRoomService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MeetingController {
	
	private MeetingRoomService meetingRoomService;
	private MeetingRoomImgFileService fileService;
	
	@Autowired
	public MeetingController(MeetingRoomService meetingRoomService, MeetingRoomImgFileService fileService) {
		this.meetingRoomService = meetingRoomService;
		this.fileService = fileService;
	}
	
	// 회의실 예약 가능 시간 수정
	@ResponseBody
	@PostMapping("/meetingRoomTimeModify")
	public void MeetingRoomTimeModify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] time = request.getParameter("time").split(",");
		String roomNo = request.getParameter("roomNo");
		String[][] times = new String[time.length][2];
		for (int i = 0; i < time.length; i++) {
			times[i] = time[i].split("~");
		}
		LocalTime[][] toTime = new LocalTime[time.length][2];
		for (int i = 0; i < times.length; i++) {
			for (int j = 0; j < times[i].length; j++) {
				toTime[i][j] = LocalTime.parse(times[i][j]);
			}
		}
		meetingRoomService.MeetingRoomTimeModify(toTime , roomNo);
	}
	
	// 회의실 수정
	@ResponseBody
	@PostMapping("/meetingRoomModify")
	public Map<String, String> modifyMeetingRoom(MeetingRoomDto dto, @RequestParam("file") MultipartFile file){
		Map<String, String> resultMap = new HashMap<String, String>();
		String saveFile = fileService.Img(file);
		String path = saveFile;
		if(saveFile != null) {
			dto.setMeeting_room_img(path);
			if(meetingRoomService.modifyMeetingRoom(dto) != null) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "회의실이 수정되었습니다.");
			}
		} else {
			resultMap.put("res_msg", "회의실 수정 중 문제가 발생했습니다.");
		}
		
		return resultMap;
	}
	
	
	
	// 예약 변경
	@ResponseBody
	@PostMapping("/meetingRoomReserveModify")
	public void meetingRoomReserveModify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MeetingRoomReserveDto reserveDto = new MeetingRoomReserveDto();
		reserveDto.setMeeting_room_reserve_no(Long.parseLong(request.getParameter("reserveNo")));
		reserveDto.setMeeting_room_no(Long.parseLong(request.getParameter("meetingRoomNo")));
		reserveDto.setMeeting_room_reserve_date(LocalDate.parse(request.getParameter("reserveDate")));
		String time = request.getParameter("reserveTime");
		reserveDto.setMeeting_room_reserve_time_no(Long.parseLong(time));
		reserveDto.setEmp_id(Long.parseLong(request.getParameter("reserveEmpId")));
		meetingRoomService.meetingRoomReserveModify(reserveDto);
	}
	
	// 예약 취소
	@DeleteMapping("/reserveDelete/{reserveNo}")
	public Map<String, String> ReserveDelete(@PathVariable("reserveNo") Long reserveNo){
		Map<String, String> result = new HashMap<String, String>();
		result.put("msg", "삭제 중 문제가 발생했습니다.");
		if(meetingRoomService.ReserveDelete(reserveNo) > 0) {
			result.put("msg", "삭제되었습니다.");
		}
		return result;
	}
	
	// 예약 리스트 조회
	@ResponseBody
	@PostMapping("/meetingRoomReserveList/{empId}")
	public Map<String, Object> MeetingRoomReserveList(@PathVariable("empId") Long empId, Model model){
		List<MeetingRoomReserveDto> dto = meetingRoomService.MeetingRoomReserveList(empId);
		Map<String, Object> result = new HashMap<String, Object>();
		if(dto != null) {
			model.addAttribute("reserveList", dto);
			result.put("reserveList", dto);
		}
		return result;
	}
	
	// 회의실 삭제
	@ResponseBody
	@DeleteMapping("/meetingRoomDelete/{meetingRoomNo}")
	public Map<String, String> MeetingRoomDelete(@PathVariable("meetingRoomNo") Long roomNo){
		Map<String, String> result = new HashMap<String, String>();
		result.put("msg", "삭제 중 문제가 발생했습니다.");
		int aaa = fileService.delete(roomNo);
		if(aaa > 0) {
			
			if(meetingRoomService.MeetingRoomDelete(roomNo) > 0) {
				result.put("msg", "삭제되었습니다.");
			}
		}
		return result;
	}
	
	// 회의실 예약 가능 시간 추가
	@ResponseBody
	@PostMapping("/meetingRoomTimeCreate")
	public void MeetingRoomTimeCreate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] time = request.getParameter("time").split(",");
		String[][] times = new String[time.length][2];
		for(int i=0; i<time.length; i++) {
			times[i] = time[i].split("~");
		}
		LocalTime[][] toTime = new LocalTime[time.length][2];
		for(int i=0; i<times.length; i++) {
			for(int j=0; j<times[i].length; j++) {
				toTime[i][j] = LocalTime.parse(times[i][j]);
			}
		}
		meetingRoomService.MeetingRoomTimeCreate(toTime);
	}
	
	// 회의실 추가
	@ResponseBody
	@PostMapping("/meetingRoomCreate")
	public Map<String, String> MeetingRoomCreate(MeetingRoomDto dto, @RequestParam("file") MultipartFile file){
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String saveFile = fileService.Img(file);
		String path = saveFile;
		if(saveFile != null) {
			dto.setMeeting_room_img(path);
			if(meetingRoomService.MeetingRoomCreate(dto) != null) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "회의실이 추가되었습니다.");
			}
		} else {
			resultMap.put("res_msg", "회의실 추가 중 문제가 발생했습니다.");
		}
		
		return resultMap;
	}
	
	// 회의실 리스트 출력
	@ResponseBody
	@PostMapping("/meetingRoom")
	public Map<String, Object> meetingRoomList(Model model) {
		List<MeetingRoomDto> mr = meetingRoomService.meetingRoomList();
		List<MeetingRoomTimeDto> time = meetingRoomService.meetingRoomTimeList();
		Map<String, Object> resultMr = new HashMap<String, Object>();
		if(mr != null) {
			resultMr.put("meetingRoom", mr);
			resultMr.put("meetingRoomTime", time);
			model.addAttribute("meetingRoom", mr);
			model.addAttribute("meetingRoomTime", time);
		}
		return resultMr;
		
	}
	
	// 회의실 예약
	@ResponseBody
	@PostMapping("/meetingRoomReserve")
	public void meetingRoomDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MeetingRoomReserveDto reserveDto = new MeetingRoomReserveDto();
		reserveDto.setMeeting_room_no(Long.parseLong(request.getParameter("meetingRoomNo")));
		reserveDto.setMeeting_room_reserve_date(LocalDate.parse(request.getParameter("reserveDate")));
		String time = request.getParameter("reserveTime");
		String[] times = time.split(",");
		reserveDto.setEmp_id(Long.parseLong(request.getParameter("reserveEmpId")));
		for(int i=0; i<times.length; i++) {
			reserveDto.setMeeting_room_reserve_time_no(Long.parseLong(times[i]));
			
			if(meetingRoomService.meetingRoomReserve(reserveDto) != null) {
				
			};
		}
	}

}
