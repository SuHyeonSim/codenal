// DataTable 초기화
$(document).ready(function() {
	var table = $('#tasksTable').DataTable({
		"paging": false,
		"ordering": true,
		"searching": false,
		"info": false,
		"lengthChange": true,
		"autoWidth": false,
	});
});