package com.codenal.meeting.service;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomReserve;
import com.codenal.meeting.domain.MeetingRoomReserveDto;
import com.codenal.meeting.domain.MeetingRoomTime;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.repository.MeetingRoomRepository;
import com.codenal.meeting.repository.MeetingRoomReserveRepository;
import com.codenal.meeting.repository.MeetingRoomTimeRepository;

@Service
public class MeetingRoomService {
	
	private MeetingRoomRepository meetingRoomRepository;
	private MeetingRoomTimeRepository meetingRoomTimeRepository;
	private MeetingRoomReserveRepository meetingRoomReserveRepository;
	
	@Autowired
	public MeetingRoomService(MeetingRoomRepository meetingRoomRepository, MeetingRoomTimeRepository meetingRoomTimeRepository, MeetingRoomReserveRepository meetingRoomReserveRepository) {
		this.meetingRoomRepository = meetingRoomRepository;
		this.meetingRoomTimeRepository = meetingRoomTimeRepository;
		this.meetingRoomReserveRepository = meetingRoomReserveRepository;
	}
	
	// 예약 변경
	public MeetingRoomReserve meetingRoomReserveModify(MeetingRoomReserveDto dto) {
		
		MeetingRoomTime time = meetingRoomTimeRepository.findByMeetingRoomTimeNo(dto.getMeeting_room_reserve_time_no());
		
		MeetingRoomTimeDto timeToDto = new MeetingRoomTimeDto().toDto(time);
		
		MeetingRoomReserve reserve = meetingRoomReserveRepository.findByMeetingRoomReserveNo(dto.getMeeting_room_reserve_no());
		MeetingRoomReserveDto reserveDto = MeetingRoomReserveDto.builder()
				.meeting_room_reserve_no(reserve.getMeetingRoomReserveNo())
				.meeting_room_no(reserve.getMeetingRoom().getMeetingRoomNo())
				.emp_id(reserve.getEmpId())
				.meeting_room_reserve_date(reserve.getMeetingRoomReserveDate())
				.meeting_room_start_time(reserve.getMeetingRoomStartTime())
				.meeting_room_end_time(reserve.getMeetingRoomEndTime())
				.meeting_room_reserve_time_no(reserve.getMeetingRoomReserveTimeNo())
				.build();
		reserveDto.setMeeting_room_no(dto.getMeeting_room_no());
		reserveDto.setMeeting_room_reserve_date(dto.getMeeting_room_reserve_date());
		reserveDto.setMeeting_room_start_time(timeToDto.getMeeting_room_start_time());
		reserveDto.setMeeting_room_end_time(timeToDto.getMeeting_room_end_time());
		reserveDto.setMeeting_room_reserve_time_no(dto.getMeeting_room_reserve_time_no());
		
		MeetingRoomReserve modify = reserveDto.toEntity();
		MeetingRoomReserve result = meetingRoomReserveRepository.save(modify);
		return result;
	}
	
	// 예약 가능 시간 수정
	public void MeetingRoomTimeModify(LocalTime[][] time, String roomNo) {
		
		if(meetingRoomTimeRepository.deleteByMeetingRoomNo(Integer.parseInt(roomNo)) > 0 || meetingRoomTimeRepository.deleteByMeetingRoomNo(Integer.parseInt(roomNo)) == 0) {
			
			MeetingRoom meetingRoom = meetingRoomRepository.findByMeetingRoomNo(Long.parseLong(roomNo));
			MeetingRoomDto meetingRoomDto = new MeetingRoomDto().toDto(meetingRoom);
			String[] ampm = new String[time.length];
			LocalTime standard = LocalTime.of(12, 00);
			for (int i = 0; i < time.length; i++) {
				int compare = time[i][1].compareTo(standard);
				if (compare <= (-1)) {
					ampm[i] = "오전";
				} else {
					ampm[i] = "오후";
				}
			}
			for (int i = 0; i < time.length; i++) {
				MeetingRoomTime times = MeetingRoomTime.builder().meetingRoomNo(meetingRoomDto.getMeeting_room_no())
						.meetingRoomStartTime(time[i][0]).meetingRoomEndTime(time[i][1]).meetingRoomAmpm(ampm[i]).build();
				meetingRoomTimeRepository.save(times);
			}
		}


	}
	
	// 회의실 업데이트
	public MeetingRoom modifyMeetingRoom(MeetingRoomDto dto) {
		MeetingRoom room = meetingRoomRepository.findByMeetingRoomNo(dto.getMeeting_room_no());
		MeetingRoomDto roomdto = MeetingRoomDto.builder()
				.meeting_room_no(room.getMeetingRoomNo())
				.meeting_room_name(room.getMeetingRoomName())
				.meeting_room_place(room.getMeetingRoomPlace())
				.meeting_room_amenity(room.getMeetingRoomAmenity())
				.meeting_room_img(room.getMeetingRoomImg())
				.build();
		roomdto.setMeeting_room_name(dto.getMeeting_room_name());
		roomdto.setMeeting_room_place(dto.getMeeting_room_place());
		roomdto.setMeeting_room_amenity(dto.getMeeting_room_amenity());
		if(dto.getMeeting_room_img() != "") {
			roomdto.setMeeting_room_img(dto.getMeeting_room_img());
		} else {
			roomdto.setMeeting_room_img(room.getMeetingRoomImg());
		}
		MeetingRoom modify = roomdto.toEntity();
		MeetingRoom result = meetingRoomRepository.save(modify);
		return result;
	}
	
