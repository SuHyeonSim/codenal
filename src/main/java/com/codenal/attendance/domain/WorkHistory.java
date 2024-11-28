package com.codenal.attendance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WorkHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_history_no")
    private Long workHistoryNo;
    
    @Column(name = "work_history_date")
    private LocalDate workHistoryDate;
    
    @Column(name = "work_history_total_time")
    private BigDecimal workHistoryTotalTime;
    
    @Column(name = "work_history_over_time")
    private BigDecimal workHistoryOverTime;
    
    @Column(name = "work_history_working_time")
    private BigDecimal workHistoryWorkingTime;
    
    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id", nullable = false)
    private Employee employee; // Employee 객체를 사용한 관계 설정
}
