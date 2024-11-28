package com.codenal.websocket;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.codenal.chat.domain.ChatMsg;
import com.codenal.chat.domain.ChatMsgDto;
import com.codenal.chat.domain.ChatParticipantsDto;
import com.codenal.chat.domain.ChatRoom;
import com.codenal.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	private final ChatService chatService;
	private final NotificationWebSocketHandler notificationWebSocketHandler;

	@Autowired
	public ChatWebSocketHandler(ChatService chatService, NotificationWebSocketHandler notificationWebSocketHandler) {
		this.chatService = chatService;
		this.notificationWebSocketHandler = notificationWebSocketHandler;
	}

	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>());

	private Map<String, WebSocketSession> clients = new HashMap<String, WebSocketSession>();

	// 클라이언트가 연결되었을 때 설정
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("New connection established: " + session.getId());
		sessions.add(session);
	}

	// 클라이언트가 웹소켓 서버로 메시지를 전송했을 때 설정
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

		// 클라이언트가 보낸 메시지
		String payload = message.getPayload();
		System.out.println("Received payload: " + payload);
		
		ObjectMapper objectMapper = new ObjectMapper();
		ChatMsgDto msg = objectMapper.readValue(payload, ChatMsgDto.class);

		JSONObject json = new JSONObject(message.getPayload());
		String chatType = json.getString("chat_type");
		String roomNo = json.getString("room_no");
		int userNo = json.getInt("participant_no");

		switch (msg.getChat_type()) {
		case "open":
			String userNoStr = String.valueOf(userNo); // userNo를 String으로 변환
			System.out.println("clients.containsKey(userNo): " + clients.containsKey(userNoStr)); // 중복 여부 확인
			if (clients.containsKey(userNoStr)) {
				System.out.println("이미 등록된 클라이언트: " + clients);
				return; // 이미 등록된 경우, 더 이상 open 이벤트 처리하지 않음
			}
			clients.put(userNoStr, session); // 클라이언트 등록
			System.out.println("=== 등록 확인 ===");
			System.out.println("현재 접속중인 클라이언트: " + clients);
			System.out.println("등록 후 containsKey: " + clients.containsKey(userNoStr));

			// 방 번호와 채팅 타입을 세션에 저장
			session.getAttributes().put("chat_type", chatType);
			session.getAttributes().put("room_no", roomNo);

			System.out.println("채팅 타입 저장: " + chatType);
			System.out.println("방 번호 저장: " + roomNo);

			// 새로 접속한 클라이언트에게만 메시지 전송
			int roomno = Integer.parseInt((String) session.getAttributes().get("room_no"));
			Long participantId = Long.valueOf(session.getPrincipal().getName());
			ChatRoom room = chatService.selectChatRoomOne(roomno, participantId);

			for (ChatMsg chat : room.getMessages()) {
				Map<String, Object> result = new HashMap<>();
				result.put("res_code", "200");
				result.put("res_msg", "채팅 메시지 조회를 완료했습니다.");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime sendDate = chat.getSendDate();
				result.put("send_date", sendDate.format(formatter)); // LocalDateTime을 문자열로 변환하여 Map에 저장
				result.put("msg_type", Character.toString(chat.getMsgType()));
				result.put("msg_content", chat.getMsgContent());
				result.put("sender_no", chat.getChatParticipant().getParticipantNo());
				result.put("room_no", chat.getChatRoom().getRoomNo());

				TextMessage resultMsg = new TextMessage(objectMapper.writeValueAsString(result));

				session.sendMessage(resultMsg);
			}
			break;

		case "msg":
			// 메시지를 DB에 저장
			ChatMsg chat = chatService.createChatMsg(msg);
			Map<String, String> resultMap = new HashMap<String, String>();
			if (chat != null) {
				resultMap.put("res_code", "200");
				resultMap.put("res_msg", "채팅 전송을 완료했습니다.");
				resultMap.put("msg_content", chat.getMsgContent());
				resultMap.put("sender_no", String.valueOf(chat.getChatParticipant().getParticipantNo()));
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime sendDate = chat.getSendDate();
				resultMap.put("send_date", sendDate.format(formatter)); // LocalDateTime을 문자열로 변환하여 Map에 저장
				resultMap.put("msg_type", String.valueOf(chat.getMsgType()));
				resultMap.put("room_no", String.valueOf(chat.getChatRoom().getRoomNo()));
				resultMap.put("sender_name", chat.getChatParticipant().getEmployee().getEmpName());

				// 메시지를 받은 사용자에게 읽음 상태 업데이트
				for (WebSocketSession client : clients.values()) {
					if (client.isOpen()) { // 세션이 열려있는지 확인
						// 클라이언트에게 메시지 전송
						client.sendMessage(new TextMessage(objectMapper.writeValueAsString(resultMap)));

						Long empNo = Long.parseLong(client.getPrincipal().getName());
						chatService.updateMessageReadStatus(empNo);
					}
				}

				// 특정 채팅방 참가자 리스트 조회
				List<ChatParticipantsDto> participantsList = chatService
						.selectChatRoomParticipants(chat.getChatRoom().getRoomNo());

				// 특정 대상자들에게만 알림 메시지 전송
				notificationWebSocketHandler.sendNotificationToSpecificUsers(participantsList,
						objectMapper.writeValueAsString(resultMap));

			}
			break;
		}
	}

	// 클라이언트가 연결을 끊었을 때 설정
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		// 나간 사용자의 세션을 clients 맵에서 제거
		// 나머지 사용자들의 세션은 여전히 clients 맵에 남아있으므로, 채팅 가능
		clients.values().removeAll(Arrays.asList(session));
		System.out.println("=== 삭제 확인 ===");

	}

}