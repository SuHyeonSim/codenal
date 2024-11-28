package com.codenal.meeting.domain;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
public class MeetingRoomTimeDto {
	
	private Long meeting_room_time_no;
	private Long meeting_room_no;
	private LocalTime meeting_room_start_time;
	private LocalTime meeting_room_end_time;
	private String meeting_room_ampm;
	
	public MeetingRoomTime toEntity() {
		return MeetingRoomTime.builder()
				.meetingRoomTimeNo(meeting_room_time_no)
				.meetingRoomNo(meeting_room_no)
				.meetingRoomStartTime(meeting_room_start_time)
				.meetingRoomEndTime(meeting_room_end_time)
				.meetingRoomAmpm(meeting_room_ampm)
				.build();
	}
	
	public MeetingRoomTimeDto toDto(MeetingRoomTime time) {
		return MeetingRoomTimeDto.builder()
				.meeting_room_time_no(time.getMeetingRoomTimeNo())
				.meeting_room_no(time.getMeetingRoomNo())
				.meeting_room_start_time(time.getMeetingRoomStartTime())
				.meeting_room_end_time(time.getMeetingRoomEndTime())
				.meeting_room_ampm(time.getMeetingRoomAmpm())
				.build();
	}

}
