/*
Template Name: Velzon - Admin & Dashboard Template
Author: Themesbrand
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Calendar init js
*/


var start_date = document.getElementById("event-start-date");
var timepicker1 = document.getElementById("timepicker1");
var timepicker2 = document.getElementById("timepicker2");
const csrfToken = document.getElementById('csrf_token').value;
const writer = document.getElementById("empId").value;
var date_range = null;
var T_check = null;
var eventContent = null;
var defaultEvents = [];

const fetchEventList = () => {
		return new Promise((resolve,reject)=>{
			fetch('/eventList'+writer, {
				method: 'POST',
				headers: {
		                'X-CSRF-TOKEN': csrfToken
		            }
			})
			.then(response => response.json())
			.then(data => {
				console.log(data);
				defaultEvents = [];
				for(let i=0; i<data.eventList.length; i++){
					switch (data.eventList[i].calendar_schedule_category) {
						case 1 : data.eventList[i].calendar_schedule_category = 'bg-soft-success'; break;
						case 2 : data.eventList[i].calendar_schedule_category = 'bg-soft-info'; break;
						case 3 : data.eventList[i].calendar_schedule_category = 'bg-soft-warning'; break;
						case 4 : data.eventList[i].calendar_schedule_category = 'bg-soft-primary'; break;
						case 5 : data.eventList[i].calendar_schedule_category = 'bg-soft-danger'; break;
					}
						
						data[i] = {
							id: data.eventList[i].calendar_schedule_no,
					        title: data.eventList[i].calendar_schedule_title,
					        start: new Date(data.eventList[i].calendar_schedule_start_date),
					        end: new Date(data.eventList[i].calendar_schedule_end_date),
					        className: data.eventList[i].calendar_schedule_category,
					        location: data.eventList[i].calendar_schedule_location,
					        description: data.eventList[i].calendar_schedule_content,
					        writer: data.eventList[i].calendar_schedule_writer
						}
					
					defaultEvents[i] = data[i];
				}
				resolve(defaultEvents);
			})
		})
	}
	
	

