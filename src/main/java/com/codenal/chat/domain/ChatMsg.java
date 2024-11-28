package com.codenal.chat.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="chat_msg")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Builder
public class ChatMsg {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="msg_no")
	private int msgNo;
	
	@ManyToOne
	@JoinColumn(name="room_no")
	private ChatRoom chatRoom;
	
	@ManyToOne
	@JoinColumn(name="sender_no")
	private ChatParticipants chatParticipant;
	
	@Column(name="msg_content")
	private String msgContent;
	
	@Column(name="send_date")
	private LocalDateTime sendDate;
	
	@PrePersist
	public void prePersist() {
		this.sendDate = LocalDateTime.now();
	}
	
	@Column(name="msg_status")
	private char msgStatus;
	
	@Column(name="msg_type")
	private char msgType;
	
	@OneToMany(mappedBy = "chatMsg")
	private List<ChatRead> chatReads;
}
