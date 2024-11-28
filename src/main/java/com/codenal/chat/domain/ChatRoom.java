package com.codenal.chat.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="chat_room")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Builder
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="room_no")
	private int roomNo;
	
	@Column(name="room_status")
	private char roomStatus;
	
	@Column(name="chat_name")
	private String chatName;
	
	@Column(name="room_type")
	private int roomType;
	
	@Column(name="create_date")
	private LocalDateTime createDate;
	
	@PrePersist
	public void prePersist() {
		this.createDate = LocalDateTime.now();
	}
	
	@OneToMany(mappedBy = "chatRoom")
	private List<ChatParticipants> participants;
	
	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMsg> messages;
	
	
	
}