document.addEventListener("DOMContentLoaded", function () {
    flatPickrInit();
    var addEvent = new bootstrap.Modal(document.getElementById('event-modal'), {
        keyboard: false
    });
    document.getElementById('event-modal');
    var modalTitle = document.getElementById('modal-title');
    var formEvent = document.getElementById('form-event');
    var selectedEvent = null;
    var forms = document.getElementsByClassName('needs-validation');
    /* initialize the calendar */

    var date = new Date();
    var d = date.getDate();
    var m = date.getMonth();
    var y = date.getFullYear();
    var Draggable = FullCalendar.Draggable;
    var externalEventContainerEl = document.getElementById('external-events');
	var eventSelect = [];
	/*var defaultEvents = [];*/
	/*defaultEvents = [
		{
			id: 10,
            title: "추석",
            start: new Date("2024-09-16"),
            end: new Date("2024-09-19"),
            className: "bg-soft-danger",
            allDay: true
		},
		
	]*/
	/*if(document.getElementById("sss").checked){
		console.log(document.getElementById("sss").value);
		for(let i=0; i<=defaultEvents.length; i++){
			if(defaultEvents[i].title == document.getElementById("sss").value){
				defaultEvents[i] = '';
			}
		}
	}*/
	
	/*const fetchEventList = () => {
		return new Promise((resolve,reject)=>{
			fetch('/eventList', {
				method: 'POST'
			})
			.then(response => response.json())
			.then(data => {
				defaultEvents = [];
				for(let i=0; i<data.eventList.length; i++){
					
					console.log(data.eventList[i].calendar_schedule_category);
					switch (data.eventList[i].calendar_schedule_category) {
						case 1 : data.eventList[i].calendar_schedule_category = 'bg-soft-success'; break;
						case 2 : data.eventList[i].calendar_schedule_category = 'bg-soft-info'; break;
						case 3 : data.eventList[i].calendar_schedule_category = 'bg-soft-warning'; break;
						case 4 : data.eventList[i].calendar_schedule_category = 'bg-soft-primary'; break;
					}
					console.log(data.eventList[i].calendar_schedule_category);

					data[i] = {
						id: data.eventList[i].calendar_schedule_no,
				        title: data.eventList[i].calendar_schedule_title,
				        start: new Date(data.eventList[i].calendar_schedule_start_date),
				        end: new Date(data.eventList[i].calendar_schedule_end_date),
				        className: data.eventList[i].calendar_schedule_category,
				        location: data.eventList[i].calendar_schedule_location,
				        description: data.eventList[i].calendar_schedule_content,
				        writer: data.eventList[i].calendar_schedule_writer,
				        color: data.eventList[i].calendar_schedule_color
					}
					defaultEvents[i] = data[i];
					
				}
				resolve(defaultEvents);
			})
		})
	}*/
	
	
	
	/*function eventList(){
		return new Promise(function(resolve,reject){
			
		fetch('/eventList', {
			method: 'POST'
		})
		.then(response => response.json())
		.then(data => {
			for(let i=0; i<=data.eventList.length; i++){
				data[i] = {
					id: data.eventList[i].calendar_schedule_no,
					category: data.eventList[i].calendar_schedule_category,
			        title: data.eventList[i].calendar_schedule_title,
			        start: data.eventList[i].calendar_schedule_start_date,
			        end: data.eventList[i].calendar_schedule_end_date,
			        className: 'bg-soft-primary',
			        location: data.eventList[i].calendar_schedule_location,
			        description: data.eventList[i].calendar_schedule_content,
			        writer: data.eventList[i].calendar_schedule_writer,
			        color: data.eventList[i].calendar_schedule_color
				}
				defaultEvents[i] = data[i];
			}
			resolve(defaultEvents);
		})
		})
	}
	eventList();*/
	
	/*	console.log(defaultEvents[0]);
	for(let i=0; i<=5; i++){
		
		console.log(defaultEvents[i]);
		defaultEvents[i] = {
				id: defaultEvents[i],
				category: 1,
				title: "title1",
				start: "2024-09-0"+(i+1)+"T00:00:00",
				end: "2024-09-0"+(i+3)+"T00:00:00",
				className: 'bg-soft-primary',
				location: "location",
				description: "content1",
				writer: 12345678,
				color: null
			}
	}*/
	


    // init draggable
    new Draggable(externalEventContainerEl, {
        itemSelector: '.external-event',
        eventData: function (eventEl) {
			let categoryNo = 0;
			switch (eventEl.getAttribute('data-class')) {
				case 'bg-soft-success': categoryNo = 1; break; /*개인일정*/
				case 'bg-soft-info': categoryNo = 2; break; /*부서일정*/
				case 'bg-soft-warning': categoryNo = 3; break; /*전체일정*/
				case 'bg-soft-primary': categoryNo = 4; break; /*연차일정*/
			}
			startDate = new Date().getFullYear()+'-'+(new Date().getMonth()+1).toString().padStart(2, '0')+'-'+new Date().getDate().toString().padStart(2, '0')
				+' '+new Date().getHours().toString().padStart(2, '0')+':'+new Date().getMinutes().toString().padStart(2, '0');
			
			const xhr = new XMLHttpRequest();
			xhr.open("post", "/create/event", true);
			xhr.onreadystatechange = function() {
				if (xhr.readyState == 4 && xhr.status == 200) {
				}
			}
			const header = document.getElementById("_csrf_header").value;
			xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			xhr.setRequestHeader(header, csrfToken);
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			let content = " ";
			xhr.send("category="+categoryNo+"&title="+eventEl.innerText+"&writer="+writer+"&start_date="+startDate+"&content="+content);
            return {
                id: Math.floor(Math.random() * 11000),
                title: eventEl.innerText,
                allDay: true,
                start: new Date(),
                writer: writer,
                description: ' ',
                className: eventEl.getAttribute('data-class')
            };
        }
    });

    var calendarEl = document.getElementById('calendar');

    function addNewEvent(info) {
        document.getElementById('form-event').reset();
        document.getElementById('btn-delete-event').setAttribute('hidden', true);
        addEvent.show();
        formEvent.classList.remove("was-validated");
        formEvent.reset();
        selectedEvent = null;
        modalTitle.innerText = '일정 추가';
        newEventData = info;
        document.getElementById("edit-event-btn").setAttribute("data-id", "new-event");
        document.getElementById('edit-event-btn').click();
        document.getElementById("edit-event-btn").setAttribute("hidden", true);
    }

    function getInitialView() {
        if (window.innerWidth >= 768 && window.innerWidth < 1200) {
            return 'timeGridWeek';
        } else if (window.innerWidth <= 768) {
            return 'listMonth';
        } else {
            return 'dayGridMonth';
        }
    }

    var eventCategoryChoice = new Choices("#event-category", {
        searchEnabled: false
    });
    fetchEventList().then(function(data){
		console.log(data);
    var calendar = new FullCalendar.Calendar(calendarEl, {
        timeZone: 'local',
        editable: true,
        droppable: true,
        selectable: true,
        navLinks: true,
        
        initialView: getInitialView(),
        themeSystem: 'bootstrap',
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
        },
        windowResize: function (view) {
            var newView = getInitialView();
            calendar.changeView(newView);
		},
        eventResize: function(info) {
            var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                return x.id == info.event.id
            });
            if (defaultEvents[indexOfSelectedEvent]) {
				
            }
            console.log(defaultEvents);
            upcomingEvent(defaultEvents);
        },
        eventClick: function (info) {
            document.getElementById("edit-event-btn").removeAttribute("hidden");
            document.getElementById('btn-save-event').setAttribute("hidden", true);
            document.getElementById("edit-event-btn").setAttribute("data-id", "edit-event");
            document.getElementById("edit-event-btn").innerHTML = "수정";
            eventClicked();
            flatPickrInit();
            flatpicekrValueClear();
            addEvent.show();
            formEvent.reset();
            selectedEvent = info.event;
            eventContent = selectedEvent._def;

            // First Modal
            document.getElementById("modal-title").innerHTML = "";
            document.getElementById("event-location-tag").innerHTML = selectedEvent.extendedProps.location === undefined ? "장소 없음" : selectedEvent.extendedProps.location;
            document.getElementById("event-description-tag").innerHTML = selectedEvent.extendedProps.description === undefined ? "내용 없음" : selectedEvent.extendedProps.description;

            // Edit Modal
            document.getElementById("event-title").value = selectedEvent.title;
            document.getElementById("event-location").value = selectedEvent.extendedProps.location === undefined ? "장소 없음" : selectedEvent.extendedProps.location;
            document.getElementById("event-description").value = selectedEvent.extendedProps.description === undefined ? "내용 없음" : selectedEvent.extendedProps.description;
            document.getElementById("eventid").value = selectedEvent.id;

            if (selectedEvent.classNames[0]) {
                eventCategoryChoice.destroy();
                eventCategoryChoice = new Choices("#event-category", {
                    searchEnabled: false
                });
                eventCategoryChoice.setChoiceByValue(selectedEvent.classNames[0]);
            }
            var st_date = selectedEvent.start;
            var ed_date = selectedEvent.end;

            var date_r = function formatDate(date) {
                var d = new Date(date),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear();
                if (month.length < 2)
                    month = '0' + month;
                if (day.length < 2)
                    day = '0' + day;
                return [year, month, day].join('-');
            };
            var updateDay = null
            if(ed_date != null){
                var endUpdateDay = new Date(ed_date);
                updateDay = endUpdateDay.setDate(endUpdateDay.getDate() - 1);
            }
            
            var r_date = ed_date == null ? (str_dt(st_date)) : (str_dt(st_date)) + ' ~ ' + (str_dt(updateDay));
            var er_date = ed_date == null ? (date_r(st_date)) : (date_r(st_date)) + ' ~ ' + (date_r(updateDay));

            flatpickr(start_date, {
                defaultDate: er_date,
                altInput: true,
                altFormat: "j F Y",
                dateFormat: "Y-m-d",
                mode: ed_date !== null ? "range" : "range",
                onChange: function (selectedDates, dateStr, instance) {
                    var date_range = dateStr;
                    var dates = date_range.split("to");
                    if (dates.length > 1) {
                        document.getElementById('event-time').setAttribute("hidden", true);
                    } else {
                        document.getElementById("timepicker1").parentNode.classList.remove("d-none");
                        document.getElementById("timepicker1").classList.replace("d-none", "d-block");
                        document.getElementById("timepicker2").parentNode.classList.remove("d-none");
                        document.getElementById("timepicker2").classList.replace("d-none", "d-block");
                        document.getElementById('event-time').removeAttribute("hidden");
                    }
                },
            });
            document.getElementById("event-start-date-tag").innerHTML = r_date;

            var gt_time = getTime(selectedEvent.start);
            var ed_time = getTime(selectedEvent.end);

            if (gt_time == ed_time) {
                document.getElementById('event-time').setAttribute("hidden", true);
                flatpickr(document.getElementById("timepicker1"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                });
                flatpickr(document.getElementById("timepicker2"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                });
            } else {
                document.getElementById('event-time').removeAttribute("hidden");
                flatpickr(document.getElementById("timepicker1"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                    defaultDate: gt_time
                });

                flatpickr(document.getElementById("timepicker2"), {
                    enableTime: true,
                    noCalendar: true,
                    dateFormat: "H:i",
                    defaultDate: ed_time
                });
                document.getElementById("event-timepicker1-tag").innerHTML = tConvert(gt_time);
                document.getElementById("event-timepicker2-tag").innerHTML = tConvert(ed_time);
                
                
                var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                    return x.id == selectedEvent.id
                });
                let eventWriter = defaultEvents[indexOfSelectedEvent].writer;
                console.log(eventWriter+'확인'+writer);
                
                if(eventWriter != writer){
					document.getElementById("edit-event-btn").style.display = "none";
					document.getElementById("btn-delete-event").style.display = "none";
					
				} else if(eventWriter == writer){
					document.getElementById("edit-event-btn").style.display = "";
					document.getElementById("btn-delete-event").style.display = "";
				}
                
                fetch('/eventWriter'+eventWriter, {
				method: 'POST',
				headers: {
		                'X-CSRF-TOKEN': csrfToken
		            },
				})
				.then(response => response.json())
				.then(data => {
					let empName = data.name;
					let empDept = data.dept;
					let empJob = data.job;
					
		            document.getElementById("event-writer").innerHTML = empDept+' '+empName+' '+empJob;
				})
                
                
            }
            newEventData = null;
            modalTitle.innerText = selectedEvent.title;

            // formEvent.classList.add("view-event");
            document.getElementById('btn-delete-event').removeAttribute('hidden');
        },
        dateClick: function (info) {
            addNewEvent(info);
        },
        events: defaultEvents,
        eventReceive: function (info) {
            var newid = parseInt(info.event.id);
            var newEvent = {
                id: newid,
                title: info.event.title,
                start: info.event.start,
                allDay: info.event.allDay,
                className: info.event.classNames[0]
            };
            defaultEvents.push(newEvent);
            upcomingEvent(defaultEvents);
        },
        eventDrop: function (info) {
            var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                return x.id == info.event.id
            });
            if (defaultEvents[indexOfSelectedEvent]) {
				defaultEvents[indexOfSelectedEvent].category = info.event.category;
                defaultEvents[indexOfSelectedEvent].title = info.event.title;
                defaultEvents[indexOfSelectedEvent].start = info.event.start;
                defaultEvents[indexOfSelectedEvent].end = (info.event.end) ? info.event.end : null;
                defaultEvents[indexOfSelectedEvent].allDay = info.event.allDay;
                defaultEvents[indexOfSelectedEvent].className = info.event.classNames[0];
                defaultEvents[indexOfSelectedEvent].description = (info.event._def.extendedProps.description) ? info.event._def.extendedProps.description : '';
                defaultEvents[indexOfSelectedEvent].location = (info.event._def.extendedProps.location) ? info.event._def.extendedProps.location : '';
                defaultEvents[indexOfSelectedEvent].writer = info.event.writer;
            }
            upcomingEvent(defaultEvents);
        }
    });

    calendar.render();
    upcomingEvent(defaultEvents);
    /*Add new event*/
    // Form to add new event
    formEvent.addEventListener('submit', function (ev) {
        ev.preventDefault();
        
        var updatedTitle = document.getElementById("event-title").value;
        var updatedCategory = document.getElementById('event-category').value;
        
        let categoryNo = 0;
        switch(updatedCategory){
			case 'bg-soft-success' : categoryNo = 1; break; /*개인일정*/
			case 'bg-soft-info' : categoryNo = 2; break; /*부서일정*/
			case 'bg-soft-warning' : categoryNo = 3; break; /*전체일정*/
			case 'bg-soft-primary' : categoryNo = 4; break; /*연차일정*/
		}
        var start_date = (document.getElementById("event-start-date").value).split("to");
        var updateStartDate = new Date(start_date[0].trim());

        var newdate = new Date(start_date[1]);
        newdate.setDate(newdate.getDate() + 1);

        var updateEndDate = (start_date[1]) ? newdate : '';
        
        var start_time = (document.getElementById("timepicker1").value);
        var end_time = (document.getElementById("timepicker2").value);

        var end_date = null;
        var event_location = document.getElementById("event-location").value;
        var eventDescription = document.getElementById("event-description").value;
        var eventid = document.getElementById("eventid").value;
        console.log(eventid);
        var all_day = false;
        if (start_date.length > 1) {
			
            var end_date = new Date(start_date[1]);
            end_date.setDate(end_date.getDate() + 1);
            start_date = new Date(start_date[0]);
            all_day = true;
        } else {
            var e_date = start_date;
            start_time = (document.getElementById("timepicker1").value).trim();
            end_time = (document.getElementById("timepicker2").value).trim();
			updateStartDate.setHours(start_time.substring(0,2));
			updateStartDate.setMinutes(start_time.substring(3));
			
			
			updateEndDate = new Date(updateStartDate.getFullYear(),updateStartDate.getMonth(),updateStartDate.getDate(),
			end_time.substring(0,2),end_time.substring(3));
			
            start_date = new Date(start_date + "T" + start_time);
            end_date = new Date(e_date + "T" + end_time);
        }
        var e_id = defaultEvents.length + 1;
         
	    	
	        const payload = {
				calendar_schedule_category : updatedCategory,
				calendar_schedule_title : updatedTitle,
				calendar_schedule_location : event_location,
				calendar_schedule_content : eventDescription,
				calendar_schedule_writer : 1,
				calendar_schedule_start_date : updateStartDate,
				calendar_schedule_end_date : updateEndDate
			}
	        console.log('작성자'+writer);
	        if(eventContent == null){
				console.log('생성');
				const xhr = new XMLHttpRequest();
				xhr.open("post","/create/event",true);
				xhr.onreadystatechange = function() {
					if(xhr.readyState == 4 && xhr.status == 200){ // 200: 정상적으로 작동한다는 뜻
						/*alert('일정이 추가되었습니다.');*/
					}
				}
				const header = document.getElementById("_csrf_header").value;
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				xhr.setRequestHeader(header, csrfToken);
				updateStartDate = updateStartDate.getFullYear()+'-'+(updateStartDate.getMonth()+1).toString().padStart(2, '0')+'-'+updateStartDate.getDate().toString().padStart(2, '0')
				+' '+updateStartDate.getHours().toString().padStart(2, '0')+':'+updateStartDate.getMinutes().toString().padStart(2, '0');
				
				console.log(updateStartDate);
				if(updateEndDate != null){
					updateEndDate = updateEndDate.getFullYear()+'-'+(updateEndDate.getMonth()+1).toString().padStart(2, '0')+'-'+updateEndDate.getDate().toString().padStart(2, '0')
					+' '+updateEndDate.getHours().toString().padStart(2, '0')+':'+updateEndDate.getMinutes().toString().padStart(2, '0');
					
					xhr.send("category="+categoryNo+"&title="+updatedTitle+"&location="+event_location
					+"&content="+eventDescription+"&writer="+writer+"&start_date="+updateStartDate+"&end_date="+updateEndDate+"&token="+csrfToken);
				}
			} else {
				console.log('수정');
				const xhr = new XMLHttpRequest();
				xhr.open("post","/modify/event",true);
				xhr.onreadystatechange = function() {
					if(xhr.readyState == 4 && xhr.status == 200){ // 200: 정상적으로 작동한다는 뜻
						
					}
				}
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				const header = document.getElementById("_csrf_header").value;
				xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				xhr.setRequestHeader(header, csrfToken);
				updateStartDate = updateStartDate.getFullYear()+'-'+(updateStartDate.getMonth()+1).toString().padStart(2, '0')+'-'+updateStartDate.getDate().toString().padStart(2, '0')
				+' '+updateStartDate.getHours().toString().padStart(2, '0')+':'+updateStartDate.getMinutes().toString().padStart(2, '0');
				
				console.log(eventContent.publicId);
				if(updateEndDate != null){
					updateEndDate = updateEndDate.getFullYear()+'-'+(updateEndDate.getMonth()+1).toString().padStart(2, '0')+'-'+updateEndDate.getDate().toString().padStart(2, '0')
					+' '+updateEndDate.getHours().toString().padStart(2, '0')+':'+updateEndDate.getMinutes().toString().padStart(2, '0');
					
					xhr.send("eventId="+eventContent.publicId+"&category="+categoryNo+"&title="+updatedTitle+"&location="+event_location
					+"&content="+eventDescription+"&writer="+writer+"&start_date="+updateStartDate+"&end_date="+updateEndDate);
				}
			}
				
				
				

        // validation
        if (forms[0].checkValidity() === false) {
            forms[0].classList.add('was-validated');
        } else {
            if (selectedEvent) {
                selectedEvent.setProp("id", eventid);
                selectedEvent.setProp("category", categoryNo);
                selectedEvent.setProp("title", updatedTitle);
                selectedEvent.setProp("classNames", [updatedCategory]);
                selectedEvent.setStart(updateStartDate);
                selectedEvent.setEnd(updateEndDate);
                selectedEvent.setAllDay(all_day);
                selectedEvent.setExtendedProp("description", eventDescription);
                selectedEvent.setExtendedProp("location", event_location);
                selectedEvent.setProp("writer", writer);
                var indexOfSelectedEvent = defaultEvents.findIndex(function (x) {
                    return x.id == selectedEvent.id
                });
                if (defaultEvents[indexOfSelectedEvent]) {
					defaultEvents[indexOfSelectedEvent].category = categoryNo;
                    defaultEvents[indexOfSelectedEvent].title = updatedTitle;
                    defaultEvents[indexOfSelectedEvent].start = updateStartDate;
                    defaultEvents[indexOfSelectedEvent].end = updateEndDate;
                    defaultEvents[indexOfSelectedEvent].allDay = all_day;
                    defaultEvents[indexOfSelectedEvent].className = updatedCategory;
                    defaultEvents[indexOfSelectedEvent].description = eventDescription;
                    defaultEvents[indexOfSelectedEvent].location = event_location;
                    defaultEvents[indexOfSelectedEvent].writer = writer;
                }
                calendar.render();
                // default
            } else {
                var newEvent = {
                    id: e_id,
                    title: updatedTitle,
                    start: start_date,
                    end: end_date,
                    allDay: all_day,
                    className: updatedCategory,
                    description: eventDescription,
                    location: event_location
                };
                calendar.addEvent(newEvent);
                defaultEvents.push(newEvent);
            }
            addEvent.hide();
            upcomingEvent(defaultEvents);
        }
    });
	});


    document.getElementById("btn-delete-event").addEventListener("click", function (e) {
        if (selectedEvent) {
			if(confirm("정말 삭제하시겠습니까?")){
				for (var i = 0; i < defaultEvents.length; i++) {
				console.log(defaultEvents[i].id);
				console.log(selectedEvent.id);
					if (defaultEvents[i].id == selectedEvent.id) {
						fetch('/delete/event' + (defaultEvents[i].id), {
							method: 'delete',
							headers: {
								'X-CSRF-TOKEN': csrfToken
							}
						})
						.then(response => response.json())
						.then(data => {
							
						});
						defaultEvents.splice(i, 1);
						i--;
					}
				}
	            upcomingEvent(defaultEvents);
	            
	            selectedEvent.remove();
	            selectedEvent = null;
	            addEvent.hide();
			}
			
        }
    });
    document.getElementById("btn-new-event").addEventListener("click", function (e) {
        flatpicekrValueClear();
        flatPickrInit();
        addNewEvent();
        document.getElementById("edit-event-btn").setAttribute("data-id", "new-event");
        document.getElementById('edit-event-btn').click();
        document.getElementById("edit-event-btn").setAttribute("hidden", true);
    });
});


