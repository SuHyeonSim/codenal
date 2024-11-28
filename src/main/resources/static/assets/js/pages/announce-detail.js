function goBack() {
	window.location.href = document.referrer;  // 이전 페이지로 이동하면서 새로고침
}

// 'modDate' 요소의 텍스트 내용 가져오기
const modDate = document.getElementById('modDate').textContent.trim();   // <span id="modDate"> </span> 공백 포함이기 때문에 공백 문자 제거후 판단
const modDate_div = document.getElementById('modDate_div');

// 만약 'modDate' 텍스트가 비어있다면 'modDate_div' 숨기기
if (modDate.length < 1) {
	modDate_div.style.display = 'none';
}



const announceDelete = function(announceNo) {
	if (confirm("정말 삭제하시겠습니까?")) {
		const csrfTokenElement = document.getElementById("csrf_token");
		if (csrfTokenElement) {
			const csrfToken = csrfTokenElement.value;
			fetch('/announce/delete/' + announceNo, {
				method: 'DELETE',
				headers: {
					'X-CSRF-TOKEN': csrfToken
				}
			})
				.then(response => response.json())
				.then(data => {
					if (data.res_code === '200') {
						alert(data.res_msg);
						location.href = `/announce`;
					} else {
						alert(data.res_msg);
					}
				})
				.catch(error => console.error('Error:', error));
		} else {
			console.error('CSRF token element not found.');
		}
	}
}