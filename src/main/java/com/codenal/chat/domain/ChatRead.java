package com.codenal.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="chat_read")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Builder
public class ChatRead {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="read_no")
	private int readNo;
	
	@ManyToOne
	@JoinColumn(name="msg_no")
	private ChatMsg chatMsg;
	
	@ManyToOne
	@JoinColumn(name="participant_no")
	private ChatParticipants chatParticipant;
	
	@Column(name="is_receiver_read")
	private char isReceiverRead;
	
	
}
