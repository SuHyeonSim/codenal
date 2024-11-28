/*                      전자결재                      */
/*모달 관련 js*/
const loginId = document.getElementById('emp_id').value;
document.addEventListener('DOMContentLoaded',function(){
    const selectArray = [];
    let buttonId = '';

    $('#button_modal1, #button_modal2, #references').click(function() {
        buttonId = this.id;
        console.log(buttonId);

        // 선택 배열 초기화
        selectArray.length = 0;

        // jsTree 선택 초기화
        if ($('#treeMenu').data('jstree')) {
            $('#treeMenu').jstree("deselect_all");
        }

        $.ajax({
            url: '/approval/modal',
            method: 'GET',
            dataType: 'json',
            success: function(data) {
                console.log(data);

                // jsTree 데이터 형식 변환
                const formatDataForJsTree = (nodes) => {
                    return nodes
                        .filter(node => node.nodeId.toString() !== loginId)
                        .map(node => ({
                            id: node.nodeId,
                            text: node.nodeName,
                            state: { opened: true },
                            children: node.nodeChildren ? formatDataForJsTree(node.nodeChildren) : [],
                            icon: node.nodeChildren && node.nodeChildren.length > 0 ? 'ri-team-fill' : 'ri-user-2-fill'
                        }));
                };

                const formattedData = formatDataForJsTree(data);
                const companyNode = {
                    id: 'companyName',
                    text: 'withXwork',
                    state: { opened: true },
                    children: formattedData,
                    icon: 'ri-building-fill'
                };

                // jsTree 초기화
                if ($('#treeMenu').data('jstree')) {
                    $('#treeMenu').jstree("destroy");
                }

                $('#treeMenu').jstree({
                    'core': {
                        animation: 0,
                        check_callback: false,
                        data: [companyNode]
                    },
                    'plugins': ['checkbox'],
                    "types": {
                        default: { icon: "ri-team-fill" },
                        file: { icon: "ri-user-2-fill" }
                    }
                }).on('select_node.jstree', function(e, data) {
                    const selectedNodes = $('#treeMenu').jstree("get_selected", true);
                    selectArray.length = 0; // 배열 초기화

                    selectedNodes.forEach(node => {
                        if (node.parent !== 'companyName') {
                            selectArray.push({ id: node.id, text: node.text });
                            console.log(node);
                        }
                    });
                });

                // 모달 표시
                const modalElement = document.getElementById('staticBackdrop');
                if (modalElement) {
                    const modal = new bootstrap.Modal(modalElement, {
                        backdrop: 'static',
                        keyboard: false 
                    });
                    modal.show(); 
                    
                    // 기존 클릭 이벤트 해제
                    $(document).off('click', '#successBtn'); 
                    
                    // 성공 버튼 클릭 이벤트 등록
                    $(document).on('click', '#successBtn', function() {
                        if (buttonId === "references") {
                            const selectedNames = selectArray.map(item => item.text).join(' '); // 선택된 이름을 결합
                            const selectNameElement = document.getElementById('references'); 

                            selectNameElement.value = selectedNames; // 결합된 문자열 설정
                            selectNameElement.setAttribute("data-id", selectArray.map(item => item.id).join(' '));  // json 데이터로 보낼 때 쓸 data-id 값도 넣어주기
                        } else {
                            const selectNameElements = buttonId === "button_modal1" 
                                ? document.querySelectorAll('.selectedName') 
                                : document.querySelectorAll('.selectedName2');

                            const length = Math.max(selectNameElements.length, selectArray.length); // 최대 길이로 설정

                            for (let index = 0; index < length; index++) {
                                if (index < selectArray.length) {
                                    // 배열에 값이 있을 때
                                    selectNameElements[index].textContent = selectArray[index].text; // 선택된 이름 설정
                                    selectNameElements[index].setAttribute('data-name', selectArray[index].id); // data-name 설정
                                } else {
                                    // 배열에 값이 없을 때
                                    selectNameElements[index].textContent = ''; // 비워두기
                                    selectNameElements[index].removeAttribute('data-name'); // data-name 제거
                                }
                            }
                        }
                        modal.hide(); // 모달 닫기
                    });
                } else {
                    console.error("모달 요소를 찾을 수 없습니다.");
                }
            }
        })
    })
	});
// 날짜 생성 관련 로직
    const select = document.getElementById('category-select');
    const content = document.getElementById('dateContent');
	
	document.addEventListener('DOMContentLoaded',function(){
    	select.addEventListener('change', function() {
        const selectedValue = select.value;

        // 날짜 입력 필드 생성
        if (selectedValue == '1') {
            content.innerHTML = `
                <input type="date" style="width:200px;" id="startDate" name="start_date"> - 
                <select style="margin-left:10px; width:150px;" name="time_period"> 
                    <option>선택하세요</option>
                    <option>오전</option>
                    <option>오후</option>
                </select>
                <input type="number" step="0.1" id="totalDay" name="totalDay" style="width:50px;">`;
        } else {
            content.innerHTML = `
                <input type="date" style="width:200px;" id="startDate" name="start_date"> - 
                <input type="date" style="width:200px;" id="endDate" name="end_date"> 
                <input type="number" id="totalDay" step="0.1" name="totalDay" style="width:50px;">`;
        }

        // 날짜 선택 이벤트 리스너 추가
        setupDateListeners();
    });

    function setupDateListeners() {
        const start = document.getElementById('startDate');
        const end = document.getElementById('endDate');

        if (start) {
            start.removeEventListener('change', calculateWorkDays);
            start.addEventListener('change', calculateWorkDays);
        }
        if (end) {
            end.removeEventListener('change', calculateWorkDays);
            end.addEventListener('change', calculateWorkDays);
        }
    }

    function calculateWorkDays() {
        const startDateValue = document.getElementById('startDate').value;
        const endDateValue = document.getElementById('endDate') ? document.getElementById('endDate').value : null;

        if (!startDateValue) {
            document.getElementById('totalDay').value = '';
            return;
        }

        const selectedValue = select.value;
        if (selectedValue == '1') {
            document.getElementById('totalDay').value = '0.5';
            return;
        }

        const startDate = new Date(startDateValue);
        const endDate = endDateValue ? new Date(endDateValue) : new Date();

        if (endDateValue && startDate > endDate) {
			Swal.fire({
             icon: 'info',
             title: '실패',
             text: '종료일이 시작일보다 빠릅니다.'
         });
            
        document.getElementById('endDate').value = '';
        return;
    	}	
		
		if (selectedValue == '3' || selectedValue == '4') {
		            document.getElementById('totalDay').value = '0';
		            return;
		}

        const year = startDate.getFullYear();
        const holidays = new Set();

        fetch(`https://date.nager.at/api/v2/PublicHolidays/${year}/KR`)
            .then(response => response.json())
            .then(data => {
                data.forEach(item => holidays.add(item.date));
                const annualDays = countWorkDays(startDate, endDate, holidays);
                document.getElementById('totalDay').value = annualDays;
            });
    }

    function countWorkDays(start, end, holidays) {
        let totalDays = 0;
        let currentDate = new Date(start);

        while (currentDate <= end) {
            const formattedDate = currentDate.toISOString().split('T')[0];
            const isWeekend = currentDate.getDay() === 0 || currentDate.getDay() === 6;

            if (!isWeekend && !holidays.has(formattedDate)) {
                totalDays++;
            }

            currentDate.setDate(currentDate.getDate() + 1);
        }
        return totalDays;
    };
});
	
   
