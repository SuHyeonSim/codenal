package com.codenal.alarms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.repository.MeetingRoomReserveRepository;

@Service
public class MeetingRoomAlarmsService {
    
    @Autowired
    private MeetingRoomReserveRepository meetingRoomReserveRepository;

    @Autowired
    private AlarmsService alarmsService;

    // 회의실 예약 생성
    @Transactional
    public void reserveMeetingRoom(MeetingRoomReserve reserve) {
        // 회의실 예약 저장
        meetingRoomReserveRepository.save(reserve);

        // 예약 성공 후 알림 생성
        alarmsService.createMeetingRoomReservationAlarm(reserve);
    }

}

