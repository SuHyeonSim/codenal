package com.codenal.alarms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.alarms.domain.AlarmType;
import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.domain.AlarmsDto;
import com.codenal.alarms.repository.AlarmsRepository;
import com.codenal.meeting.domain.MeetingRoomReserve;

@Service
public class AlarmsService {

	private final AlarmsRepository alarmsRepository;

	@Autowired
	public AlarmsService(AlarmsRepository alarmsRepository) {
		this.alarmsRepository = alarmsRepository;
	}

	// 알림 아이콘 누르면 읽음 'Y'
	@Transactional
	public void markAsRead(Long alarmNo) {
		Alarms alarm = alarmsRepository.findById(alarmNo)
				.orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다."));

		// 알림을 읽음으로 처리
		alarm.setAlarmIsRead("Y");
		alarm.setAlarmReadTime(LocalDateTime.now());

		alarmsRepository.save(alarm);
	}

	// 특정 직원의 알림 목록 가져오기
	public List<AlarmsDto> getAlarmsByEmp(Long empId) {
		return alarmsRepository.findByEmployee_EmpId(empId).stream()
				.map(AlarmsDto::fromEntity)
				.collect(Collectors.toList());
	}

	// 알림 삭제
	@Transactional
	public void deleteAlarm(Long alarmNo) {
		alarmsRepository.deleteById(alarmNo);
	}

	// 회의실 예약 알림
	@Transactional
	public void createMeetingRoomReservationAlarm(MeetingRoomReserve reserve) {
		// 알림 제목과 내용 생성
		String title = reserve.getMeetingRoom().getMeetingRoomName() + " 회의실 예약 알림";
		String context = String.format("회의실 '%s'에서 %s에 예약이 있습니다.",
				reserve.getMeetingRoom().getMeetingRoomName(),
				reserve.getMeetingRoomReserveDate().toString());

		// 알림 생성
		Alarms alarm = Alarms.builder()
				.employee(reserve.getEmployee()) // 예약자에게 알림
				.alarmTitle(title)
				.alarmContext(context)
				.alarmType(AlarmType.MEETING) // 회의실 알림 타입
				.alarmReferenceNo(reserve.getMeetingRoomReserveNo()) // 예약 번호 참조
				.alarmCreateTime(LocalDateTime.now()) // 알림 생성 시간
				.alarmIsRead("N") // 알림 읽음 여부 (N)
				.alarmIsDeleted("N") // 알림 삭제 여부 (N)
				.alarmUrl("/meetingRoom/" + reserve.getMeetingRoomReserveNo()) // 예약 상세 페이지로 이동하는 URL
				.build();

		// 알림 저장
		alarmsRepository.save(alarm);
	}


	// 예약된 날짜의 오전 9시에 알림 전송
	@Scheduled(cron = "0 */1 * * * *")
	// @Scheduled(cron = "0 0 9 * * ?") // 매일 오전 9시에 실행
	public void sendMeetingRoomReservationAlarms() {
		LocalDate today = LocalDate.now();
		LocalDateTime startOfDay = today.atStartOfDay(); // 자정
		LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 하루 끝

		List<Alarms> alarmsForToday = alarmsRepository.findAlarmsByDate(startOfDay, endOfDay);

		for (Alarms alarm : alarmsForToday) {
			System.out.println("Sending alarm: " + alarm.getAlarmTitle());
		}
	}

	// 날짜가 지난 알림을 삭제
	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
	public void deleteExpiredAlarms() {
		LocalDateTime now = LocalDateTime.now();
		List<Alarms> expiredAlarms = alarmsRepository.findAlarmsBeforeDate(now);

		for (Alarms alarm : expiredAlarms) {
			alarmsRepository.delete(alarm);
			System.out.println("Deleted expired alarm: " + alarm.getAlarmTitle());
		}
	}
	
	 // 전자결재 알림을 생성하고 저장
    @Transactional
    public Alarms createAlarm(Alarms alarm) {
        return alarmsRepository.save(alarm);
    }

}