function flatPickrInit() {
    var config = {
        enableTime: true,
        noCalendar: true,
    };
    var date_range = flatpickr(
        start_date, {
            enableTime: false,
            mode: "range",
            minDate: "today",
            onChange: function (selectedDates, dateStr, instance) {
                var date_range = dateStr;
                var dates = date_range.split("to");
                if (dates.length > 1) {
                    document.getElementById('event-time').setAttribute("hidden", true);
                } else {
                    document.getElementById("timepicker1").parentNode.classList.remove("d-none");
                    document.getElementById("timepicker1").classList.replace("d-none", "d-block");
                    document.getElementById("timepicker2").parentNode.classList.remove("d-none");
                    document.getElementById("timepicker2").classList.replace("d-none", "d-block");
                    document.getElementById('event-time').removeAttribute("hidden");
                }
            },
        });
    flatpickr(timepicker1, config);
    flatpickr(timepicker2, config);

}

function flatpicekrValueClear() {
    start_date.flatpickr().clear();
    timepicker1.flatpickr().clear();
    timepicker2.flatpickr().clear();
}


function eventClicked() {
    document.getElementById('form-event').classList.add("view-event");
    document.getElementById("event-title").classList.replace("d-block", "d-none");
    document.getElementById("event-category").classList.replace("d-block", "d-none");
    document.getElementById("event-start-date").parentNode.classList.add("d-none");
    document.getElementById("event-start-date").classList.replace("d-block", "d-none");
    document.getElementById('event-time').setAttribute("hidden", true);
    document.getElementById("timepicker1").parentNode.classList.add("d-none");
    document.getElementById("timepicker1").classList.replace("d-block", "d-none");
    document.getElementById("timepicker2").parentNode.classList.add("d-none");
    document.getElementById("timepicker2").classList.replace("d-block", "d-none");
    document.getElementById("event-location").classList.replace("d-block", "d-none");
    document.getElementById("event-description").classList.replace("d-block", "d-none");
    document.getElementById("event-start-date-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-timepicker1-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-timepicker2-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-location-tag").classList.replace("d-none", "d-block");
    document.getElementById("event-description-tag").classList.replace("d-none", "d-block");
    document.getElementById('btn-save-event').setAttribute("hidden", true);
}

