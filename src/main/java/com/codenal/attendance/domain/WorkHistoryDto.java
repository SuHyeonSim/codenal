package com.codenal.attendance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WorkHistoryDto {

    private Long workHistoryNo;
    private LocalDate workHistoryDate;
    private BigDecimal workHistoryWorkingTime; // 근무시간
    private BigDecimal workHistoryOverTime; // 초과근무시간
    private BigDecimal workHistoryTotalTime; // 총 근무시간
    private Long empId;

    // DTO를 엔티티로 변환하는 메서드 (Employee는 별도로 조회 필요)
    public WorkHistory toEntity(Employee employee) {
        return WorkHistory.builder()
                .workHistoryDate(this.workHistoryDate)
                .workHistoryWorkingTime(this.workHistoryWorkingTime)
                .workHistoryOverTime(this.workHistoryOverTime)
                .workHistoryTotalTime(this.workHistoryTotalTime)
                .employee(employee)
                .build();
    }

    // 엔티티를 DTO로 변환하는 메서드
    public static WorkHistoryDto fromEntity(WorkHistory workHistory) {
        return WorkHistoryDto.builder()
                .workHistoryNo(workHistory.getWorkHistoryNo())
                .workHistoryDate(workHistory.getWorkHistoryDate())
                .workHistoryWorkingTime(workHistory.getWorkHistoryWorkingTime())
                .workHistoryOverTime(workHistory.getWorkHistoryOverTime())
                .workHistoryTotalTime(workHistory.getWorkHistoryTotalTime())
                .empId(workHistory.getEmployee().getEmpId())
                .build();
    }
 // 근무시간을 "HH:mm" 형식으로 반환하는 메서드
    public String getFormattedWorkingTime() {
        return formatBigDecimalTime(workHistoryWorkingTime);
    }

    // 초과근무시간을 "HH:mm" 형식으로 반환하는 메서드
    public String getFormattedOverTime() {
        return formatBigDecimalTime(workHistoryOverTime);
    }

    // 총 근무시간을 "HH:mm" 형식으로 반환하는 메서드
    public String getFormattedTotalTime() {
        return formatBigDecimalTime(workHistoryTotalTime);
    }

    /**
     * BigDecimal 형식의 시간을 "HH:mm" 형식으로 변환하는 유틸리티 메서드
     * @param time 근무 시간 (BigDecimal)
     * @return "HH:mm" 형식의 문자열
     */
    private String formatBigDecimalTime(BigDecimal time) {
        if (time == null) return "-";
        int hours = time.intValue();
        int minutes = time.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(60)).intValue();
        return String.format("%02d:%02d", hours, minutes);
    }
}
