package com.codenal.meeting.domain;

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
public class MeetingRoomDto {
	
	private long meeting_room_no;
	private String meeting_room_name;
	private String meeting_room_place;
	private String meeting_room_amenity;
	private String meeting_room_img;
	
	public MeetingRoom toEntity() {
		return MeetingRoom.builder()
				.meetingRoomNo(meeting_room_no)
				.meetingRoomName(meeting_room_name)
				.meetingRoomPlace(meeting_room_place)
				.meetingRoomAmenity(meeting_room_amenity)
				.meetingRoomImg(meeting_room_img)
				.build();
	}
	
	public MeetingRoomDto toDto(MeetingRoom mr) {
		return MeetingRoomDto.builder()
				.meeting_room_no(mr.getMeetingRoomNo())
				.meeting_room_name(mr.getMeetingRoomName())
				.meeting_room_place(mr.getMeetingRoomPlace())
				.meeting_room_amenity(mr.getMeetingRoomAmenity())
				.meeting_room_img(mr.getMeetingRoomImg())
				.build();
	}

}
