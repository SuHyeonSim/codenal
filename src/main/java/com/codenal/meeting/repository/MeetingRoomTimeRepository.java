package com.codenal.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.meeting.domain.MeetingRoomTime;

public interface MeetingRoomTimeRepository extends JpaRepository<MeetingRoomTime, Long>{
	
	MeetingRoomTime findByMeetingRoomTimeNo(Long meetingRoomTimeNo);

	@Transactional
	@Modifying
	@Query("delete from MeetingRoomTime t where t.meetingRoomNo = ?1")
	int deleteByMeetingRoomNo(int roomNo);

}
