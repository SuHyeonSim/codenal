/*
Template Name: Velzon - Admin & Dashboard Template
Author: Themesbrand
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Ecommerce product Details Js File
*/

var productNavSlider = new Swiper(".product-nav-slider", {
	loop: false,
	spaceBetween: 10,
	slidesPerView: 4,
	freeMode: true,
	watchSlidesProgress: true,
});
var productThubnailSlider = new Swiper(".product-thumbnail-slider", {
	loop: false,
	spaceBetween: 24,
	navigation: {
		nextEl: ".swiper-button-next",
		prevEl: ".swiper-button-prev",
	},
	thumbs: {
		swiper: productNavSlider,
	},
});

for(let i=0; i<document.getElementById("size").value; i++){
	
	var date_range = document.getElementsByClassName('datePicker')[i].flatpickr({
						dateFormat: "Y-m-d",
			            minDate: "today",
			            inline:"true",
			            defaultDate: "today",
			            disable: [
							function(date){
								return (date.getDay() == 0 || date.getDay() == 6);
							}
						]
			        });
}

const csrfToken = document.getElementById('csrf_token').value;

fetch('/meetingRoom', {
	method: 'POST',
	headers: {
		'X-CSRF-TOKEN': csrfToken
	}
})
	.then(response => response.json())
	.then(data => {
		for(let i=0; i<= data.meetingRoom.length; i++){
			
		document.getElementsByClassName("list-group")[0].innerHTML += "<a class='list-group-item list-group-item-action nav-link show'\
									id='custom-v-pills-home-tab' data-bs-toggle='pill'\
									href='#meetingRoomDetail"+data.meetingRoom[i].meeting_room_no+"' role='tab'\
									aria-controls='meetingRoomDetail"+data.meetingRoom[i].meeting_room_no+"'>\
									<div class='d-flex align-items-center'>\
										<div class='flex-shrink-0'>\
											<div class='rounded' style='width: 150px; height: 100px; background-image: url(/meetingRoomImg/"+data.meetingRoom[i].meeting_room_img+"); background-repeat: no-repeat; background-size: cover; background-position: center'>\
											</div>\
										</div>\
										<div class='flex-grow-1 ms-3 mb-5'>\
											<p class='card-title mb-2' style='font-weight: bold;'>"+data.meetingRoom[i].meeting_room_name+"</p>\
											<p class='mb-0'>"+data.meetingRoom[i].meeting_room_place+"</p>\
										</div>\
									</div>\
								</a> "
		document.getElementsByClassName("list-group")[0].firstElementChild.className += ' active';
		
		document.getElementsByClassName("meetingRoomActive")[0].className += ' show active';
		
			/*for (let j = 1; j < data.meetingRoom.length+1; j++) {

				const formData = document.getElementById("meetingRoomForm"+j);
				formData.addEventListener('submit', (e) => {
					e.preventDefault();

					let vali_check = false;
					let vali_text = "";
					if (formData.reserve_date.value == "") {
						vali_text += '예약 날짜를 선택하세요.';
						formData.reserve_date.focus();
					} else if (formData.reserve_time[0].checked == false) {
						vali_text += '예약 시간을 선택하세요.';
						formData.reserve_time[0].focus();
					} else {
						vali_check = true;
					}
					if (vali_check == false) {
						alert(vali_text);
					} else {

						let meetingRoomNo = document.getElementById("reserve_meeting_room_no").value;

						let checkTime = [];

						for (let i = 0; i < formData.reserve_time.length; i++) {
							if (formData.reserve_time[i].checked == true) {
								checkTime.push(formData.reserve_time[i].value);
							}
						}

						console.log(meetingRoomNo);

						const payload = new FormData(formData);
						console.log(payload);
						fetch('/meetingRoomReserve', {
							method: 'POST',
							headers: {
								'X-CSRF-TOKEN': csrfToken
							},
							body: {
								'reserveDate': formData.reserve_date.value,
								'reserveTime': checkTime
							}
						})
							.then(response => response.json())
							.then(data => {

								alert('성공');
							})
					}

				})

			}*/
		
		}
	})
	
	
