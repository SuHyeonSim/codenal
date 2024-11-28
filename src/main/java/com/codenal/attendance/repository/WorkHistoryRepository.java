package com.codenal.attendance.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.attendance.domain.WorkHistory;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Long> {

    // 페이징을 지원하는 메서드
    Page<WorkHistory> findByEmployee_EmpId(Long empId, Pageable pageable);

    Page<WorkHistory> findByEmployee_EmpIdAndWorkHistoryDateBetween(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Optional<WorkHistory> findByEmployee_EmpIdAndWorkHistoryDate(Long empId, LocalDate workHistoryDate);

}
