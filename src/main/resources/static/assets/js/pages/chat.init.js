/* 참여자 초대 모달 JS 시작 */
function searchEmployees() {
	const keyword = document.getElementById('searchInput').value.trim();  // 검색어 입력받기


	const csrfToken = document.getElementById('csrf_token').value;  // CSRF 토큰 가져오기
	const fetchUrl = `/chatList/search?keyword=${keyword}`;  // AJAX로 검색 요청 보낼 URL

	fetch(fetchUrl, {
		method: 'GET',
		headers: {
			'X-CSRF-TOKEN': csrfToken,
			'Content-Type': 'application/json'
		}
	})
		.then(response => response.json())
		.then(data => {
    const employeeList = document.querySelector('#employeeList');  // <ul> 태그를 가져옴
    employeeList.innerHTML = '';  // 기존 목록을 지우고 새 목록을 추가

    if (data.length === 0) {
        const noResultItem = document.createElement('li');
        noResultItem.classList.add('list-unstyled');
        noResultItem.innerText = '검색 결과가 없습니다.';
        employeeList.appendChild(noResultItem);
    } else {
        let previousDeptName = '';  // 이전에 출력된 부서명을 저장할 변수
        
        data.forEach(employee => {
            const currentDeptName = employee.deptName != null ? employee.deptName : '부서명 없음';

            // li 요소 생성
            const listItem = document.createElement('li');
            listItem.classList.add('list-unstyled');

            // 부서명이 다를 때만 출력
            if (currentDeptName !== previousDeptName) {
                const deptDiv = document.createElement('div');
                deptDiv.classList.add('border-top', 'mt-3', 'mb-1');
                const deptName = document.createElement('small');
                deptName.classList.add('deptName', 'text-muted');
                deptName.innerText = currentDeptName;
                deptDiv.appendChild(deptName);
                listItem.appendChild(deptDiv);
                
                // 이전 부서명을 현재 부서명으로 업데이트
                previousDeptName = currentDeptName;
            }

            // 직원 목록 div 생성
            const employeeDiv = document.createElement('div');
            employeeDiv.classList.add('d-flex', 'align-items-center', 'mb-1');

            // 프로필 이미지 div 생성
            const avatarDiv = document.createElement('div');
            avatarDiv.classList.add('avatar-xs', 'flex-shrink-0', 'me-3');
            const img = document.createElement('img');
            img.classList.add('img-fluid', 'rounded-circle', 'border');
            img.alt = '직원 프로필 이미지';
            img.src = employee.empProfilePicture != null ? employee.empProfilePicture : '/assets/images/users/user-dummy-img.jpg';
            avatarDiv.appendChild(img);

            // 직원 정보 div 생성
            const employeeInfoDiv = document.createElement('div');
            employeeInfoDiv.classList.add('flex-grow-1');
            const h5 = document.createElement('h5');
            h5.classList.add('fs-13', 'mb-0');
            const empNameBlock = document.createElement('div');
            empNameBlock.classList.add('empName', 'd-block');
            const empNameSpan = document.createElement('span');
            empNameSpan.innerText = employee.empName != null ? employee.empName : '이름 없음';
            const jobNameSpan = document.createElement('span');
            jobNameSpan.innerText = employee.jobName != null ? employee.jobName : '직무명 없음';
            
            empNameBlock.appendChild(empNameSpan);
            empNameBlock.appendChild(jobNameSpan);
            h5.appendChild(empNameBlock);
            employeeInfoDiv.appendChild(h5);

            // 선택 버튼 생성
            const buttonDiv = document.createElement('div');
            buttonDiv.classList.add('flex-shrink-0');
            const selectBtn = document.createElement('button');
            selectBtn.type = 'button';
            selectBtn.classList.add('btn', 'btn-light', 'btn-sm');
            selectBtn.innerText = '선택';
            selectBtn.setAttribute('data-emp-name', employee.empName);
            selectBtn.setAttribute('data-emp-id', employee.empId);
            selectBtn.setAttribute('data-emp-profile', employee.empProfilePicture != null ? employee.empProfilePicture : '/assets/images/users/user-dummy-img.jpg');
            selectBtn.onclick = function() {
                employeeSelect_btn(this);
            };

            buttonDiv.appendChild(selectBtn);

            // 모든 요소 조합
            employeeDiv.appendChild(avatarDiv);
            employeeDiv.appendChild(employeeInfoDiv);
            employeeDiv.appendChild(buttonDiv);

            listItem.appendChild(employeeDiv);
            employeeList.appendChild(listItem);  // <ul>에 <li> 추가
        });
    }
});
}