function editEvent(data) {
    var data_id = data.getAttribute("data-id");
    var selectedEvent = null;
    if (data_id == 'new-event') {
        document.getElementById('modal-title').innerHTML = "";
        document.getElementById('modal-title').innerHTML = "일정 추가";
        document.getElementById("btn-save-event").innerHTML = "일정 추가";
        eventTyped();
    } else if (data_id == 'edit-event') {
		/*if (defaultEvents[i].id == selectedEvent.id) {}*/
        data.innerHTML = "취소";
        data.setAttribute("data-id", 'cancel-event');
        document.getElementById("btn-save-event").innerHTML = "일정 수정";
        data.removeAttribute("hidden");
        console.log(eventContent);
        eventTyped();
        /*fetch('/modify/event'+eventContent,{
			method:'post'
		})
	    .then(response => response.json())
	    .then(data=>{});*/
    } else {
        data.innerHTML = "수정";
        data.setAttribute("data-id", 'edit-event');
        eventClicked();
    }
}

function eventTyped() {
    document.getElementById('form-event').classList.remove("view-event");
    document.getElementById("event-title").classList.replace("d-none", "d-block");
    document.getElementById("event-category").classList.replace("d-none", "d-block");
    document.getElementById("event-start-date").parentNode.classList.remove("d-none");
    document.getElementById("event-start-date").classList.replace("d-none", "d-block");
    document.getElementById("timepicker1").parentNode.classList.remove("d-none");
    document.getElementById("timepicker1").classList.replace("d-none", "d-block");
    document.getElementById("timepicker2").parentNode.classList.remove("d-none");
    document.getElementById("timepicker2").classList.replace("d-none", "d-block");
    document.getElementById("event-location").classList.replace("d-none", "d-block");
    document.getElementById("event-description").classList.replace("d-none", "d-block");
    document.getElementById("event-start-date-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-timepicker1-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-timepicker2-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-location-tag").classList.replace("d-block", "d-none");
    document.getElementById("event-description-tag").classList.replace("d-block", "d-none");
    document.getElementById('btn-save-event').removeAttribute("hidden");
}

