document.addEventListener('DOMContentLoaded', () => {
	// 삭제 버튼에 클릭 이벤트 리스너 추가
    document.querySelectorAll('.delete-file-btn').forEach(function(button) {
		const csrfToken = document.getElementById('csrf_token').value;
        button.addEventListener('click', function() {
		const fileId = this.getAttribute('data-file-id');  // 버튼에서 fileId를 가져옴
            if (confirm('정말로 이 파일을 삭제하시겠습니까?')) {
                 fetch('/announce/deleteFile', {
			        method: 'POST',
			        headers: {
						'Content-Type': 'application/json',
			            'X-CSRF-TOKEN': csrfToken  // CSRF 토큰을 헤더에 추가
			        },
			        body: JSON.stringify({ fileId: fileId })  // 파일 ID를 요청 본문에 포함
			    })
			    .then(response => {
			        if (response.ok) {
			            alert('파일이 삭제되었습니다.');
			            window.location.reload();  // 페이지를 새로고침
			        } else {
			            alert('파일 삭제 중 오류가 발생했습니다.');
			        }
			    })
                .catch(error => {
                    alert(error.message);
                });
            }
        });
    });
    
    // 읽기 권한 설정 모달 열기
    const selectRadio = document.getElementById('select');

    selectRadio.addEventListener('click', () => {
        if (selectRadio.checked) {
            const selectArray = [];
            selectArray.length = 0;

            if ($('#treeMenu').data('jstree')) {
                $('#treeMenu').jstree("deselect_all");
            }

            // Fetch API 사용
            fetch('/employee/addressBook/tree-menu-announce', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
            .then(response => {
                // 응답이 성공적이지 않으면 에러 처리
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();  // JSON 형식으로 응답을 파싱
            })
            .then(data => {
                const formatDataForJsTree = (nodes) => {
                    return nodes.map(department => ({
                        id: department.nodeId,  // 부서 ID
                        text: department.nodeName,  // 부서명
                        state: { opened: true },  // 부서 노드를 열어둠
                        children: department.nodeChildren.map(job => ({
                            id: job.nodeId,
                            text: job.nodeName,  // 직급명
                            jobId: job.jobId,   // 직급 ID
                            state: { opened: false },  // 직급은 닫아둠
                            icon: 'ri-user-2-fill'  // 아이콘 설정
                        })),
                        icon: 'ri-team-fill'  // 부서 아이콘 설정
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
                });

                // 모달 표시
                const modalElement = document.getElementById('staticBackdrop');
                if (modalElement) {
                    const modal = new bootstrap.Modal(modalElement, {
                        backdrop: 'static',
                        keyboard: false
                    });
                    modal.show();

                    $(document).off('click', '#successBtn');
                    $(document).on('click', '#successBtn', function() {
                        const selectedDeptIds = [];
                        const selectedJobIds = [];

                        // 선택된 노드 확인
                        $('#treeMenu').jstree("get_selected", true).forEach(item => {
                            console.log("Selected Node:", item);

                            // 부모 노드가 "companyName"이 아니면, 해당 노드를 직급으로 간주
                            if (item.parent !== 'companyName') {
                                selectedJobIds.push(item.original.jobId);  // 직급 ID 저장
                            } else {
                                // 부모 노드가 "companyName"이면 부서로 간주
                                selectedDeptIds.push(item.id);
                            }
                        });

                        const selectedDeptIdsString = selectedDeptIds.join(',');
                        const selectedJobIdsString = selectedJobIds.join(',');

                        // hidden 필드에 선택된 값 설정 (부서 ID와 직급 ID를 각각 저장)
                        document.getElementById('selectedDeptIds').value = selectedDeptIdsString;
                        document.getElementById('selectedJobIds').value = selectedJobIdsString;

                        // 모달 닫기
                        modal.hide();
                    });
                } else {
                    console.error("모달 요소를 찾을 수 없습니다.");
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
            });
        }
    });
});




document.addEventListener('DOMContentLoaded', () => {

function goBack() {
	window.location.href = document.referrer;  // 이전 페이지로 이동하면서 새로고침
}

const form = document.getElementById("announceUpdateForm");
const announceWriter = document.getElementById('announce_writer');
const announceTitle = document.getElementById('announce_title');
const allRadio = document.getElementById('all');
const selectRadio = document.getElementById('select');
const selectedDeptIds = document.getElementById('selectedDeptIds');
const selectedJobIds = document.getElementById('selectedJobIds');

	form.addEventListener('submit', (e) => {
	    e.preventDefault(); // 기본 폼 제출 동작 방지
	
		// 유효성 검사 함수 호출
	    if (validateForm()) {
	        // 유효성 검사를 통과하면 폼을 제출할 수 있는 추가 동작을 실행
	   		// FormData 객체를 생성하여 폼 데이터를 가져옴
		    const formData = new FormData(form);
		
		    // 추가적으로 필요한 데이터를 FormData에 추가
		    formData.append('announce_no', document.getElementById('announce_no').value);
		    formData.append('announce_writer', document.getElementById('announce_writer').value);
		    formData.append('announce_title', document.getElementById('announce_title').value);
		    formData.append('announce_content', editor.getHTML());
		    formData.append('reg_date', document.getElementById('reg_date').value);
		    formData.append('read_authority_status', document.querySelector('input[name="read_authority_status"]:checked')?.value);
		
			
		    const csrfToken = document.getElementById('csrf_token').value;
			const announceNo = document.getElementById('announce_no').value;
		
		    fetch('/announce/updateEnd/'+announceNo, {
		        method: 'POST',
		        headers: {
		            'X-CSRF-TOKEN': csrfToken // CSRF 토큰을 헤더에 포함
		        },
		        body: formData // FormData 객체를 body에 포함하여 전송
		    })
		     .then(response => response.json())
		     .then(data => {
		        if (data.res_code === '200') {
		            alert('게시글 수정을 완료했습니다.');
		            location.href = `/announce/detail/${data.announceNo}`; // 서버에서 받은 `announceNo`로 URL을 설정합니다.
		        } else {
		            alert('실패: ' + data.res_msg);
		        }
		     })
		};
	
		// 유효성 검사
	    function validateForm() {
	    if(announceTitle.value.length < 1 || announceTitle.value.length > 50 ){
			alert('게시글 제목은 최소 1글자, 최대 50글자 이내로 입력하세요.');
			announceTitle.focus(announceTitle);
			return false; // 폼 제출을 막음
		} else if(editor.getMarkdown().length < 1) {
		    alert('게시글 내용을 입력하세요.');
		    editor.focus(editor);
		   return false; // 폼 제출을 막음
		} else if (!allRadio.checked && !selectRadio.checked) { 	// 라디오 버튼 선택 여부 확인
	        alert('읽기 권한을 선택하세요.');
		   return false; // 폼 제출을 막음
	    } else if (selectRadio.checked) {
		    // 부서 또는 직급이 선택되었는지 확인
		    const deptSelected = selectedDeptIds && selectedDeptIds.value.trim() !== '';
		    const jobSelected = selectedJobIds && selectedJobIds.value.trim() !== '';
		
		    if (!deptSelected && !jobSelected) {  // 둘 다 선택되지 않았을 경우
		        alert('부서 또는 직급을 선택하세요.');
		        return false; // 폼 제출을 막음
		    }
	    }
	   	  return true;
	    }
	});
});