/*fetch('/meetingRoomDetail', {
	method: 'POST',
	headers: {
		'X-CSRF-TOKEN': csrfToken
	}
})
	.then(response => response.json())
	.then(data => {
		
		console.log(data.meetingRoom.length);
		for(let i=0; i<= data.meetingRoom.length; i++){
			
			document.getElementsByClassName("meetingRoom")[0].innerHTML += "<div class='card-header align-items-center d-flex'>\
							<h4 class='card-title mb-0 flex-grow-1'>회의실 A</h4>\
						</div>\
						<div class='card-body'>\
							<div class='mx-n3'>\
								<div data-simplebar style='max-height: 640px;' class='px-3'>\
									<div class='row gx-lg-4'>\
										<div class='col-xl-12 mb-4'>\
											<img class='rounded' alt=''  width='100%' src='assets/images/profile-bg.jpg'>\
										</div>\
										<div class='col-xl-12'>\
											<div class='mt-xl-0 mt-5'>\
												<div class='d-flex'>\
													<div class='flex-grow-1'>\
														<h4>회의실A</h4>\
														<div class='hstack gap-3 flex-wrap'>\
															<div>\
																<a href='#' class='text-primary d-block'> 3층 301호 </a>\
															</div>\
															<div class='vr'></div>\
															<div class='text-muted'>기자재 : <span class='text-body fw-medium'> TV, 화이트 보드, 빔 프로젝터 </span>\
															</div>\
														</div>\
													</div>\
													<div class='flex-shrink-0'>\
														<div>\
															<a href='/apps-ecommerce-add-product'\
																class='btn btn-light' data-bs-toggle='tooltip'\
																data-bs-placement='top' title='Edit'>\
																<i class='ri-pencil-fill align-bottom'></i>\
															</a>\
														</div>\
													</div>\
												</div>\
												<form id='meetingRoomForm'>\
													<div class='row'>\
														<div class='col-lg-7 mb-4'>\
															<div class='mt-3 upcoming-scheduled rangeMode animate arrowTop arrowLeft open'>\
																<input type='text' class='form-control'\
																	data-provider='flatpickr' data-date-format='d M, Y'\
																	data-deafult-date='today' data-minDate='05 09,2024'\
																	data-maxDate='30 09,2024' data-inline-date='true'>\
															</div>\
														</div>\
														<div class='col-xl-5  mt-4'>\
															<h5 class='fs-16 fw-bold'>오전</h5>\
															<div class='mb-3'>\
																<input type='checkbox' class='btn-check ' id='btnradio1'\
																	autocomplete='off' value='9:00~9:50'> <label\
																	class='btn btn-outline-secondary' for='btnradio1'\
																	style='width: 125px'> 9 : 00 ~ 9 : 50 </label>\
															</div>\
															<h5 class='fs-16 fw-bold'>오후</h5>\
															<div class='mb-3'>\
																<input type='checkbox' class='btn-check' id='btnradio4'\
																	autocomplete='off' value='1:00~1:50'> <label\
																	class='btn btn-outline-secondary' for='btnradio4'\
																	style='width: 125px'> 1 : 00 ~ 1 : 50 </label>\
															</div>\
														</div>\
														<input type='submit' class='btn btn-primary ' value='회의실 예약'>\
													</div>\
												</form>\
											</div>\
										</div>\
									</div>\
								</div>\
							</div>\
						</div>";
		}
	})*/
	

let reserveDate = "";

const reserveDateCheck=(reserveDateCheck)=> {
	reserveDate = reserveDateCheck.value.toString();
	
}

const reserve=(event)=> {
	/*event.preventDefault();*/
	
	let listTime = [];
		
		for(let i=0; i<document.getElementsByClassName("reserve_time").length; i++){
			if(document.getElementsByClassName("reserve_time")[i].checked == true){
				listTime.push(document.getElementsByClassName("reserve_time")[i].value);
			}
		}
	
	
		let dateFormat = new Date();
		let nowDay = new Date();
		dateFormat.setDate(document.getElementById("meeting_room_reserve_date").value)
	let vali_check = false;
	let vali_text = "";
	if (listTime == "") {
		vali_text += '예약 시간을 선택하세요.';
	} else {
		vali_check = true;
	}
	if(vali_check == false){
		alert(vali_text);
	} else{
		
		let meetingRoomNo = event.value;
		
		const empId = document.getElementById("empId").value;
		
		
		/*let reserveDate = dateFormat.getFullYear()+"-"+(dateFormat.getMonth()+1).toString().padStart(2, '0')+"-"+dateFormat.getDate().toString().padStart(2, '0');*/
		
		/*const payload = new FormData(formData);
		console.log(reserveDate);*/
		/*fetch('/meetingRoomReserve',{
			method:'POST',
			headers: {
				'X-CSRF-TOKEN': csrfToken
			},
			body: payload,
		})
		.then(response => response.json())
		.then(data=>{
			
			alert('성공');
		})*/
		
		const xhr = new XMLHttpRequest();
		xhr.open("post", "/meetingRoomReserve", true);
		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				alert('예약 되었습니다.');
				location.href="/apps-meeting-room-reserve-list";
			}
		}
		const header = document.getElementById("_csrf_header").value;
		xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		xhr.setRequestHeader(header, csrfToken);
		xhr.send("meetingRoomNo="+meetingRoomNo+"&reserveDate="+reserveDate+"&reserveTime="+listTime+"&reserveEmpId="+empId);
	}

}

const deleteMeetingRoom=(event) => {
	/*event.preventDefault();*/
	
	let meetingRoomNo = event.value;
	if(confirm("해당 회의실을 삭제하시겠습니까?")){
		fetch('/meetingRoomDelete/'+meetingRoomNo,{
			method:'delete',
			headers: {
				'X-CSRF-TOKEN': csrfToken
			}
		})
		.then(response => response.json())
		.then(data=>{
			if(data != null){
				alert(data.msg);
				location.reload();
			}
		})
		
	}
}


