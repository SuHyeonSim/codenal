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
public class ChatParticipantsDto {

	private int participant_no;
	private int room_no;
	private Long emp_id;
	private char switch_status;
	private LocalDateTime participate_date;
	private LocalDateTime exit_date;
	private char participate_status;
	
	public ChatParticipants toEntity() {
		return ChatParticipants.builder()
				.participantNo(participant_no)
				.switchStatus(switch_status)
				.participateDate(participate_date)
				.exitDate(exit_date)
				.participateStatus(participate_status)
				.build();
	}
	
	public ChatParticipantsDto toDto(ChatParticipants chatParticipants) {
		return ChatParticipantsDto.builder()
				.participant_no(chatParticipants.getParticipantNo())
				.switch_status(chatParticipants.getSwitchStatus())
				.participate_date(chatParticipants.getParticipateDate())
				.exit_date(chatParticipants.getExitDate())
				.participate_status(chatParticipants.getParticipateStatus())
				.build();
	}

}
