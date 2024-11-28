package com.codenal.meeting.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.employee.domain.Employee;
import com.codenal.meeting.domain.MeetingRoomReserve;

public interface MeetingRoomReserveRepository extends JpaRepository<MeetingRoomReserve, Long>{
	
	 
	@Query(value = "select r from MeetingRoomReserve r where r.empId = ?1")
	List<MeetingRoomReserve> findByEmpId(Long empId);

	MeetingRoomReserve findByMeetingRoomReserveNo(Long meeting_room_no);

	List<MeetingRoomReserve> findByMeetingRoomReserveDate(LocalDate date);
}
