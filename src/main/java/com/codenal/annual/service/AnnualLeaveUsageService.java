package com.codenal.annual.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.repository.ApprovalCategoryRepository;
import com.codenal.employee.repository.EmployeeRepository;

@Service
public class AnnualLeaveUsageService {
    private final EmployeeRepository employeeRepository;
    private final ApprovalCategoryRepository approvalCategoryRepository;
    private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
    private final AnnualLeaveManageService annualLeaveManageService; 

    @Autowired
    public AnnualLeaveUsageService(
            EmployeeRepository employeeRepository,
            ApprovalCategoryRepository approvalCategoryRepository,
            AnnualLeaveUsageRepository annualLeaveUsageRepository, AnnualLeaveManageService annualLeaveManageService) {
        this.employeeRepository = employeeRepository;
        this.approvalCategoryRepository = approvalCategoryRepository;
        this.annualLeaveUsageRepository = annualLeaveUsageRepository;
        this.annualLeaveManageService = annualLeaveManageService; 
    }
    
   
    public Page<AnnualLeaveUsageDto> getAnnualLeaveUsage(Long empId, LocalDate startDate, LocalDate endDate, Integer year, Integer month, Pageable pageable) {
        if (startDate != null && endDate != null) {
            return annualLeaveUsageRepository
                .findByEmployee_EmpIdAndAnnualUsageStartDateBetween(empId, startDate, endDate, pageable)
                .map(AnnualLeaveUsageDto::toDto);
        } else if (year != null && month != null) {
            LocalDate monthStart = YearMonth.of(year, month).atDay(1);
            LocalDate monthEnd = YearMonth.of(year, month).atEndOfMonth();
            return annualLeaveUsageRepository
                .findByEmployee_EmpIdAndAnnualUsageStartDateBetween(empId, monthStart, monthEnd, pageable)
                .map(AnnualLeaveUsageDto::toDto);
        } else {
            // 기본적으로 현재 연도와 월을 사용하여 데이터를 조회하도록 설정
            YearMonth currentYearMonth = YearMonth.now();
            LocalDate currentStart = currentYearMonth.atDay(1);
            LocalDate currentEnd = currentYearMonth.atEndOfMonth();
            return annualLeaveUsageRepository
                .findByEmployee_EmpIdAndAnnualUsageStartDateBetween(empId, currentStart, currentEnd, pageable)
                .map(AnnualLeaveUsageDto::toDto);
        }
    }


    // 연차 사용 내역 생성 및 연차 관리 업데이트 메서드
    @Transactional
    public void useAnnualLeave(Long empId, LocalDate startDate, LocalDate endDate, int annualType, String timePeriod) {
        Double daysToUse = calculateDaysToUse(startDate, endDate, annualType, timePeriod);  // 연차 사용 일수 계산

        // 연차 관리 정보를 가져옴
        AnnualLeaveManage manage = annualLeaveManageService.getAnnualLeaveManageById(empId);
        if (manage == null) {
            throw new IllegalStateException("해당 직원의 연차 관리 정보가 없습니다.");
        }

        // 잔여 연차가 충분한지 확인 후 처리
        if (manage.getAnnualRemainDay() >= daysToUse) {
            // 연차 사용 내역 생성 및 저장
            AnnualLeaveUsage usage = AnnualLeaveUsage.builder()
                .employee(employeeRepository.findById(empId).orElseThrow(() -> new IllegalArgumentException("Invalid empId")))
                .annualUsageStartDate(startDate)
                .annualUsageEndDate(endDate)
                .annualType(annualType)
                .timePeriod(timePeriod)
                .totalDay(daysToUse)
                .build();
            annualLeaveUsageRepository.save(usage);  // 연차 내역 저장

            // 잔여 연차 정보 업데이트
            manage.setAnnualUsedDay(manage.getAnnualUsedDay() + daysToUse);  // 사용 연차 추가
            manage.setAnnualRemainDay(manage.getAnnualRemainDay() - daysToUse);  // 남은 연차 감소
            annualLeaveManageService.save(manage);  // 연차 관리 정보 저장
        } else {
            throw new IllegalStateException("잔여 연차가 부족합니다.");
        }
    }
    
    
    private Double calculateDaysToUse(LocalDate startDate, LocalDate endDate, int annualType, String timePeriod) {
        if (annualType == 1) {
            // 반차의 경우 0.5일로 설정
            return 0.5;
        } else if (startDate.isEqual(endDate)) {
            // 시작일과 종료일이 동일한 경우 1일만 계산
            return 1.0;
        } else {
            // 시작일과 종료일이 다른 경우 일수 계산
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
            return (double) daysBetween;
        }
   
    }
}
