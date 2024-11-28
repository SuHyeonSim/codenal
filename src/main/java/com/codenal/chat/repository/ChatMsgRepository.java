package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatRoom;

public interface ChatMsgRepository extends JpaRepository<ChatMsg, Integer>{

	@Query(value="SELECT m FROM ChatMsg m WHERE m.chatRoom = ?1 AND m.msgStatus = 'Y' AND m.sendDate >=(SELECT p.participateDate FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participantNo = ?2) ")
	List<ChatMsg> findAllById(int roomNo, int userNo);

	void deleteByChatRoom(ChatRoom chatRoom);

	@Query(value = "SELECT m FROM ChatMsg m WHERE m.chatRoom = ?1 ORDER BY m.sendDate DESC")
	ChatMsg findTopByChatRoomOrderBySendDateDesc(ChatRoom chatRoom);



}
