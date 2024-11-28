	package com.codenal.attendance.controller;
	
	import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.service.AttendanceService;
import com.codenal.security.vo.SecurityUser;
	
	@Controller
	
	public class AttendanceViewController {
	
		
		 	@Autowired
		    private AttendanceService attendanceService;
		 	
		 	  // 현재 사용자의 empId를 가져오는 메서드
		    private Long getCurrentUserId() {
		        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
		            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
		            return userDetails.getEmpId(); // empId를 반환
		        } else {
		            throw new IllegalStateException("인증된 사용자가 아닙니다.");	
		        }
		    }
		
		    @GetMapping("/apps-attendance")
		    public String showAttendancePage(
		            @RequestParam(value = "year", required = false) Integer year,
		            @RequestParam(value = "month", required = false) Integer month,
		            @RequestParam(value = "startDate", required = false) String startDate,
		            @RequestParam(value = "endDate", required = false) String endDate,
		            @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable, 
		            Model model) {

		        Long empId = getCurrentUserId();

		        // 연도와 월 기본값 설정
		        YearMonth selectedYearMonth = (year != null && month != null) ? YearMonth.of(year, month) : YearMonth.now();
		        
		        // 검색 기간이 없는 경우 기본값 설정
		        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : selectedYearMonth.atDay(1);
		        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : selectedYearMonth.atEndOfMonth();

		        // 출퇴근 기록 조회
		        Page<AttendanceDto> attendances = attendanceService.getAttendancesByDateRange(empId, start, end, pageable);

		        // 모델에 검색 결과 및 페이지 정보 추가
		        model.addAttribute("attendances", attendances);
		        model.addAttribute("currentPage", attendances.getNumber());
		        model.addAttribute("totalPages", attendances.getTotalPages());

		        // 선택된 연도와 월 정보 추가
		        String currentMonth = selectedYearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));
		        model.addAttribute("currentMonth", currentMonth);

		        // 검색 후 날짜 필드는 초기화
		        model.addAttribute("startDate", null);
		        model.addAttribute("endDate", null);

		        return "apps/attendance";
		    }
	}