package com.codenal.chat.domain;

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
public class ChatReadDto {

	private int read_no;
	private char is_receiver_read;
	
	public ChatRead toEntity() {
		return ChatRead.builder()
				.readNo(read_no)
				.isReceiverRead(is_receiver_read)
				.build();
	}
	
	public ChatReadDto toDto(ChatRead chatRead) {
		return ChatReadDto.builder()
				.read_no(chatRead.getReadNo())
				.is_receiver_read(chatRead.getIsReceiverRead())
				.build();
	}
}
