package com.codenal.annual.domain;

import java.time.LocalDate;

import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AnnualLeaveUsageDto {

    private Long annual_usage_no;
    private LocalDate annual_usage_start_date;
    private LocalDate annual_usage_end_date;
    private Long emp_id;
    private int annual_type;
    private Double total_day;
    private String time_period;

    // 추가 필드
    private String empName;
   

    public static AnnualLeaveUsageDto toDto(AnnualLeaveUsage alu) {
        return AnnualLeaveUsageDto.builder()
                .annual_usage_no(alu.getAnnualUsageNo())
                .annual_usage_start_date(alu.getAnnualUsageStartDate())
                .annual_usage_end_date(alu.getAnnualUsageEndDate())
                .emp_id(alu.getEmployee().getEmpId())
                .annual_type(alu.getAnnualType())
                .time_period(alu.getTimePeriod())
                .total_day(alu.getTotalDay())
                .empName(alu.getEmployee().getEmpName())
                .build();
    }

    public AnnualLeaveUsage toEntity() {
        return AnnualLeaveUsage.builder()
                .annualUsageNo(annual_usage_no)
                .annualUsageStartDate(annual_usage_start_date)
                .annualUsageEndDate(annual_usage_end_date)
                .annualType(annual_type)
                .timePeriod(time_period)
                .totalDay(total_day)
                .employee(Employee.builder().empId(emp_id).build())
                .build();
    }
    public Double getTotalDay() {
        return total_day;
    }

    public void setTotalDay(Double totalDay) {
        this.total_day = totalDay;
    }
}