// 직원 선택 로직
const employeeSelect_btn = function(button) {

	const empName = button.getAttribute('data-emp-name');
	const empId = button.getAttribute('data-emp-id');
	const empProfile = button.getAttribute('data-emp-profile');
	const view_div = document.getElementById('selectEmp');
	const form = document.getElementById('selectEmployeeForm');

	// 이미 선택된 직원인지 확인
	const existingEmployee = document.querySelector(`input[name='emp_id[]'][value='${empId}']`);

	// 직원이 이미 선택되어 있는 경우 -> 선택 해제
	if (existingEmployee) {
		// 선택된 직원의 아바타를 제거
		const avatarGroupItem = view_div.querySelector(`div[data-bs-toggle='tooltip'][title='${empName}']`);
		if (avatarGroupItem) {
			avatarGroupItem.remove();
		}

		// 숨겨진 입력 필드에서 해당 직원 ID 제거
		existingEmployee.remove();
	} else {
		// 직원이 선택되지 않은 경우 -> 선택 추가
		// 선택된 직원의 프로필 추가
		view_div.innerHTML += ` <div class='avatar-group-item' data-bs-toggle='tooltip' data-bs-trigger='hover' data-bs-placement='top' title='${empName}'>
						        	<div class='avatar-xs'>
						               <img src='${empProfile}' alt='' class='rounded-circle img-fluid border'>
						            </div>
						        </div>`;

		// 숨겨진 입력 필드로 선택된 직원 ID 추가
		const hiddenInput = document.createElement('input');
		hiddenInput.type = 'hidden';
		hiddenInput.name = 'emp_id[]';  // 배열 형태로 여러 직원 선택
		hiddenInput.value = empId;
		form.appendChild(hiddenInput);
	}
	// 직원이 2명 이상 선택된 경우에만 EmpForm 실행
	if (form && form.querySelectorAll('input[name="emp_id[]"]').length >= 2) {
		EmpForm();
	}
};

/* 그룹 채팅방 이름 설정*/
function EmpForm() {
	const form = document.getElementById("selectEmployeeForm");

	// 이미 chatNameInput이 추가되었는지 확인
	if (!form.querySelector('input[name="chat_name"]')) {
		const chatNamediv = document.createElement('div');
		chatNamediv.innerText = '그룹 채팅방 이름: ';
		const chatNameInput = document.createElement('input');
		chatNameInput.type = 'text';
		chatNameInput.name = 'chat_name';
		chatNameInput.placeholder = '이름 입력';
		// chatNamediv에 input 요소 추가
		chatNamediv.appendChild(chatNameInput);
		// 필요한 경우 DOM의 원하는 위치에 추가
		document.getElementById('chatname_container').appendChild(chatNamediv);
	}

}


