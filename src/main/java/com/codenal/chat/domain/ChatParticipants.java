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
@Table(name="chat_participants")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Builder
public class ChatParticipants {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="participant_no")
	private int participantNo;
	
	@ManyToOne
	@JoinColumn(name="room_no")
	private ChatRoom chatRoom;
	
	@ManyToOne
	@JoinColumn(name="emp_id")
	private Employee employee;
	
	@Column(name="switch_status")
	private char switchStatus;
	
	@Column(name="participate_date")
	private LocalDateTime participateDate;
	
	@PrePersist
	public void prePersist() {
		this.participateDate = LocalDateTime.now();
	}
	
	@Column(name="exit_date")
	private LocalDateTime exitDate;
	
	@Column(name="participate_status")
	private char participateStatus;
	
	@OneToMany(mappedBy = "chatParticipant")
	private List<ChatRead> chatReads;
	
	@OneToMany(mappedBy = "chatParticipant")
    private List<ChatMsg> messages;

}
