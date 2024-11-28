package com.codenal.calendar.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.service.CalendarService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CalendarApiController {
	
	private final CalendarService calendarService;
	
	@Autowired
	public CalendarApiController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	// 
	
	// 일정 작성자 정보 출력
	@ResponseBody
	@PostMapping("/eventWriter{id}")
	public Map<String, Object> eventWriter(@PathVariable("id") Long empId){
		Map<String, Object> result = new HashMap<String, Object>();
		String[] writer = calendarService.eventWriter(empId);
		if(writer != null) {
			result.put("name", writer[0]);	// 이름
			result.put("dept", writer[1]); // 부서
			result.put("job", writer[2]);	// 직급
		}
		
		return result;
	}
	
	// 일정 수정
	@ResponseBody
	@PostMapping("/modify/event")
	public void modifyEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarDto dto = new CalendarDto();
		dto.setCalendar_schedule_no(Long.parseLong(request.getParameter("eventId")));
		dto.setCalendar_schedule_category(Long.parseLong(request.getParameter("category")));
		dto.setCalendar_schedule_title(request.getParameter("title"));
		dto.setCalendar_schedule_location(request.getParameter("location"));
		dto.setCalendar_schedule_content(request.getParameter("content"));
		dto.setCalendar_schedule_writer(Long.parseLong(request.getParameter("writer")));
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		dto.setCalendar_schedule_start_date(LocalDateTime.parse(startDate, dtf));
		if(endDate.equals("null") == false) {
			dto.setCalendar_schedule_end_date(LocalDateTime.parse(endDate, dtf));
		}
		calendarService.modifyEvent(dto);
	}
	
	// 일정 삭제
	@ResponseBody
	@DeleteMapping("/delete/event{id}")
	public Map<String, String> deleteEvent(@PathVariable("id") Long eventNo){
		Map<String, String> result = new HashMap<String, String>();
		
		calendarService.deleteEvent(eventNo);
		return result;
	}
	
	// 일정 목록 조회
	@ResponseBody
	@PostMapping("/eventList{id}")
	public Map<String, Object> selectEvent(@PathVariable("id") Long writer) {
		Map<String, Object> resultEvent = new HashMap<String, Object>();
		List<CalendarDto> eventList = calendarService.selectEvent(writer);
		JSONObject obj = new JSONObject();
        JSONArray arr = new JSONArray();
 
		
		if(eventList != null) {
			resultEvent.put("eventList", eventList);
//			for(int i=0; i<=eventList.size(); i++) {
//				resultEvent.put("id", eventList.get(i).getCalendar_schedule_no());
//				resultEvent.put("category", eventList.get(i).getCalendar_schedule_category());
//				resultEvent.put("title", eventList.get(i).getCalendar_schedule_title());
//				resultEvent.put("start", eventList.get(i).getCalendar_schedule_start_date());
//				resultEvent.put("end", eventList.get(i).getCalendar_schedule_end_date());
//				resultEvent.put("location", eventList.get(i).getCalendar_schedule_location());
//				resultEvent.put("description", eventList.get(i).getCalendar_schedule_content());
//				resultEvent.put("writer", eventList.get(i).getCalendar_schedule_writer_name());
//				resultEvent.put("color", eventList.get(i).getCalendar_schedule_color());
//				
//				obj = new JSONObject(resultEvent);
//	            arr.put(obj);
//			}
		}
		
		return resultEvent;
	}
	
	// 일정 생성
	@ResponseBody
	@PostMapping("/create/event")
	public void createEvent(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CalendarDto dto = new CalendarDto();
		dto.setCalendar_schedule_category(Long.parseLong(request.getParameter("category")));
		dto.setCalendar_schedule_title(request.getParameter("title"));
		dto.setCalendar_schedule_location(request.getParameter("location"));
		dto.setCalendar_schedule_content(request.getParameter("content"));
		dto.setCalendar_schedule_writer(Long.parseLong(request.getParameter("writer")));
		String startDate = request.getParameter("start_date");
		String endDate = request.getParameter("end_date");
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		dto.setCalendar_schedule_start_date(LocalDateTime.parse(startDate, dtf));
		if(endDate != null && endDate.equals("null") == false) {
			dto.setCalendar_schedule_end_date(LocalDateTime.parse(endDate, dtf));
		}
		calendarService.createEvent(dto);
	}

}
