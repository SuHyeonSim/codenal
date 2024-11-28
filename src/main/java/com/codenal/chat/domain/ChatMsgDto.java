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
public class ChatMsgDto {

	private int msg_no;
	private String msg_content;
	private LocalDateTime send_date;
	private char msg_status;
	private char msg_type;
	
	private String chat_type;
	private int room_no;
	private int sender_no;
	private int participant_no;
	
	public ChatMsg toEntity() {
		return ChatMsg.builder()
				.msgNo(msg_no)
				.msgContent(msg_content)
				.sendDate(send_date)
				.msgStatus(msg_status)
				.msgType(msg_type)
				.build();
	}
	public ChatMsgDto toDto(ChatMsg chatMsg) {
		return ChatMsgDto.builder()
				.msg_no(chatMsg.getMsgNo())
				.msg_content(chatMsg.getMsgContent())
				.send_date(chatMsg.getSendDate())
				.msg_status(chatMsg.getMsgStatus())
				.msg_type(chatMsg.getMsgType())
				.build();
	}
}
