package com.codenal.attendance.controller;

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

import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.service.WorkHistoryService;
import com.codenal.security.vo.SecurityUser;

@Controller
public class WorkHistoryViewController {

    @Autowired
    private WorkHistoryService workHistoryService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId(); // empId를 반환
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }

    /**
     * 근무 내역 조회 페이지
     * @param year 선택된 연도 (기본값: 현재 연도)
     * @param month 선택된 월 (기본값: 현재 월)
     * @param pageable 페이지 정보
     * @param model 모델 데이터
     * @return 뷰 템플릿 경로
     */
    @GetMapping("/apps-work-history")
    public String showWorkHistoryPage(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @PageableDefault(size = 10, sort = "workHistoryDate", direction = Sort.Direction.DESC) Pageable pageable, 
            Model model) {

        Long empId = getCurrentUserId();

        // 선택된 연도와 월이 없으면 현재 연도와 월로 설정
        YearMonth selectedYearMonth;
        if (year != null && month != null) {
            selectedYearMonth = YearMonth.of(year, month);
        } else {
            selectedYearMonth = YearMonth.now();
        }

        // 시작일과 종료일 계산
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : selectedYearMonth.atDay(1);
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : selectedYearMonth.atEndOfMonth();

        // 근무 내역 조회
        Page<WorkHistoryDto> workHistories = workHistoryService.getHistoriesByRange(empId, start, end, pageable);

        // 모델에 데이터 추가
        model.addAttribute("workHistories", workHistories);
        model.addAttribute("currentPage", workHistories.getNumber());
        model.addAttribute("totalPages", workHistories.getTotalPages());
        model.addAttribute("selectedYear", selectedYearMonth.getYear());
        model.addAttribute("selectedMonth", selectedYearMonth.getMonthValue());

        // 현재 선택된 연도와 월을 "yyyy.MM" 형식으로 포맷
        String currentMonth = selectedYearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM"));
        model.addAttribute("currentMonth", currentMonth);

        // 검색 후 날짜 필드는 초기화
        model.addAttribute("startDate", null);
        model.addAttribute("endDate", null);

        return "apps/workHistory";
    }
}
