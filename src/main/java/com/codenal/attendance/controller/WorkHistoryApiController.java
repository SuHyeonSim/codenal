package com.codenal.attendance.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.service.WorkHistoryService;
import com.codenal.security.vo.SecurityUser;

@RestController
@RequestMapping("/api/work-history")
public class WorkHistoryApiController {	

    @Autowired
    private WorkHistoryService workHistoryService;

    /**
     * 현재 인증된 사용자의 ID를 가져오는 메서드
     * @return 사용자 ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUser) {
            SecurityUser userDetails = (SecurityUser) authentication.getPrincipal();
            return userDetails.getEmpId(); // SecurityUser에서 empId를 가져옴
        } else {
            throw new IllegalStateException("인증된 사용자가 아닙니다.");
        }
    }

    /**
     * 특정 날짜 범위의 근무 내역을 조회하는 API
     * @param startDate 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate 종료 날짜 (yyyy-MM-dd 형식)
     * @param pageable 페이지 정보
     * @return 근무 내역 페이지
     */
    @GetMapping("/records")
    public ResponseEntity<Page<WorkHistoryDto>> getWorkHistoriesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "workHistoryDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Long empId = getCurrentUserId();
        Page<WorkHistoryDto> workHistories = workHistoryService.getHistoriesByRange(empId, startDate, endDate, pageable);
        return ResponseEntity.ok(workHistories);
    }

    /**
     * 현재 사용자의 모든 근무 내역을 조회하는 API
     * @param pageable 페이지 정보
     * @return 근무 내역 페이지
     */
    @GetMapping("/all")
    public ResponseEntity<Page<WorkHistoryDto>> getAllWorkHistories(
            @PageableDefault(size = 10, sort = "workHistoryDate", direction = Sort.Direction.DESC) Pageable pageable) { 
        Long empId = getCurrentUserId();
        Page<WorkHistoryDto> workHistories = workHistoryService.getHistories(empId, pageable);
        return ResponseEntity.ok(workHistories);
    }

    // 필요한 경우 추가적인 엔드포인트를 구현할 수 있습니다.
}
