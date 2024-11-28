package com.codenal.chat.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.domain.ChatParticipants;
import com.codenal.chat.domain.ChatParticipantsDto;
import com.codenal.chat.domain.ChatRead;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.chat.domain.ChatRoomDto;
import com.codenal.chat.repository.ChatMsgRepository;
import com.codenal.chat.repository.ChatParticipantsRepository;
import com.codenal.chat.repository.ChatReadRepository;
import com.codenal.chat.repository.ChatRoomRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatParticipantsRepository chatParticipantsRepository;
	private final EmployeeRepository employeeRepository;
	private final ChatMsgRepository chatMsgRepository;
	private final ChatReadRepository chatReadRepository;
	
	@Autowired
	public ChatService(ChatRoomRepository chatRoomRepository, ChatParticipantsRepository chatParticipantsRepository
			, EmployeeRepository employeeRepository, ChatMsgRepository chatMsgRepository, ChatReadRepository chatReadRepository) {
		this.chatRoomRepository=chatRoomRepository;
		this.chatParticipantsRepository=chatParticipantsRepository;
		this.chatMsgRepository=chatMsgRepository;
		this.chatReadRepository=chatReadRepository;
		this.employeeRepository=employeeRepository;
	}
	
	
	
	@Transactional
	public ChatRoom createChat(ChatRoomDto roomDto, List<String> empIds, Long userId) {
		// 채팅방 이름 저장할 직원 이름 정보
		List<String> empName = new ArrayList<String>();
		for(String empId : empIds) {
		Employee emp = employeeRepository.findByEmpId(Long.parseLong(empId));
			empName.add(emp.getEmpName());
		}
		
		// 1. 채팅방 정보 save
		ChatRoom chatRoom = ChatRoom.builder()
				.chatName(empIds.size() <=2 ? empName.toString().substring(1, empName.toString().length()-1) : roomDto.getChat_name())
				.roomType(empIds.size()<=2?1 : 2)
				.roomStatus('Y')
				.build();
		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
			
		for(String empId : empIds) {
			// 2. 참여자 정보 save
			ChatParticipants participant = ChatParticipants.builder()
				.chatRoom(savedChatRoom)
				.employee(employeeRepository.findByEmpId(Long.parseLong(empId)))
				.switchStatus('Y')
				.participateStatus('Y')
				.build();
			ChatParticipants participantNo = chatParticipantsRepository.save(participant);
		}
		
		//  초대 정보 메시지 추가
		Employee emp = employeeRepository.findByEmpId(userId);
		ChatParticipants userNo = chatParticipantsRepository.findByEmpId(savedChatRoom, emp);
		String empNames = "";
		for(String empId : empIds) {
			Employee employee = employeeRepository.findByEmpId(Long.parseLong(empId));
			if(!employee.getEmpId().equals(emp.getEmpId())) {
			empNames += employee.getEmpName()+"님,";
			}
		}
		String empname = empNames.substring(0,empNames.length()-1);
		ChatMsg target = ChatMsg.builder()
				.msgContent(userNo.getEmployee().getEmpName()+"님이 "+empname+"을 초대했습니다.")
				.chatRoom(savedChatRoom)
				.chatParticipant(userNo)
				.msgStatus('Y')
				.msgType('1')
				.build();

		ChatMsg savedMsg = chatMsgRepository.save(target);
		
		
		return savedChatRoom;
	}
	
	// 본인의 활성 상태인 채팅 참여 테이블 조회
	public List<ChatParticipants> participantListSelect(String username){
		Long empId = Long.parseLong(username);
		Employee emp = employeeRepository.findByEmpId(empId);
		List<ChatParticipants> chatLists = chatParticipantsRepository.findByChatRoom(emp);
		List<ChatParticipantsDto> chatListDto = new ArrayList<ChatParticipantsDto>();
		for(ChatParticipants chatList :chatLists) {
			ChatParticipantsDto dto = new ChatParticipantsDto().toDto(chatList);
			dto.setRoom_no(chatList.getChatRoom().getRoomNo());
			dto.setEmp_id(chatList.getEmployee().getEmpId());
			chatListDto.add(dto);
		}
		return chatLists;
	}
	
	// 내가 속하면서 나를 제외한 채팅방 참가자 정보 조회
	public List<ChatParticipants> notMeParticipant(String username){
		Long empId = Long.parseLong(username);
		Employee emp = employeeRepository.findByEmpId(empId);
		List<ChatParticipants> chatLists = chatParticipantsRepository.findByNotMeChatRoom(emp);
		if (chatLists != null) {
			List<ChatParticipantsDto> chatListDto = new ArrayList<ChatParticipantsDto>();
			for(ChatParticipants chatList :chatLists) {
				ChatParticipantsDto dto = new ChatParticipantsDto().toDto(chatList);
				dto.setRoom_no(chatList.getChatRoom().getRoomNo());
				dto.setEmp_id(chatList.getEmployee().getEmpId());
				chatListDto.add(dto);
			}
		} else {
		    // chatParticipant가 null일 때의 처리 로직
		    System.out.println("ChatParticipant 객체가 null입니다.");
		}
		
		return chatLists;
	}

	
	public Map<Integer, Long> countUnreadMessagesForParticipants(String username) {
		List<ChatParticipants> participants = participantListSelect(username);
        Map<Integer, Long> unreadCounts = new HashMap<>();

        for (ChatParticipants participant : participants) {
            int participantNo = participant.getParticipantNo(); // 내 참가자 No 조회
            // 각 참가자에 대한 안 읽은 메시지 수 계산
            Long unreadCount = chatReadRepository.countByChatParticipant_ParticipantNoAndIsReceiverRead(participantNo, 'N');
            unreadCounts.put(participantNo, unreadCount);
            System.out.println("ParticipantNo: " + participantNo + ", UnreadCount: " + unreadCount);
        }

        return unreadCounts;
    }
	
	
	// chat detail 부분
	@Transactional
	public ChatRoom selectChatRoomOne(int roomNo, Long empId) {
		// 채팅 참여자 정보와 채팅 메시지 불러오기
		// 채팅방 입장 순간 알림온 메시지 읽은 상태로 update
		ChatRoom chatRoom = chatRoomRepository.findByRoomNo(roomNo);
		Employee employee = employeeRepository.findByEmpId(empId);
		// 내 참가자 번호 불러오기
		ChatParticipants chatParticipant = chatParticipantsRepository.findByEmpId(chatRoom, employee);
		
		// 추가 초대한 경우 메시지 불러오는거 수정해야함
		// 메시지 상태들이 'Y'이면서 내가 채팅방에 참여한 날짜 이후부터의 메시지만 불러오기
		chatReadRepository.updateByRead(chatParticipant.getParticipantNo());  // --> roomNo 마다 내 참가자 번호가 다르니까 위에서 찾은 참가자 번호로만 업데이트
	    
		Hibernate.initialize(chatRoom.getMessages());

		return chatRoom;
	}
	
	// 추가 초대한 경우 메시지 불러오는거 수정 함께 해야함
	@Transactional
	public List<ChatMsg> selectChatMsgList(int roomNo, Long empId) {
		ChatRoom chatRoom = chatRoomRepository.findByRoomNo(roomNo);
		Employee employee = employeeRepository.findByEmpId(empId);
		// 내 참가자 번호 불러오기
		ChatParticipants chatParticipant = chatParticipantsRepository.findByEmpId(chatRoom, employee);
	    // 메시지 상태들이 'Y'이면서 내가 채팅방에 참여한 날짜 이후부터의 메시지만 불러오기
		List<ChatMsg> msgs = chatMsgRepository.findAllById(roomNo, chatParticipant.getParticipantNo());
		return msgs;
	
	}
	
	// 메시지 전송시 생성
	@Transactional
	public ChatMsg createChatMsg(ChatMsgDto dto) {
			ChatRoom room = chatRoomRepository.findByRoomNo(dto.getRoom_no());
			ChatParticipants senderNo = chatParticipantsRepository.findByParticipants(room,dto.getSender_no());
			ChatMsg target = ChatMsg.builder()					.chatRoom(room)
					.chatParticipant(senderNo)
					.msgContent(dto.getMsg_content())
					.msgStatus('Y')
					.msgType(dto.getMsg_type())
					.build();

			ChatMsg savedMsg = chatMsgRepository.save(target); // 채팅 메시지 정보 저장
			
			List<ChatParticipants> participants = chatParticipantsRepository.findByNotMeChatRoom(room, senderNo.getParticipantNo()); // 본인을 제외한 참여자 정보
			for(ChatParticipants participant : participants) { // 읽지 않은 상태를 저장
				ChatRead read = ChatRead.builder()
						.chatMsg(savedMsg)
						.chatParticipant(participant)
						.isReceiverRead('N')
						.build();
				chatReadRepository.save(read);
			
			}
			return savedMsg;
	}


	@Transactional
	public int removeUserFromRoom(int roomNo, Long empId) {
	    int result = -1;

	    ChatRoom chatRoom = chatRoomRepository.findByRoomNo(roomNo);
	    Employee employee = employeeRepository.findByEmpId(empId);
	    ChatParticipants chatParticipant = chatParticipantsRepository.findByEmpId(chatRoom, employee);
	    
	    if (chatParticipantsRepository.updateByParticipateStatus(chatRoom, employee) > 0) {
	        ChatMsg target = ChatMsg.builder()
	                .msgContent(employee.getEmpName() + "님이 채팅방에서 퇴장했습니다.")
	                .chatRoom(chatRoom)
	                .chatParticipant(chatParticipant)
	                .msgStatus('Y')
	                .msgType('1')
	                .build();

	        chatMsgRepository.save(target);

	        boolean exists = chatParticipantsRepository.countParticipantsWithStatusY(chatRoom) > 0;

	        if (!exists) {
	            // 1) 읽음 테이블에서 해당 채팅방의 모든 데이터를 삭제 (ChatMsg 경유)
	            chatReadRepository.deleteByChatRoom(chatRoom);

	            // 2) 해당 채팅방의 메시지를 삭제
	            chatMsgRepository.deleteByChatRoom(chatRoom);

	            // 3) 해당 채팅방의 모든 참여자를 삭제
	            chatParticipantsRepository.deleteByChatRoom(chatRoom);

	            // 4) 마지막으로 채팅방 자체를 삭제
	            chatRoomRepository.deleteById(roomNo);
	        }

	        result = 1;
	    }

	    return result;
	}



	@Transactional
	public void updateMessageReadStatus(Long empId) {
		chatReadRepository.updateByRead(empId);
		
	}

	
	public List<ChatParticipantsDto> selectChatRoomParticipants(int chatRoom){
		List<ChatParticipants> participants = chatParticipantsRepository.findByChatRoomRoomNoAndParticipateStatus(chatRoom, 'Y');
		List<ChatParticipantsDto> chatListDto = new ArrayList<ChatParticipantsDto>();
		for(ChatParticipants participant : participants) {
			ChatParticipantsDto dto = new ChatParticipantsDto().toDto(participant);
			dto.setEmp_id(participant.getEmployee().getEmpId());
			chatListDto.add(dto);
		}
		return chatListDto;
	}

	public List<EmployeeDto> getActiveEmployeeListSearch(String username, String empSearch) {
    	Long empId = Long.parseLong(username);
    	List<Employee> empList = employeeRepository.findByEmpNameContainingOrDepartmentsDeptNameContaining(empId, empSearch);;
    	
    	List<EmployeeDto> dtoList = new ArrayList<EmployeeDto>();
    	for(Employee e : empList) {
    		EmployeeDto dto = new EmployeeDto().fromEntity(e);
    		dtoList.add(dto);
    	}
    	return dtoList;
	}

}
