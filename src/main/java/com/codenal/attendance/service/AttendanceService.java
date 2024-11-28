	package com.codenal.attendance.service;
	
	import java.math.BigDecimal;
	import java.math.RoundingMode;
	import java.time.Duration;
	import java.time.LocalDate;
	import java.time.LocalTime;
	import java.time.YearMonth;
	import java.util.List;
	import java.util.Optional;
	
	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.Pageable;
	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.security.core.Authentication;
	import org.springframework.security.core.context.SecurityContextHolder;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;
	
	import com.codenal.annual.domain.AnnualLeaveUsage;
	import com.codenal.annual.repository.AnnualLeaveUsageRepository;
	import com.codenal.annual.service.AnnualLeaveUsageService;
	import com.codenal.attendance.domain.Attendance;
	import com.codenal.attendance.domain.AttendanceDto;
	import com.codenal.attendance.domain.WorkHistoryDto;
	import com.codenal.attendance.repository.AttendanceRepository;
	import com.codenal.employee.repository.EmployeeRepository;
	import com.codenal.security.vo.SecurityUser;
	
	@Service
	public class AttendanceService {
		
	    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
	
	
		private final AttendanceRepository attendanceRepository;
		
		private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
		
	    private final WorkHistoryService workHistoryService; 
	    
	    private final EmployeeRepository employeeRepository; // 추가된 필드
	    
	    private final AnnualLeaveUsageService annualLeaveUsageService;
	
		
		// 출근 기준 시간과 퇴근 기준 시간 설정
	    private final LocalTime standardStartTime = LocalTime.of(9, 0); // 오전 9시
	    private final LocalTime standardEndTime = LocalTime.of(18, 0); // 오후 6시
	    
	
		@Autowired
		public AttendanceService(AttendanceRepository attendanceRepository, AnnualLeaveUsageRepository annualLeaveUsageRepository ,WorkHistoryService workHistoryService,EmployeeRepository employeeRepository ,AnnualLeaveUsageService annualLeaveUsageService) {
			 this.attendanceRepository = attendanceRepository;
		        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
		        this.workHistoryService = workHistoryService;
		        this.employeeRepository = employeeRepository;
		        this.annualLeaveUsageService = annualLeaveUsageService;
		}
	
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
	 // 출근하기 메서드
	    @Transactional
	    public void checkIn() {
	        Long empId = getCurrentUserId();
	        LocalDate today = LocalDate.now();
	
	        // 이미 출근 기록이 있는지 확인
	        boolean exists = attendanceRepository.existsByEmpIdAndWorkDate(empId, today);
	        if (exists) {
	            throw new IllegalStateException("이미 오늘 출근 처리가 되어 있습니다.");
	        }
	
	        LocalTime now = LocalTime.now();
	
	        Attendance attendance = Attendance.builder()
	                .empId(empId)
	                .workDate(today) // LocalDate 타입 사용
	                .attendStartTime(now)
	                .attendStatus(determineStatus(empId, today, now))
	                .build();
	
	        attendanceRepository.save(attendance);
	    }
		
	 // 퇴근하기 메서드 수정
	    @Transactional
	    public void checkOut() {
	        Long empId = getCurrentUserId();
	        LocalDate today = LocalDate.now();

	        // 오늘 출근 기록이 있는지 확인
	        Attendance attendance = attendanceRepository.findByEmpIdAndWorkDate(empId, today)
	                .orElseThrow(() -> new IllegalStateException("출근 기록이 없습니다. 먼저 출근 체크를 해주세요."));

	        // 이미 퇴근한 경우 예외 처리
	        if (attendance.getAttendEndTime() != null) {
	            throw new IllegalStateException("이미 퇴근 처리가 완료되었습니다.");
	        }

	        LocalTime now = LocalTime.now();
	        attendance.setAttendEndTime(now);

	        // 근무 시간 계산 (calculateWorkingHours 메서드 사용)
	        String workingTimeFormatted = calculateWorkingHours(attendance.getAttendStartTime(), now);
	        System.out.println("근무 시간: " + workingTimeFormatted);

	        // 근무 시간 BigDecimal로 계산
	        BigDecimal workingHours = calculateWorkingHoursAsBigDecimal(attendance.getAttendStartTime(), now);

	        // 초과 근무 시간 계산
	        BigDecimal overTime = BigDecimal.ZERO;
	        if (workingHours.compareTo(BigDecimal.valueOf(9)) > 0) { // 9시간 초과 시 초과근무시간 계산
	            overTime = workingHours.subtract(BigDecimal.valueOf(9));
	        }

	        // 전체 근무 시간을 근무시간 + 초과근무시간으로 설정
	        BigDecimal totalWorkingTime = workingHours;

	        // attendWorkingTime에 근무 시간을 설정
	        attendance.setAttendWorkingTime(totalWorkingTime);

	        attendanceRepository.save(attendance);

	        // WorkHistory 기록
	        WorkHistoryDto workHistoryDto = WorkHistoryDto.builder()
	                .workHistoryDate(today)
	                .workHistoryWorkingTime(workingHours)  // 정규 근무 시간
	                .workHistoryOverTime(overTime)         // 초과 근무 시간
	                .workHistoryTotalTime(totalWorkingTime)  // 전체 근무 시간 (정규 근무 + 초과 근무)
	                .empId(empId)
	                .build();

	        workHistoryService.createOrUpdateWorkHistory(empId, workHistoryDto);
	    }
	        
	 // 근무시간을 "HH:mm" 형식으로 변환하는 메서드 (출력 시 사용)
	 	public String formatHoursMinutes(BigDecimal totalHours) {
	 	    long hours = totalHours.longValue();
	 	    long minutes = totalHours.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(60)).longValue();
	 	    return String.format("%02d:%02d", hours, minutes);
	 	}
	
	 	private BigDecimal calculateWorkingHoursAsBigDecimal(LocalTime startTime, LocalTime endTime) {
	 	    if (startTime != null && endTime != null) {
	 	        Duration duration = Duration.between(startTime, endTime);
	 	        long seconds = duration.getSeconds(); // 총 초로 계산
	 	        BigDecimal hours = BigDecimal.valueOf(seconds)
	 	                .divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP); // 두 자리까지 반올림
	 	        return hours;
	 	    } else {
	 	        return BigDecimal.ZERO;
	 	    }
	 	}
	    
	 // 연차 여부 확인 및 상태 결정
	 	public String determineStatus(Long empId, LocalDate workDate, LocalTime attendStartTime) {
	 	    // 특정 날짜에 해당하는 연차 사용 내역 조회
	 	    Page<AnnualLeaveUsage> annualLeaveUsages = annualLeaveUsageRepository
	 	        .findByEmployee_EmpIdAndAnnualUsageStartDateBetween(
	 	            empId, workDate, workDate, Pageable.unpaged());

	 	    // 연차 사용 내역이 존재하는지 여부 확인
	 	    boolean isAnnualLeave = annualLeaveUsages.hasContent();

	 	    if (isAnnualLeave) {
	 	        // 연차 내역이 존재하면 '휴가' 상태 반환
	 	        return "휴가";
	 	    } 
	 	    
	 	    // 출근 시간에 따라 정상 출근 또는 지각 상태 결정
	 	    if (attendStartTime != null) {
	 	        if (!attendStartTime.isAfter(standardStartTime)) {
	 	            // 기준 시간과 같거나 이전인 경우 '정상출근'
	 	            return "정상출근";
	 	        } else {
	 	            // 기준 시간을 초과한 경우 '지각'
	 	            return "지각";
	 	        }
	 	    } 
	 	    
	 	    // 과거의 근무 날짜에 출근 기록이 없는 경우 '결근' 반환
	 	    if (workDate.isBefore(LocalDate.now())) {
	 	        return "결근";
	 	    }

	 	    // 현재 날짜 기준으로 출근 기록이 없는 경우 '미출근' 반환
	 	    return "미출근";
	 	}
	    
	 // 해당 날짜의 출근 기록이 있는지 확인하고 없으면 결근으로 처리
	    public String checkAttendanceStatus(Long empId, LocalDate workDate) {
	        Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, workDate);
	        if (attendanceOpt.isPresent()) {
	            Attendance attendance = attendanceOpt.get();
	            return determineStatus(empId, workDate, attendance.getAttendStartTime());
	        } else if (workDate.isBefore(LocalDate.now())) {
	            // 해당 날짜의 기록이 없고 날짜가 지났다면 결근 처리
	            return "결근";
	        }
	        return "미출근";
	    }
	    
	 // 근무 시간 계산 메서드
	    private String calculateWorkingHours(LocalTime startTime, LocalTime endTime) {
	        // 출근 시간과 퇴근 시간이 모두 존재할 때만 계산
	        if (startTime != null && endTime != null) {
	            Duration duration = Duration.between(startTime, endTime);
	            long hours = duration.toHours();
	            long minutes = duration.toMinutesPart();  // Java 9 이상에서 사용 가능
	
	            // 결과를 "HH:mm" 형식으로 반환
	            return String.format("%02d:%02d", hours, minutes);
	        } else {
	            // 퇴근 시간이 없는 경우, 근무 시간을 "-"로 표시
	            return "-";
	        }
	    }
	    
	    public Page<AttendanceDto> getAllAttendances(Long empId, Pageable pageable) {
	        Page<Attendance> attendances = attendanceRepository.findByEmpId(empId, pageable);
	        return attendances.map(AttendanceDto::fromEntity);
	    }
	
	    // 특정 날짜의 출퇴근 기록을 조회하는 메서드
	    public Page<AttendanceDto> getAttendancesByDate(LocalDate date, Pageable pageable) {
	        Long empId = getCurrentUserId();
	
	        // 날짜 기준으로 조회
	        Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDate(empId, date, pageable);
	        return attendances.map(AttendanceDto::fromEntity);
	    }
	
	    public Page<AttendanceDto> getAttendancesByDateRange(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
	        Page<Attendance> attendances = attendanceRepository.findByEmpIdAndWorkDateBetween(empId, startDate, endDate, pageable);
	        return attendances.map(AttendanceDto::fromEntity);
	    }
	    
	    // 현재 월의 시작일과 종료일을 계산하는 메서드
	    private LocalDate getStartOfCurrentMonth() {
	        YearMonth currentMonth = YearMonth.now();
	        return currentMonth.atDay(1);
	    }
	
	    private LocalDate getEndOfCurrentMonth() {
	        YearMonth currentMonth = YearMonth.now();
	        return currentMonth.atEndOfMonth();
	    }
	
	
	    
	    
	    // 정상 출근 횟수 조회
	public long getNormalAttendanceCount(Long empId) {
	    LocalDate startDate = getStartOfCurrentMonth();
	    LocalDate endDate = getEndOfCurrentMonth();
	    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "정상출근", startDate, endDate);
	}
	
	    // 지각 횟수 조회
	public long getLateAttendanceCount(Long empId) {
	    LocalDate startDate = getStartOfCurrentMonth();
	    LocalDate endDate = getEndOfCurrentMonth();
	    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "지각", startDate, endDate);
	}
	
	
	    // 연차 횟수 조회
	public long getAnnualLeaveCount(Long empId) {
	    LocalDate startDate = getStartOfCurrentMonth();
	    LocalDate endDate = getEndOfCurrentMonth();
	    return attendanceRepository.countByEmpIdAndAttendStatusAndWorkDateBetween(empId, "휴가", startDate, endDate);
	}
	//    결근한 횟수 가져오기 (출근 기록이 없거나 결근으로 표시된 경우)
	//    public Long getAbsentCount(Long empId) {
	//        return attendanceRepository.countByEmpIdAndAttendStatus(empId, Attendance.ABSENT);
	//    }
	    // 연차 처리 메서드
	  
	
	// 결근 상태 자동 처리 (출근 기록이 없는 경우)
	@Transactional
	public void markAbsent(Long empId, LocalDate date) {
	    if (!attendanceRepository.existsByEmpIdAndWorkDate(empId, date)) {
	        Attendance attendance = Attendance.builder()
	                .empId(empId)
	                .workDate(date)
	                .attendStatus("결근") // 혹은 Attendance.ABSENT 상수를 사용
	                .build();
	        attendanceRepository.save(attendance);
	        logger.info("Marked employee ID {} as absent for date {}", empId, date);
	    }
	}
	@Scheduled(cron = "0 0 18 * * ?") // 매일 18:00에 실행
	public void markAbsentsAtSixPM() {
	    LocalDate today = LocalDate.now();
	    logger.info("AttendanceScheduler started for date: {}", today);
	
	    try {
	        List<Long> allEmployeeIds = employeeRepository.findAllEmployeeIds();
	        logger.info("Total employees to check: {}", allEmployeeIds.size());
	
	        for (Long empId : allEmployeeIds) {
	            try {
	                markAbsent(empId, today);
	            } catch (Exception e) {
	                logger.error("Error marking absent for employee ID {}: {}", empId, e.getMessage());
	            }
	        }
	
	        logger.info("AttendanceScheduler completed for date: {}", today);
	    } catch (Exception e) {
	        logger.error("Error in AttendanceScheduler: {}", e.getMessage());
	    }
	}
	
	// 연차 신청 메서드
	@Transactional
	public void applyAnnualLeave(Long empId, LocalDate date) {
	    // 기존 출근 기록이 있는지 확인
	    Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, date);
	    if (attendanceOpt.isPresent()) {
	        Attendance attendance = attendanceOpt.get();
	        attendance.setAttendStatus("휴가");
	    } else {
	        Attendance attendance = Attendance.builder()
	            .empId(empId)
	            .workDate(date)
	            .attendStatus("휴가")
	            .build();
	        attendanceRepository.save(attendance);
	    }

	   
	}
	
	// 반차일경우
	@Transactional
	public void applyHalfLeave(Long empId, LocalDate date, boolean isMorningHalf, LocalTime defaultEndTime) {
	    Optional<Attendance> attendanceOpt = attendanceRepository.findByEmpIdAndWorkDate(empId, date);
	    
	    Attendance attendance;
	    
	    if (attendanceOpt.isPresent()) {
	        attendance = attendanceOpt.get();
	    } else {
	        // 새로운 Attendance 객체 생성
	        attendance = Attendance.builder()
	            .empId(empId)
	            .workDate(date)
	            .build(); // 초기 상태로만 생성
	    }
	    
	    if (isMorningHalf) {
	        // 오전 반차 처리
	        attendance.setAttendStatus("반차");
	        attendance.setAttendStartTime(LocalTime.of(14, 0)); // 오후 출근 시간 설정
	        attendance.setAttendEndTime(null); // 퇴근 시간 필요 없음
	    } else {
	        // 오후 반차 처리
	        attendance.setAttendStatus("반차");
	        attendance.setAttendStartTime(null); // 출근 시간 필요 없음
	        attendance.setAttendEndTime(LocalTime.of(13, 0)); // 종료 시간 설정
	    }
	    
	    attendanceRepository.save(attendance); 
	}


}
	
	
	
