package com.codenal.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.employee.domain.Employee;

public interface ChatParticipantsRepository extends JpaRepository<ChatParticipants, Integer> {

	// 활성 상태의 본인이 참여중인 채팅 목록 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.employee = ?1 AND p.participateStatus = 'Y' ")
	List<ChatParticipants> findByChatRoom(Employee empId);

	// 본인을 제외한 참여자 정보 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.employee != ?1 AND p.chatRoom IN (SELECT e.chatRoom FROM ChatParticipants e WHERE e.employee = ?1 AND e.participateStatus = 'Y' ) ")
	List<ChatParticipants> findByNotMeChatRoom(Employee empId);
	
	// 본인을 제외한 참여자 정보 조회
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.participantNo != ?2 ")
	List<ChatParticipants> findByNotMeChatRoom(ChatRoom roomNo, int userNo);

	// 내가 선택한 채팅방의 참가번호
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.participantNo = ?2 ")
	ChatParticipants findByParticipants(ChatRoom roomNo, int userNo);
	
	// 내가 선택한 채팅방의 참가번호
	@Query(value="SELECT p FROM ChatParticipants p WHERE p.chatRoom = ?1 AND p.participateStatus = 'Y' AND p.employee = ?2 ")
	ChatParticipants findByEmpId(ChatRoom roomNo, Employee empId);

	@Modifying
	@Query(value="UPDATE ChatParticipants c SET c.participateStatus = 'N', c.exitDate=CURRENT_TIMESTAMP WHERE c.chatRoom = ?1 AND c.employee = ?2 ")
	int updateByParticipateStatus(ChatRoom roomNo, Employee empId);
	
	@Query("SELECT COUNT(c) FROM ChatParticipants c WHERE c.chatRoom = ?1 AND c.participateStatus = 'Y'")
	long countParticipantsWithStatusY(ChatRoom roomNo);

	void deleteByChatRoom(ChatRoom chatRoom);

	List<ChatParticipants> findByChatRoomRoomNoAndParticipateStatus(int chatRoom, char status);


}
