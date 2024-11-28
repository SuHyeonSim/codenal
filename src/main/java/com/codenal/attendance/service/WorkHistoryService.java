package com.codenal.attendance.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.attendance.domain.WorkHistory;
import com.codenal.attendance.domain.WorkHistoryDto;
import com.codenal.attendance.repository.WorkHistoryRepository;
import com.codenal.employee.domain.Employee;

@Service
public class WorkHistoryService {

    private final WorkHistoryRepository workHistoryRepository;

    @Autowired
    public WorkHistoryService(WorkHistoryRepository workHistoryRepository) {
        this.workHistoryRepository = workHistoryRepository;
    }

    /**
     * 현재 사용자의 모든 근무 내역을 페이징 조회
     * @param empId 사용자 사번
     * @param pageable 페이징 정보
     * @return 근무 내역 페이지
     */
    public Page<WorkHistoryDto> getHistories(Long empId, Pageable pageable) {
        Page<WorkHistory> workHistoryPage = workHistoryRepository.findByEmployee_EmpId(empId, pageable);
        return workHistoryPage.map(WorkHistoryDto::fromEntity);
    }

    /**
     * 특정 날짜 범위의 근무 내역을 페이징 조회
     * @param empId 사용자 사번
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param pageable 페이징 정보
     * @return 근무 내역 페이지
     */
    public Page<WorkHistoryDto> getHistoriesByRange(Long empId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<WorkHistory> workHistoryPage = workHistoryRepository.findByEmployee_EmpIdAndWorkHistoryDateBetween(empId, startDate, endDate, pageable);
        return workHistoryPage.map(WorkHistoryDto::fromEntity);
    }
    @Transactional
    public void createOrUpdateWorkHistory(Long empId, WorkHistoryDto workHistoryDto) {
        LocalDate workDate = workHistoryDto.getWorkHistoryDate();
        BigDecimal workingTime = workHistoryDto.getWorkHistoryWorkingTime();
        BigDecimal overTime = workHistoryDto.getWorkHistoryOverTime();
        BigDecimal totalTime = workHistoryDto.getWorkHistoryTotalTime();

        // 이미 해당 날짜의 WorkHistory가 있는지 확인
        WorkHistory existingWorkHistory = workHistoryRepository.findByEmployee_EmpIdAndWorkHistoryDate(empId, workDate)
                .orElse(null);

        if (existingWorkHistory != null) {
            // 기존 WorkHistory 업데이트
            existingWorkHistory.setWorkHistoryWorkingTime(workingTime);
            existingWorkHistory.setWorkHistoryOverTime(overTime);
            existingWorkHistory.setWorkHistoryTotalTime(totalTime);
            workHistoryRepository.save(existingWorkHistory);
        } else {
            // 새로운 WorkHistory 생성
            WorkHistory workHistory = WorkHistory.builder()
                    .workHistoryDate(workDate)
                    .workHistoryWorkingTime(workingTime)
                    .workHistoryOverTime(overTime)
                    .workHistoryTotalTime(totalTime)
                    .employee(Employee.builder().empId(empId).build()) // Employee 엔티티 설정 (ID만 설정)
                    .build();
            workHistoryRepository.save(workHistory);
        }
    }
    
  
    
}
