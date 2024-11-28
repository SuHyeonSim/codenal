package com.codenal.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codenal.attendance.domain.Attendance;
import com.codenal.attendance.domain.AttendanceDto;
import com.codenal.attendance.repository.AttendanceRepository;
import com.codenal.attendance.service.AttendanceService;
import com.codenal.security.vo.SecurityUser;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceApiController {	

    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private AttendanceRepository attendanceRepository;
    
    
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
     * 특정 날짜 범위의 출퇴근 기록을 조회하는 API
     * @param startDate 시작 날짜 (yyyy-MM-dd 형식)
     * @param endDate 종료 날짜 (yyyy-MM-dd 형식)
     * @return 출퇴근 기록 목록
     */
    @GetMapping("/records")
    public ResponseEntity<Page<AttendanceDto>> getAttendancesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Long empId = getCurrentUserId(); // 현재 사용자 ID 가져오기
        Page<AttendanceDto> attendances = attendanceService.getAttendancesByDateRange(empId, startDate, endDate, pageable);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<AttendanceDto>> getAllAttendances(
            @PageableDefault(size = 10, sort = "workDate", direction = Sort.Direction.DESC) Pageable pageable) { 
        Long empId = getCurrentUserId();
        Page<AttendanceDto> attendances = attendanceService.getAllAttendances(empId, pageable);
        return ResponseEntity.ok(attendances);
    }
 // 출근하기
    @PostMapping("/check-in")
    public ResponseEntity<String> checkIn() {
        try {
            attendanceService.checkIn();
            return ResponseEntity.ok("출근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 퇴근하기
    @PostMapping("/check-out")
    public ResponseEntity<String> checkOut() {
        try {
            attendanceService.checkOut();
            return ResponseEntity.ok("퇴근 처리가 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 출퇴근 상태 확인
    @GetMapping("/status")
    public ResponseEntity<String> getAttendanceStatus() {
        Long empId = getCurrentUserId();
        LocalDate today = LocalDate.now();

        Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, today);

        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            if (attendance.getAttendEndTime() != null) {
                return ResponseEntity.ok("퇴근완료");
            } else {
                return ResponseEntity.ok("출근완료");
            }
        } else {
            return ResponseEntity.ok("미출근");
        }
    }


 // 출근, 지각, 연차, 결근 상태를 조회하는 API
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getAttendanceSummary() {
        Long empId = getCurrentUserId();
        Map<String, Long> summary = new HashMap<>();
        summary.put("normal", attendanceService.getNormalAttendanceCount(empId));
        summary.put("late", attendanceService.getLateAttendanceCount(empId));
        summary.put("annualLeave", attendanceService.getAnnualLeaveCount(empId));
       
        return ResponseEntity.ok(summary);	
    }
}