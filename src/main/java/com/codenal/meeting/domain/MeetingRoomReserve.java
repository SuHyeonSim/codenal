package com.codenal.meeting.domain;

import java.time.LocalDate;
import java.time.LocalTime;

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

@Entity
@Table(name = "meetingRoomReserve")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MeetingRoomReserve {

	@Id
	@Column(name = "meeting_room_reserve_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long meetingRoomReserveNo;

	//	@Column(name = "meeting_room_no")
	//	private Long meetingRoomNo;

	@Column(name = "emp_id")
	private Long empId;

	@Column(name = "meeting_room_reserve_date")
	private LocalDate meetingRoomReserveDate;

	@Column(name = "meeting_room_start_time")
	private LocalTime meetingRoomStartTime;

	@Column(name = "meeting_room_end_time")
	private LocalTime meetingRoomEndTime;

	@Column(name = "meeting_room_reserve_time_no")
	private Long meetingRoomReserveTimeNo;


	@ManyToOne
	@JoinColumn(name = "meeting_room_no", referencedColumnName = "meeting_room_no")
	private MeetingRoom meetingRoom;

	@ManyToOne
	@JoinColumn(name = "emp_id", referencedColumnName = "emp_id", insertable = false, updatable = false)
	private Employee employee; 

}