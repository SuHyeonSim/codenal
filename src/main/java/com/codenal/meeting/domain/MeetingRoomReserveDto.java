package com.codenal.meeting.domain;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class MeetingRoomReserveDto {
	
	private Long meeting_room_reserve_no;
	private Long meeting_room_no;
	private Long emp_id;
	private LocalDate meeting_room_reserve_date;
	private LocalTime meeting_room_start_time;
	private LocalTime meeting_room_end_time;
	private Long meeting_room_reserve_time_no;
	
	private String meeting_room_name;
	private String meeting_room_place;
	private String meeting_room_amenity;
	private String meeting_room_img;
	
	public MeetingRoomReserve toEntity() {
		return MeetingRoomReserve.builder()
				.meetingRoomReserveNo(meeting_room_reserve_no)
				.meetingRoom(MeetingRoom.builder().meetingRoomNo(meeting_room_no).build())
				.empId(emp_id)
				.meetingRoomReserveDate(meeting_room_reserve_date)
				.meetingRoomStartTime(meeting_room_start_time)
				.meetingRoomEndTime(meeting_room_end_time)
				.meetingRoomReserveTimeNo(meeting_room_reserve_time_no)
				.build();
	}
	
	public MeetingRoomReserveDto toDto(MeetingRoomReserve reserve) {
		return MeetingRoomReserveDto.builder()
				.meeting_room_reserve_no(reserve.getMeetingRoomReserveNo())
				.meeting_room_no(reserve.getMeetingRoom().getMeetingRoomNo())
				.emp_id(reserve.getEmpId())
				.meeting_room_reserve_date(reserve.getMeetingRoomReserveDate())
				.meeting_room_start_time(reserve.getMeetingRoomStartTime())
				.meeting_room_end_time(reserve.getMeetingRoomEndTime())
				.meeting_room_reserve_time_no(reserve.getMeetingRoomReserveTimeNo())
				.meeting_room_name(reserve.getMeetingRoom().getMeetingRoomName())
				.meeting_room_place(reserve.getMeetingRoom().getMeetingRoomPlace())
				.meeting_room_amenity(reserve.getMeetingRoom().getMeetingRoomAmenity())
				.meeting_room_img(reserve.getMeetingRoom().getMeetingRoomImg())
				.build();
	}

}
