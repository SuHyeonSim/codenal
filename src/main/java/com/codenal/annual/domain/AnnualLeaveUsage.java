package com.codenal.annual.domain;

import java.time.LocalDate;

import com.codenal.approval.domain.Approval;
import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Builder
@Setter
@Table(name="annual_leave_usage")

public class AnnualLeaveUsage {
   
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Id
   @Column(name="annual_usage_no")
   private Long annualUsageNo;	
   
   @Column(name="annual_usage_start_date")
   private LocalDate annualUsageStartDate;
   
   @Column(name="annual_usage_end_date")
   private LocalDate annualUsageEndDate;	
   
   @Column(name="total_day")
   private Double totalDay;
   
   @ManyToOne
    @JoinColumn(name="emp_id")
    private Employee employee;
   
   // 반차인지 연차인지 나타내는 컬럼
   @Column(name="annual_type")
   private int annualType;
   
   // 반차였을 때 오전인지 오후인지 설정하는 컬럼
   @Column(name="time_period")
   private String timePeriod;
   
   @OneToOne(mappedBy="annualLeaveUsage")
   private Approval approval;
   
   @Builder.Default
   @Column(name = "processed", nullable = false)
   private boolean processed = false;
   
}