	// 회의실 하나 출력
	public MeetingRoomDto selectMeetingRoom(Long no) {
		MeetingRoom room = meetingRoomRepository.findByMeetingRoomNo(no);
		MeetingRoomDto dto = MeetingRoomDto.builder()
				.meeting_room_no(room.getMeetingRoomNo())
				.meeting_room_name(room.getMeetingRoomName())
				.meeting_room_place(room.getMeetingRoomPlace())
				.meeting_room_amenity(room.getMeetingRoomAmenity())
				.meeting_room_img(room.getMeetingRoomImg())
				.build();
		return dto;
	}
	
	// 예약 취소
	public int ReserveDelete(Long reserveNo) {
		int result = 0;

		try {
			meetingRoomReserveRepository.deleteById(reserveNo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 예약 리스트 조회
	public List<MeetingRoomReserveDto> MeetingRoomReserveList(Long empId){
		List<MeetingRoomReserve> reserve = meetingRoomReserveRepository.findByEmpId(empId);
		
		List<MeetingRoomReserveDto> reserveDto = new ArrayList<MeetingRoomReserveDto>();
		for(MeetingRoomReserve r : reserve) {
			MeetingRoomReserveDto toDto = new MeetingRoomReserveDto().toDto(r);
			
			// 만약 예약 일이 현재 날짜보다 후에 있고, 예약 종료 시간이 현재 시간보다 후에 있으면 출력
			if(toDto.getMeeting_room_reserve_date().isAfter(LocalDate.now()) || toDto.getMeeting_room_end_time().isAfter(LocalTime.now())) {
				reserveDto.add(toDto);
			} else { 
				// 예약 일과 예약 시간이 지났으면 삭제
				ReserveDelete(toDto.getMeeting_room_reserve_no());
			}
		}
		return reserveDto;
	}
	
	// 회의실 삭제
	public int MeetingRoomDelete(Long roomNo) {
		int result = 0;
		try {
			meetingRoomRepository.deleteById(roomNo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 회의실 추가
	public MeetingRoom MeetingRoomCreate(MeetingRoomDto dto) {
		MeetingRoom create = MeetingRoom.builder()
				.meetingRoomName(dto.getMeeting_room_name())
				.meetingRoomPlace(dto.getMeeting_room_place())
				.meetingRoomAmenity(dto.getMeeting_room_amenity())
				.meetingRoomImg(dto.getMeeting_room_img())
				.build();
		return meetingRoomRepository.save(create);
	}
	
	// 예약 가능 시간 추가
	public void MeetingRoomTimeCreate(LocalTime[][] time) {
		
		MeetingRoom meetingRoom = meetingRoomRepository.findBy();
		MeetingRoomDto meetingRoomDto = new MeetingRoomDto().toDto(meetingRoom);
		String[] ampm = new String[time.length];
		LocalTime standard = LocalTime.of(12, 00);
		for(int i=0; i<time.length; i++) {
			int compare = time[i][1].compareTo(standard);
			if(compare <= (-1) ) {
				ampm[i] = "오전";
			} else {
				ampm[i] = "오후";
			}
		}
		for(int i=0; i<time.length; i++) {
			MeetingRoomTime times = MeetingRoomTime.builder()
					.meetingRoomNo(meetingRoomDto.getMeeting_room_no())
					.meetingRoomStartTime(time[i][0])
					.meetingRoomEndTime(time[i][1])
					.meetingRoomAmpm(ampm[i])
					.build();
			meetingRoomTimeRepository.save(times);
		}
		
				
	}
	
	// 회의실 예약
	public MeetingRoomReserve meetingRoomReserve(MeetingRoomReserveDto dto) {
		int result = 0;
		
		MeetingRoomTime time = meetingRoomTimeRepository.findByMeetingRoomTimeNo(dto.getMeeting_room_reserve_time_no());
		
		MeetingRoomTimeDto timeToDto = new MeetingRoomTimeDto().toDto(time);
		
		MeetingRoomReserve reserve = MeetingRoomReserve.builder()
				.meetingRoom(MeetingRoom.builder().meetingRoomNo(dto.getMeeting_room_no()).build())
				.empId(dto.getEmp_id())
				.meetingRoomReserveDate(dto.getMeeting_room_reserve_date())
				.meetingRoomStartTime(timeToDto.getMeeting_room_start_time())
				.meetingRoomEndTime(timeToDto.getMeeting_room_end_time())
				.meetingRoomReserveTimeNo(dto.getMeeting_room_reserve_time_no())
				.build();
		return meetingRoomReserveRepository.save(reserve);
	}
	
	// 회의실 리스트 뽑기
	public List<MeetingRoomDto> meetingRoomList() {
		List<MeetingRoom> mr = meetingRoomRepository.findAll();
		List<MeetingRoomDto> mrDto = new ArrayList<MeetingRoomDto>();
		for(MeetingRoom m : mr) {
			MeetingRoomDto mrToDto = new MeetingRoomDto().toDto(m);
			mrDto.add(mrToDto);
		}
		return mrDto;
	}
	
	// 회의실 예약 시간 리스트 뽑기
	public List<MeetingRoomTimeDto> meetingRoomTimeList(){
		List<MeetingRoomTime> time = meetingRoomTimeRepository.findAll();
		List<MeetingRoomTimeDto> timeDto = new ArrayList<MeetingRoomTimeDto>();
		for(MeetingRoomTime t : time) {
			MeetingRoomTimeDto timeToDto = new MeetingRoomTimeDto().toDto(t);
			timeDto.add(timeToDto);
		}
		return timeDto;
	}

}