// upcoming Event
function upcomingEvent(a) {
	console.log(a);
    a.sort(function (o1, o2) {
        return (new Date(o1.start)) - (new Date(o2.start));
    });
    document.getElementById("upcoming-event-list").innerHTML = null;
    Array.from(a).forEach(function (element) {
        var title = element.title;
        if (element.end) {
            endUpdatedDay = new Date(element.end);
            var updatedDay = endUpdatedDay.setDate(endUpdatedDay.getDate() - 1);
          }
        var e_dt = updatedDay ? updatedDay : undefined;
        if (e_dt == "Invalid Date" || e_dt == undefined) {
            e_dt = null;
        } else {
            const newDate = new Date(e_dt).toLocaleDateString('ko', { year: 'numeric', month: 'numeric', day: 'numeric' });
            e_dt = new Date(newDate)
              .toLocaleDateString("ko-KR", {
                day: "numeric",
                month: "short",
                year: "numeric",
              })
              .split(" ")
              .join(" ");
        }
        var st_date = element.start ? str_dt(element.start) : null;
        var ed_date = updatedDay ? str_dt(updatedDay) : null;
        if (st_date === ed_date) {
            e_dt = null;
        }
        var startDate = element.start;
        if (startDate === "Invalid Date" || startDate === undefined) {
            startDate = null;
        } else {
            const newDate = new Date(startDate).toLocaleDateString('ko', { year: 'numeric', month: 'numeric', day: 'numeric' });
            startDate = new Date(newDate)
              .toLocaleDateString("ko-KR", {
                day: "numeric",
                month: "short",
                year: "numeric",
              })
              .split(" ")
              .join(" ");
        }

        var end_dt = (e_dt) ? " ~ " + e_dt : '';
        var category = element.className.split("-");
        console.log(category);
        /*if(element.className.includes('bg-soft-success')){
			category = '[개인]';
		} else if(element.className.includes('bg-soft-info')){
			category = '[부서]';
		} else if(element.className.includes('bg-soft-warning')){
			category = '[전체]';
		} else {
			category = '[연차]';
		}*/
        /*var category = (element.className).split("-");*/
        var description = (element.description) ? element.description : "";
        var e_time_s = tConvert(getTime(element.start));
        var e_time_e = tConvert(getTime(updatedDay));
        if (e_time_s == e_time_e) {
            var e_time_s = "하루 종일";
            var e_time_e = null;
        }
        var e_time_e = (e_time_e) ? " ~ " + e_time_e : "";
        let nowDate = new Date();
        nowDate = nowDate.getFullYear()+'-'+(nowDate.getMonth()+1)+'-1';
        let toDay = new Date(nowDate);
        
        let lastDate = new Date();
        let lastDay = new Date(lastDate.getFullYear(),(lastDate.getMonth()+1),0);
        /*console.log(element.end);*/
		if (e_dt == null && element.start >= toDay || e_dt != null && element.end <= lastDay) {
			u_event = "<div class='card mb-3'>\
                        <div class='card-body'>\
                            <div class='d-flex mb-3'>\
                                <div class='flex-grow-1'><i class='mdi mdi-checkbox-blank-circle me-2 text-"+category[2]+"'></i><span class='fw-medium'>" + startDate + " </span></div>\
                                <div class='flex-shrink-0'><small class='badge badge-soft-primary ms-auto'>" + e_time_s + e_time_e + "</small></div>\
                            </div>\
                            <h6 class='card-title fs-16'> " + title + "</h6>\
                            <p class='text-muted text-truncate-two-lines mb-0'> " + description + "</p>\
                        </div>\
                    </div>";
        document.getElementById("upcoming-event-list").innerHTML += u_event;
		}
    });
};



function getTime(params) {
    params = new Date(params);
    if (params.getHours() != null) {
        var hour = params.getHours();
        var minute = (params.getMinutes()) ? params.getMinutes() : 0;
        return hour + ":" + minute;
    }
}

function tConvert(time) {
    var t = time.split(":");
    var hours = t[0];
    var minutes = t[1];
    var newformat = hours >= 12 ? '오후' : '오전';
    hours = hours % 12;
    hours = hours ? hours : 12;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    return (newformat + ' ' + hours + ':' + minutes);
}

var str_dt = function formatDate(date) {
    var monthNames = ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"];
    var d = new Date(date),
        month = '' + monthNames[(d.getMonth())],
        day = '' + d.getDate(),
        year = d.getFullYear();
    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;
    return [day + " " + month, year].join(',');
};