document.getElementById("inviteBtn").addEventListener('click', function(e) {
	e.preventDefault();

	const form = document.getElementById("selectEmployeeForm");
	const formData = new FormData(form);  // 폼 데이터 가져오기
	const selectedEmpIds = formData.getAll('emp_id[]');  // 선택된 직원 ID 목록을 배열로 가져오기
	const oneChatNos = document.querySelectorAll(`input[class='oneChatEmpId']`); // 모든 1:1 채팅방의 empId
	const oneChatRooms = document.querySelectorAll(`input[class='oneChatRoomNo']`); // 모든 1:1 채팅방의 방 번호

	if (selectedEmpIds.length === 0) {
		alert('초대할 대상을 선택하세요.');
		return;
	} else {
		// 기존 1:1 채팅방 유저 ID와 방 번호 수집
		const existingChatEmpIds = [];
		const existingChatRoomNos = [];

		oneChatNos.forEach(input => existingChatEmpIds.push(input.value));
		oneChatRooms.forEach(input => existingChatRoomNos.push(input.value));

		console.log("선택된 Emp ID들 (배열):", selectedEmpIds);  // 선택된 대상 확인

		// 선택된 대상이 1명일 때만 1:1 채팅방과 비교
		if (selectedEmpIds.length === 1) {
			selectedEmpIds.forEach(selectedEmpId => {
				const index = existingChatEmpIds.indexOf(selectedEmpId);
				if (index !== -1) {
					alert('존재하는 채팅방입니다.');
					// 이미 존재하는 1:1 채팅방으로 리다이렉트
					window.location.href = `/chatList/${existingChatRoomNos[index]}`;
				}
				else {
					// 새로운 1:1 채팅방 생성

					for (let [key, value] of formData.entries()) {
						console.log(key, value); // 폼 데이터 출력
					}

					const csrfToken = document.getElementById("csrf_token").value;

					fetch("/chatList/chatRoom/create", {
						method: 'POST',
						headers: {
							'X-CSRF-TOKEN': csrfToken,
						},
						body: formData
					})
						.then(response => response.json())
						.then(data => {
							if (data.res_code === '200') {
								alert(data.res_msg);
								location.href = `/chatList/${data.roomNo}`; // 서버에서 받은 `roomNo`로 URL을 설정
							} else {
								alert('실패: ' + data.res_msg);
							}
						});


				}
			})
		}
		else {
			// 2명 이상 선택했을 경우 (그룹 채팅방 생성)   
			for (let [key, value] of formData.entries()) {
				console.log(key, value); // 폼 데이터 출력
			}

			// chatName 입력 필드가 존재하는지 확인
			const chatNameInput = form.querySelector('input[name="chat_name"]');
			if (chatNameInput && chatNameInput.value !== '') {
				// chatNameInput이 존재하고, 값이 비어있지 않으면 실행할 코드
				console.log('채팅방 이름이 입력되었습니다:', chatNameInput.value);
				const csrfToken = document.getElementById("csrf_token").value;

				fetch("/chatList/chatRoom/create", {
					method: 'POST',
					headers: {
						'X-CSRF-TOKEN': csrfToken,
					},
					body: formData
				})
					.then(response => response.json())
					.then(data => {
						if (data.res_code === '200') {
							alert(data.res_msg);
							location.href = `/chatList/${data.roomNo}`; // 서버에서 받은 `roomNo`로 URL을 설정
						} else {
							alert('채팅방 생성 실패: ' + data.res_msg);
						}
					})
			} else {
				alert('초대할 그룹 채팅방 이름을 입력하세요.');
				chatNameInput.focus();
			}
		}
	}
});
/* 참여자 초대 모달 JS 끝 */




/* 채팅 웹소켓 JS 시작*/

// 전역 변수 설정
window.currentRoomNo = null; // 현재 접속한 채팅방 번호, 다른 채팅방에서 보낸 토스트 알림을 받기 위해 설정
window.isChatPage = false;   // 채팅방 선택 전까지는 토스트 알림을 받을 수 있도록 false로 설정

