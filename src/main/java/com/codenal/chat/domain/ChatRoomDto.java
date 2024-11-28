package com.codenal.chat.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChatRoomDto {

	private int room_no;
	private char room_status;
	private String chat_name;
	private int room_type;
	private LocalDateTime create_date;
	
	private String search_text;
	
	
	public ChatRoom toEntity() {
		return ChatRoom.builder()
				.roomNo(room_no)
				.roomStatus(room_status)
				.chatName(chat_name)
				.roomType(room_type)
				.createDate(create_date)
				.build();
	}
	
	public ChatRoomDto toDto(ChatRoom chatRoom) {
		return ChatRoomDto.builder()
				.room_no(chatRoom.getRoomNo())
				.room_status(chatRoom.getRoomStatus())
				.chat_name(chatRoom.getChatName())
				.room_type(chatRoom.getRoomType())
				.create_date(chatRoom.getCreateDate())
				.build();
	}
}
