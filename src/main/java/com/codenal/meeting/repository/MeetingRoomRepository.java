package com.codenal.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomReserve;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long>{

	@Query(value = "select r from MeetingRoom r ORDER BY meetingRoomNo DESC LIMIT 1")
	MeetingRoom findBy();

	MeetingRoom findByMeetingRoomNo(Long no);

}