document.addEventListener('DOMContentLoaded', function() {
	event.preventDefault();
	const roomNo = document.getElementById('room_no');
	const userNo = document.getElementById('sender_no');
	
	const chatSocketUrl = 'ws://localhost:8083/chatting'; // 채팅 웹소켓 URL
	
	// 채팅방을 선택해서 roomNo 값이 생긴다면 웹소켓 연결 시작
	if(roomNo != null){
		window.chatSocket = new WebSocket(chatSocketUrl);
		setUpWebSocketHandlers(window.chatSocket);  // WebSocket 핸들러 설정
			
			
		// 이미 전역에서 알림 웹소켓이 연결된 상태로써 연결이 안되어있는 경우, 알림 웹소켓에 연결 시도
	    if (!window.notificationSocket) {
	        window.notificationSocket = new WebSocket(notificationSocketUrl);
	        setUpNotificationWebSocketHandlers(window.notificationSocket);
	    }
	    
	    // WebSocket 핸들러 설정 함수 (채팅)
		function setUpWebSocketHandlers(socket) {
		window.isChatPage = true;
		window.currentRoomNo = roomNo.value;
		
			// 서버에 접속했을 때 실행될 함수
			socket.onopen = () => {
				console.log("채팅 웹소켓 연결완료");
				const obj = {
					chat_type: "open",
					room_no: roomNo.value,
					participant_no: userNo.value
				};
				socket.send(JSON.stringify(obj)); // 서버로 방 번호 전송
			};
	
	
			socket.onmessage = (response) => {
					const resp = JSON.parse(response.data);
					
					// 방 번호 비교 (현재 접속 중인 방과 메시지의 방 번호)
					if (resp.room_no == window.currentRoomNo) {
						const pageHost = document.getElementById('sender_no').value;
						const senderNo = resp.sender_no;
						// 현재 사용자가 보낸 메시지인지 확인
						const isSender = pageHost == senderNo;
						printMsg(resp.msg_content, senderNo, isSender, resp.send_date, resp.msg_type);
					}
			};
	
			// 마지막으로 출력된 메시지의 날짜를 저장할 변수
			let lastDisplayedDate = null;
	
			const printMsg = function(msg, senderNo, isSender, sendDate, msgType) {
	
				// 메시지 리스트 아이템 생성
				const mainDiv = document.createElement("div");
				mainDiv.className = 'my_chat';
				mainDiv.style.display = 'flex';
				mainDiv.style.alignItems = 'center';
				mainDiv.style.marginTop = '20px';
				mainDiv.style.marginBottom = '20px';
	
				// participants 정보 가져오기
				const participantNos = document.querySelectorAll('.participant_no');
				const participantNames = document.querySelectorAll('.participant_name');
				const participantProfiles = document.querySelectorAll('.participant_profile');
	
				// 날짜 형식을 yyyy-mm-dd로 변환 (비교를 위해)
				const currentMsgDate = new Date(sendDate).toISOString().split('T')[0];
	
				// 날짜가 변경되었을 때만 상단에 날짜 출력
				if (lastDisplayedDate !== currentMsgDate) {
					const dateDiv = document.createElement("div");
					dateDiv.className = 'chat-date badge-soft-primary rounded p-2';
					dateDiv.textContent = new Date(sendDate).toLocaleDateString([], { year: 'numeric', month: 'long', day: 'numeric' });
					dateDiv.style.textAlign = 'center';  // 날짜는 중앙 정렬
					dateDiv.style.marginBottom = '10px';  // 날짜와 메시지 사이에 간격 추가
	
					// `users-conversation`에 날짜를 추가
					const conversationElement = document.getElementById("users-conversation");
					if (conversationElement) {
						conversationElement.appendChild(dateDiv);
					}
					// 마지막으로 출력된 날짜 업데이트
					lastDisplayedDate = currentMsgDate;
				}
	
				// 메시지 내용 처리 (msgType에 따른 처리)
				if (msgType === '1') {
					const msgDiv = document.createElement("div");
					msgDiv.className = 'user-chat-content';
					msgDiv.style.maxWidth = '300px';
					msgDiv.style.wordWrap = 'break-word';
	
					const textDiv = document.createElement("div");
					textDiv.className = 'conversation-name badge-soft-info rounded p-2';
					textDiv.textContent = msg;
	
					msgDiv.appendChild(textDiv);
					mainDiv.appendChild(msgDiv);
					mainDiv.style.textAlign = 'center';
					mainDiv.style.justifyContent = 'center';
				}
				else if (msgType === '2') {
					let participantName = "알수없음"; // 기본값 설정
					let participantProfile = '/assets/images/users/user-dummy-img.jpg'; // 기본 프로필 이미지 설정
	
					let participantFound = false; // 참가자 발견 여부
	
					participantNos.forEach((participantNo, index) => {
	
						if (participantNo.value == senderNo) { // senderNo와 일치하는지 확인
							participantName = participantNames[index]?.value || "알수없음"; // textContent로 변경
							participantProfile = participantProfiles[index]?.value || '/assets/images/users/user-dummy-img.jpg';
							participantFound = true; // 참가자를 찾았음을 기록
						}
					});
	
					if (participantFound) {
						const profileImg = document.createElement("img");
						profileImg.src = participantProfile;
						profileImg.style.width = '40px';
						profileImg.style.height = '40px';
						profileImg.style.marginRight = '10px';
						profileImg.style.borderRadius = '50%';
	
						const nameDiv = document.createElement("div");
						nameDiv.className = 'user-chat-content';
						nameDiv.textContent = participantName; // 참가자 이름 추가
						nameDiv.style.maxWidth = '300px';
						nameDiv.style.wordWrap = 'break-word';
	
						const chatContentDiv = document.createElement("div");
						chatContentDiv.className = 'user-chat-content';
						chatContentDiv.style.maxWidth = '300px';
						chatContentDiv.style.wordWrap = 'break-word';
	
						const conversationDiv = document.createElement("div");
						conversationDiv.className = 'conversation-name badge-soft-dark rounded p-2';
						conversationDiv.textContent = msg; // 메시지 내용 추가
	
						const timeDiv = document.createElement("div");
						const timeSmall = document.createElement("small");
						timeSmall.className = 'text-muted time';
						timeSmall.textContent = new Date(sendDate).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
						timeDiv.appendChild(timeSmall);
	
						if (!isSender) {
							mainDiv.style.display = 'flex';
							mainDiv.style.justifyContent = 'flex-start';
							mainDiv.appendChild(profileImg); // 프로필 이미지 추가
							chatContentDiv.appendChild(nameDiv); // 참가자 이름 추가
							chatContentDiv.appendChild(conversationDiv);
							timeDiv.style.textAlign = 'right'; // 시간 위치
							chatContentDiv.appendChild(timeDiv);
							mainDiv.appendChild(chatContentDiv);
						} else {
							mainDiv.style.display = 'flex';
							mainDiv.style.justifyContent = 'flex-end';
							chatContentDiv.style.display = 'flex';
							chatContentDiv.style.flexDirection = 'column';
							chatContentDiv.style.alignItems = 'flex-end';
							chatContentDiv.appendChild(conversationDiv);
							timeDiv.style.textAlign = 'left';
							chatContentDiv.appendChild(timeDiv);
							mainDiv.appendChild(chatContentDiv);
						}
					}
				}
	
				const conversationElement = document.getElementById("users-conversation");
				conversationElement.appendChild(mainDiv); // mainDiv 추가

				// li 요소를 마지막에 위치하게 하면서 스크롤 이동
				mainDiv.scrollIntoView({ behavior: "smooth" }); // 스크롤을 부드럽게 이동
				
			};
	
			socket.onclose = () => {
				console.log("채팅 웹소켓 연결 종료");
			};
	
		}

		
		// 메시지 전송 버튼 클릭시 이벤트 리스너
		document.getElementById('send_btn').addEventListener('click', function(event) {
			event.preventDefault();
			const msg = document.getElementById('chat_input').value;
			const obj = setMsgObj('msg', msg);
			if (chatSocket.readyState === WebSocket.OPEN) {
				chatSocket.send(JSON.stringify(obj)); // 메시지 전송
				document.getElementById('chat_input').value = ""; // 입력 필드 초기화
			} else {
				alert("채팅 연결이 끊겼습니다. 재접속하세요.");
			}
		});
	
	
		// 객체 생성자 함수
		let setMsgObj = function(chatType, chatMsg) {
			const roomNo = document.getElementById('room_no').value;
			const senderNo = document.getElementById('sender_no').value;
			const participantNo = document.getElementById('participant_no').value;
			return {
				chat_type: chatType,
				msg_content: chatMsg,
				room_no: roomNo,
				sender_no: senderNo,
				participant_no: participantNo,
				msg_type: '2'
			};
		};
		
		
		var emojiPicker = new FgEmojiPicker({
			trigger: [".emoji-btn"],
			removeOnSelection: false,
			closeButton: true,
			position: ["top", "right"],
			preFetch: true,
			dir: "/assets/js/pages/plugins/json",
			insertInto: document.querySelector(".chat-input"),
		});
		
		
		// emojiPicker position
		var emojiBtn = document.getElementById("emoji-btn");
		emojiBtn.addEventListener("click", function() {
			setTimeout(function() {
				var fgEmojiPicker = document.getElementsByClassName("fg-emoji-picker")[0];
				if (fgEmojiPicker) {
					var leftEmoji = window.getComputedStyle(fgEmojiPicker) ? window.getComputedStyle(fgEmojiPicker).getPropertyValue("left") : "";
					if (leftEmoji) {
						leftEmoji = leftEmoji.replace("px", "");
						leftEmoji = leftEmoji - 40 + "px";
						fgEmojiPicker.style.left = leftEmoji;
					}
				}
			}, 0);
		});
	
	} else{
		console.log('채팅방 선택 전, 웹소켓 대기상태');
	}
});
/* 채팅 웹소켓 JS 끝*/