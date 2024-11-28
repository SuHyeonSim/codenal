package com.codenal.alarms.domain;

import java.time.LocalDateTime;

import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AlarmsDto {

    private Long alarmNo;            // 알림 번호
    private Long empId;              // 사원 ID
    private String alarmTitle;       // 알림 제목
    private String alarmContext;     // 알림 내용
    private String alarmType;        // 알림 유형
    private Long alarmReferenceNo;   // 알림 참조 번호
    private LocalDateTime alarmCreateTime; // 알림 생성 시간
    private LocalDateTime alarmReadTime;   // 알림 확인 시간
    private String alarmIsRead;     // 알림 확인 여부
    private String alarmIsDeleted;  // 알림 삭제 여부
    private String alarmUrl;        // 관련 알림 URL

    // 회의실 정보 추가
    private String roomName;        // 회의실 이름
    private String roomLocation;    // 회의실 장소
    private String alarmTime;       // 예약 시간

   
    public static AlarmsDto fromEntity(Alarms alarms) {
        if (alarms == null) {
            return null;
        }

        return AlarmsDto.builder()
                .alarmNo(alarms.getAlarmNo())
                .empId(alarms.getEmployee() != null ? alarms.getEmployee().getEmpId() : null)
                .alarmTitle(alarms.getAlarmTitle())
                .alarmContext(alarms.getAlarmContext())
                .alarmType(alarms.getAlarmType())
                .alarmReferenceNo(alarms.getAlarmReferenceNo())
                .alarmCreateTime(alarms.getAlarmCreateTime())
                .alarmReadTime(alarms.getAlarmReadTime())
                .alarmIsRead(alarms.getAlarmIsRead())
                .alarmIsDeleted(alarms.getAlarmIsDeleted())
                .alarmUrl(alarms.getAlarmUrl())
                .build();
    }

   
    public static AlarmsDto fromEntity(Alarms alarms, String roomName, String roomLocation, String alarmTime) {
        AlarmsDto dto = fromEntity(alarms);
        dto.setRoomName(roomName);
        dto.setRoomLocation(roomLocation);
        dto.setAlarmTime(alarmTime);
        return dto;
    }

    public Alarms toEntity(Employee employee) {
        return Alarms.builder()
                .alarmNo(this.alarmNo)
                .alarmTitle(this.alarmTitle)
                .alarmContext(this.alarmContext)
                .alarmType(this.alarmType)
                .alarmReferenceNo(this.alarmReferenceNo)
                .alarmCreateTime(this.alarmCreateTime != null ? this.alarmCreateTime : LocalDateTime.now())
                .alarmReadTime(this.alarmReadTime)
                .alarmIsRead(this.alarmIsRead != null ? this.alarmIsRead : "N")
                .alarmIsDeleted(this.alarmIsDeleted != null ? this.alarmIsDeleted : "N")
                .alarmUrl(this.alarmUrl)
                .build();
    }
}
