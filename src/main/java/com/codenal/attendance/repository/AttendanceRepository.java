package com.codenal.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.attendance.domain.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // 로그인한 사용자의 모든 출퇴근 기록 조회
    Page<Attendance> findByEmpId(Long empId, Pageable pageable);

    // 로그인한 사용자의 특정 날짜의 출근 기록 존재 여부 확인
    boolean existsByEmpIdAndWorkDate(Long empId, LocalDate workDate);

    // 로그인한 사용자의 특정 날짜의 출퇴근 기록 조회
    Optional<Attendance> findByEmpIdAndWorkDate(Long empId, LocalDate workDate);

    // 로그인한 사용자의 특정 날짜의 출퇴근 기록 조회 (페이징)
    Page<Attendance> findByEmpIdAndWorkDate(Long empId, LocalDate workDate, Pageable pageable);

    // 로그인한 사용자의 특정 날짜 범위의 출퇴근 기록 조회
    Page<Attendance> findByEmpIdAndWorkDateBetween(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    long countByEmpIdAndAttendStatus(Long empId, String status);
    
    long countByEmpIdAndAttendStatusAndWorkDateBetween(Long empId, String attendStatus, LocalDate startDate, LocalDate endDate);
    
    
}
