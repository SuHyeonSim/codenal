
package com.codenal.attendance.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class AttendanceDto {

	private Long attendNo;
	private LocalTime attendStartTime;
	private LocalTime attendEndTime;
	private LocalDate workDate;
	private BigDecimal attendWorkingTime;
	private String attendStatus;
	private Long empId;

	// 엔티티 -> DTO 변환 메서드
	public static AttendanceDto fromEntity(Attendance attendance) {
		return AttendanceDto.builder()
				.attendNo(attendance.getAttendNo())
				.attendStartTime(attendance.getAttendStartTime())
				.attendEndTime(attendance.getAttendEndTime())
				.workDate(attendance.getWorkDate())
				.attendWorkingTime(attendance.getAttendWorkingTime())
				.attendStatus(attendance.getAttendStatus())
				.empId(attendance.getEmpId())
				.build();
	}

	// DTO -> 엔티티 변환 메서드
	public static Attendance toEntity(AttendanceDto dto) {
		return Attendance.builder()
				.attendNo(dto.getAttendNo())
				.attendStartTime(dto.getAttendStartTime())
				.attendEndTime(dto.getAttendEndTime())
				.workDate(dto.getWorkDate())
				.attendWorkingTime(dto.getAttendWorkingTime())
				.attendStatus(dto.getAttendStatus())
				.empId(dto.getEmpId())
				.build();
	}
	

	// 근무시간을 "HH:mm" 형식으로 반환하는 메서드
	 
	public String getFormattedWorkingTime() {
	    if (attendWorkingTime != null) {
	        long hours = attendWorkingTime.longValue();
	        long minutes = attendWorkingTime.subtract(BigDecimal.valueOf(hours))
	                            .multiply(BigDecimal.valueOf(60))
	                            .longValue();
	        return String.format("%02d:%02d", hours, minutes);
	    }
	    return "-";  // 근무시간이 없을 때
	}
}