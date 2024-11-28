# 🌐 WithXWork 그룹웨어 사이트
![스크린샷 2024-10-10 020547](https://github.com/user-attachments/assets/d47064ac-ae02-446a-aa68-38d2a6b2a08d)

## 🧐 프로젝트 소개
* 위드X워크는 회사 내 직원들이 원활하게 협업할 수 있도록 설계된 그룹웨어 사이트입니다.
* 직원들은 개인 업무에 도움될 뿐만 아니라 팀원들과 소통할 수 있는 다양한 서비스들을 이용할 수 있습니다.
* 관리자는 직원들의 원활한 업무를 위해 서비스 설정에 권한을 가지고, 전 직원들을 정보 조회나 재직 관리를 할 수 있습니다.

<br>

## 👩‍💻 팀원 구성
| 김란미 <br> https://github.com/KimRanmi | 심수현 <br> https://github.com/SuHyeonSim |최종우 <br> https://github.com/jongw306 |
|---|:---:|:---:|
<br>

| 허수영 <br> https://github.com/povheo| 이소은 <br> https://github.com/Lee-se0202|
|---|:---:|
<br>

## 🔨 개발환경
![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)
![](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
<br>

![](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![](https://img.shields.io/badge/Visual_Studio-5C2D91?style=for-the-badge&logo=visual%20studio&logoColor=white)
![](https://img.shields.io/badge/Eclipse-2C2255?style=for-the-badge&logo=eclipse&logoColor=white)
<br>

![](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
![](https://img.shields.io/badge/Canva-%2300C4CC.svg?&style=for-the-badge&logo=Canva&logoColor=white)
![](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![](https://img.shields.io/badge/Discord-7289DA?style=for-the-badge&logo=discord&logoColor=white)

<br>

## 📅 프로젝트 기간
![스크린샷 2024-10-10 102634](https://github.com/user-attachments/assets/a6da7c35-e701-4c57-8d43-a9df023e1e5e)

<br>

## 💡 주요 기능
* 로그인
* 마이페이지
* 캘린더
* 문서함
* 전자결재 및 양식 관리
* 주소록
* 1:1 채팅 및 그룹 채팅
* 근태 관리
* 게시판
* 회의실 예약 및 관리
* 직원 관리
* 부서 관리
* 알림
<br>

## 💻 ERD 설계
![image](https://github.com/user-attachments/assets/668b066d-b9c2-47f3-80da-a8b06f9cac28)
<br>
<br> 

* * *

<br>

# 🔔 기능 구현
## 1. 로그인과 메인 페이지
![로그인](https://github.com/user-attachments/assets/c22b7b4d-8779-4d2f-a3e7-6e02cd924fcd)
* 실행 시 처음 보이는 로그인 페이지입니다.
* ID 저장과 로그인 유지 기능을 사용할 수 있습니다.
<br>

## 2. 마이 페이지
### 2-1. 개인 정보 수정
![마이페이지1](https://github.com/user-attachments/assets/2d0d6427-30e0-4c0f-92dc-466921aaa4cb)
* 개인 정보 페이지이며 간단한 정보 수정을 할 수 있습니다.
* 또한, 서명이미지를 캔버스를 활용해 첨부가 가능합니다.
<br>

### 2-2. 비밀번호 변경
![마이페이지2](https://github.com/user-attachments/assets/6a334e08-d2dd-4b52-acfe-cf1d25469710)
* 비밀번호 변경도 가능합니다.
<br>

## 3. 캘린더
### 3-1. 일정 등록 및 수정, 삭제
![20241010_000608406](https://github.com/user-attachments/assets/a987f395-b795-4d02-8425-1454a190c82b)
* 캘린더에서는 크게 전체, 부서, 개인으로 나누어 일정을 등록할 수 있습니다.
* 또한, 일정 수정과 삭제를 할 수 있습니다.
<br>

### 3-2. 일정 조회
![캘린더 2](https://github.com/user-attachments/assets/543e2bb5-981d-42c9-a338-578b34806b52)
* 등록한 일정은 드래그를 통해 이동할 수 있으며, 월*주*일 단위로 확인할 수 있습니다.
* 메인 페이지에 있는 캘린더를 통해 일정을 확인할 수도 이동할 수도 있습니다.
<br>
  
## 4. 문서함
### 4-1. 파일 등록 및 휴지통
![문서함1](https://github.com/user-attachments/assets/61705d2a-1ba6-4895-b59b-8ba6f8280bf2)
* 파일을 등록할 수 있는 문서함입니다.
* 등록한 파일을 삭제하면 휴지통으로 이동합니다.
* 휴지통으로 이동한 파일은 복원이 가능하며, 완전히 삭제할 수도 있습니다.
<br>

### 4-2. 파일 다운로드 및 즐겨찾기
![문서함2](https://github.com/user-attachments/assets/03c75d7d-7b93-41b6-9882-3f0cac7c74a1)
* 파일은 다운로드가 가능합니다.
* 또한, 우측 아이콘을 누르면 즐겨찾기가 됩니다.
<br>

### 4-3. 문서 공유
![문서함3](https://github.com/user-attachments/assets/15ede557-4d26-4616-b805-2285b1f99270)
* 파일 등록 시 공유할 상대를 지정할 수 있고 이러한 파일은 공유 문서함으로 이동합니다.
* 공유한 직원은 공유한 문서함에 공유 받은 직원은 공유 받은 문서함으로 조회됩니다.
<br>

## 5. 전자결재
전자결재 시스템은 근태신청서, 요청서, 품의서를 기안할 수 있습니다.
<br>
### 5-1. 근태 신청서
![전자결재1](https://github.com/user-attachments/assets/a2a0c39d-5f13-404d-85cb-6d1b18503790)
* 먼저 근태 신청서는 연차와 반차는 사용할 총 일수가 표시되며, 이때 공휴일과 주말은 제외되어 계산됩니다.
* 만약 연차와 반차를 신청하는 날보다 보유한 연차가 부족하다면 "연차 부족" alert이 생성됩니다.
* 필수 항목을 빠짐없이 입력했다면 기안함으로 이동합니다.
<br>

### 5-2. 기안서 양식
![전자결재2](https://github.com/user-attachments/assets/f083c596-7163-4099-9efc-422bec520c99)
* 모든 기안서는 관리자가 설정한 양식에 따라 제출할 수 있는 내용이 달라집니다.
<br>

### 5-3. 반려 작성 및 사유 확인
![전자결재3](https://github.com/user-attachments/assets/bb10d253-46f7-4e3b-82f2-7442d20d2ec4)
* 결재자가 반려버튼을 누를 경우 반려 사유를 작성할 수 있습니다.
* 상신자는 알림을 통해 반려 사실을 확인할 수 있으며, 반려 버튼 클릭 시 반려 사유를 확인할 수 있습니다.
<br>

### 5-4. 전자결재 알림
![전자결재 알림](https://github.com/user-attachments/assets/f8d34122-af95-45c1-a3d2-65efc8064477)
* 결재해야 할 사람과 다음 결재자 그리고 마지막 결재자인 상신자에게 현 결재 상황에 대한 실시간 알림이 전송됩니다.
<br>

## 6. 주소록
![주소록](https://github.com/user-attachments/assets/a6f74d81-1a68-445c-9aa6-5178498ed418)
* 회사 내의 조직도와 직원들의 연락망을 볼 수 있는 목록 페이지입니다.
<br>

## 7. 채팅
### 7-1-1. 1:1 채팅 이용
![일대일채팅1](https://github.com/user-attachments/assets/841561b9-56bd-43f2-97ec-f1f5258cd53b)
* 채팅방 목록에서 1:1 채팅방을 선택하면 해당 채팅방으로 이동합니다.
* 1:1 채팅방을 생성 후 채팅 보낸 날짜는 상단에 위치하며 초대나 퇴장과 같은 정보성 메세지는 중앙에 위치합니다.
<br>

### 7-1-2. 1:1 채팅 방 만들기
![일대일채팅2](https://github.com/user-attachments/assets/9716fe4b-82a4-48a5-ba84-b0254b67a5b1)
* 채팅방 상단에는 상대의 프로필 사진과 이름이 출력되고, 채팅방 생성 버튼을 클릭 시 모달이 뜨며 직원들을 선택할 수 있습니다.
* 채팅 상대 선택 시 상단에 프로필 사진과 이름을 확인할 수 있습니다.
* 만약, 존재하는 1:1 채팅방이라면 해당방으로 이동합니다.
<br>

### 7-1-3. 1:1 채팅 실행화면
![일대일채팅3](https://github.com/user-attachments/assets/5cb33507-5750-41ca-9587-706f7d551b55)
* 웹소켓을 통해 실시간 양방향 소통이 가능합니다.
<br>
<br>

### 7-2. 그룹채팅
### 7-2-1. 그룹 채팅 이용 및 방 만들기
![그룹채팅1](https://github.com/user-attachments/assets/c13c0a67-fc6b-4814-96c9-988ee920e11b)
* 채팅방 목록에서 하단에 있는 그룹 채팅방을 선택하면 해당 채팅방으로 이동합니다.
* 채팅방에서 나가면 상대방의 채팅방에서 누가 나갔는지 알 수 있습니다.
<br>

### 7-2-2. 그룹 채팅 실행 화면 
![그룹채팅2](https://github.com/user-attachments/assets/848d95a7-9793-4466-ac6f-520c42eeff37)
* 그룹 채팅방 생성은 참가자를 여러 명 선택하면 자동으로 입력창이 열리고, 직접 채팅방 이름을 설정할 수 있습니다.
* 입력을 마친 후 초대하기 버튼을 누르면 개설한 채팅방으로 이동합니다.
<br>

![그룹채팅3](https://github.com/user-attachments/assets/ff482783-ac6f-4685-9f7a-5d6c938431d2)
* 채팅 메세지를 보내면 그룹 채팅방에 속한 직원들에게 전달되며 실시간 그룹 채팅 가능합니다.
* 상대방의 채팅방에서 보면 채팅방은 유지되면서 나간 사람이 누가 있는지 확인이 가능합니다.
<br>

## 8. 근태관리
### 8-1. 출퇴근 관리
![근태관리1](https://github.com/user-attachments/assets/a32ab353-0c74-4181-8211-cc19b07f6624)
* 기간 별로 혹은 월 별로 출퇴근 시간과 근무 시간, 근무 상태를 조회할 수 있습니다.
<br>

### 8-2. 근무 시간 관리
![근태관리2](https://github.com/user-attachments/assets/ded53b0a-974c-4837-ab2c-65c0fba7cfc2)
* 메인페이지에 있는 출퇴근 버튼을 누르면 출퇴근 관리에 반영됩니다.
* 근무내역 관리에서는 날짜별 초과 근무 시간, 총 근무시간을 조회할 수 있습니다.
<br>

### 8-3. 연차 관리
![근태관리3](https://github.com/user-attachments/assets/4121912b-d96d-4e6a-8ba9-77f2a81c3190)
* 근태 신청서가 승인되면 근태관리에서 자신의 연차 정보를 확인할 수 있습니다.
<br>

## 9. 게시판
### 9-1. 글 등록 및 수정, 삭제
![게시판1](https://github.com/user-attachments/assets/57d91192-5a29-4eba-ad8f-75171cb27040)
* 제목과 내용을 입력하고 파일은 여러 개를 지정하여 첨부할 수 있습니다.
* 수정이 완료되면 수정일이 생기며, 수정된 내용을 조회할 수 있습니다. 
* 작성한 본인에게만 수정, 삭제 버튼이 보이며 게시글 수정 시 첨부한 파일을 개별 삭제할 수 있습니다. 
<br>

### 9-2. 권한 설정
![게시판2](https://github.com/user-attachments/assets/f9070b2a-5318-4503-b2c2-dfced3dbc6d5)
* 게시판에서 글을 작성할 때는 조회 가능한 부서와 직급을 선택하여 접근 권한을 부여할 수 있습니다.
* 권한 설정 선택 후 아무도 선택하지 않으면 유효성 검사 메세지가 출력됩니다.
<br>

## 10. 회의실 예약
### 10-1. 회의실 장소 및 시간 예약
![회의실 예약1](https://github.com/user-attachments/assets/8c2d134f-1cca-440c-a630-f3c3a2a507a1)
* 관리자가 설정해 놓은 회의실을 직원이 예약할 수 있습니다.
* 원하는 회의실 장소와 시간대를 설정하여 예약을 합니다.
<br>

### 10-2. 예약 변경 및 취소
![회의실 예약2](https://github.com/user-attachments/assets/d57ba1d1-4537-4137-8790-ffa28f732c10)
* 예약 내역에서 시간대를 변경할 수도 예약 취소도 가능합니다.
<br>

## 11. 직원 관리
### 11-1. 신규 직원 등록
![신규직원](https://github.com/user-attachments/assets/1625c38f-69ac-4661-8326-93b1f16ce6cf)
* 관리자 신규 직원을 등록하면 직원은 로그인 계정이 생성됩니다.
<br>

### 11-2. 직원 정보 상세 조회 및 임시 비밀번호 발급, 정보 수정
![직원 상세](https://github.com/user-attachments/assets/2d2f5982-000a-45cd-8590-1680d58406ff)
* 목록에서 특정 직원을 선택하면 상세 정보 페이지가 나옵니다.
* 임시 비밀번호 발급을 통해 기본 비밀번호 'work1234'로 변경됩니다.
* 수정 버튼을 통해 직원의 이름, 부서, 직급을 수정할 수 있습니다.
<br>

### 11-3. 직원 퇴사 관리
![퇴사관리](https://github.com/user-attachments/assets/cc28c0bd-db31-451b-9177-8e78f08a5979)
* 퇴사 관리 탭을 누르면 직원을 퇴사 처리할 수 있습니다.
* 퇴사일은 입사일 이전 날짜로 선택할 수 없도록 설정했습니다.
<br>

## 12. 부서 관리
### 12-1. 조직도 및 부서 조회, 추가
![부서관리1](https://github.com/user-attachments/assets/4536840a-e88f-4261-8d0e-4b46a9417a65)
* 조직도와 등록 되어 있는 부서 목록을 조회할 수 있습니다.
* 부서 추가는 기존에 없는 부서명으로 입력해야 등록됩니다.
<br>

### 12-2. 부서명 수정 및 삭제
![부서관리2](https://github.com/user-attachments/assets/d067631a-6c24-4eed-8c7f-891316abbd55)
* 부서명을 수정하고 싶은 부서를 선택하여 재입력합니다.
* 부서를 삭제할 때는 부서 내에 직원이 한 명도 없어야 가능합니다.
<br>


## 13. 회의실 관리
### 13-1. 회의실 추가
![회의실 관리1](https://github.com/user-attachments/assets/1adb7f15-5d1a-4f6a-b6ba-da841fd62b43)
* 직원들이 예약할 수 있도록 회의실 장소와 시간대를 지정하여 추가합니다.
* 회의실에 대한 이미지도 첨부할 수 있습니다.
<br>

### 13-2. 회의실 정보 수정 및 삭제
![회의실 관리2](https://github.com/user-attachments/assets/b610742d-fc21-4160-ae50-7acb23370d4a)
* 이미 등록한 회의실 정보를 수정하거나 삭제할 수 있습니다.
<br>

# 여기까지 코드널팀의 위드x워크 사이트 소개였습니다. 감사합니다.
