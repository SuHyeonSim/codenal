package com.codenal.meeting.domain;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meetingRoomTime")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MeetingRoomTime {
	
	@Id
	@Column(name = "meeting_room_time_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingRoomTimeNo;
	
	@Column(name = "meeting_room_no")
	private Long meetingRoomNo;
	
	@JsonFormat(pattern = "hh-mm")
	@Column(name = "meeting_room_start_time")
	private LocalTime meetingRoomStartTime;
	
	@JsonFormat(pattern = "hh-mm")
	@Column(name = "meeting_room_end_time")
	private LocalTime meetingRoomEndTime;
	
	@Column(name = "meeting_room_ampm")
	private String meetingRoomAmpm;

}
