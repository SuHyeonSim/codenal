package com.codenal.annual.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.annual.service.AnnualLeaveManageService;
import com.codenal.annual.service.AnnualLeaveUsageService;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import com.codenal.security.vo.SecurityUser;

@Controller
public class AnnualLeaveManageController {

    @Autowired
    private AnnualLeaveUsageService annualLeaveUsageService;

    @Autowired
    private AnnualLeaveManageService annualLeaveManageService;

    @Autowired
    private EmployeeService employeeService;

    // 현재 로그인한 사용자 ID를 가져오는 메서드
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId();
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }

    @GetMapping("/apps-annual-leave")
    public String showAnnualLeavePage(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @PageableDefault(size = 10, sort = "annualUsageStartDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Long empId = getCurrentUserId();

        // 사용자 정보 가져오기
        Employee employee = employeeService.getEmployeeById(empId);

        // 연차 관리 정보 가져오기
        AnnualLeaveManage annualLeaveManage = annualLeaveManageService.getOrCreateAnnualLeaveManageById(empId);
        if (annualLeaveManage == null) {
            annualLeaveManage = new AnnualLeaveManage();
        }

        // 기본 연도와 월을 현재 날짜로 설정
        YearMonth selectedYearMonth = (year != null && month != null) ? YearMonth.of(year, month) : YearMonth.now();
        model.addAttribute("selectedYear", selectedYearMonth.getYear());
        model.addAttribute("selectedMonth", selectedYearMonth.getMonthValue());

        // 연차 사용 내역 조회
        Page<AnnualLeaveUsageDto> annualLeaveUsages = annualLeaveUsageService.getAnnualLeaveUsage(empId, startDate, endDate, year, month, pageable);

        model.addAttribute("employee", employee);
        model.addAttribute("annualLeaveManage", annualLeaveManage);
        model.addAttribute("annualLeaveUsages", annualLeaveUsages);
        model.addAttribute("currentPage", annualLeaveUsages.getNumber());
        model.addAttribute("totalPages", annualLeaveUsages.getTotalPages());

        // 현재 선택된 연도와 월을 "yyyy.MM" 형식으로 포맷
        String currentMonthStr = selectedYearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));
        model.addAttribute("currentMonth", currentMonthStr);

        return "apps/annual_manage";
    }


}