<?php
header('Content-Type: application/json');

// 데이터베이스 연결 설정
$servername = "localhost:3306/withxwork";
$username = "scott";
$password = "tigger";
$dbname = "calendar";

$conn = new mysqli($servername, $username, $password, $dbname);

// 연결 확인
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// 이벤트 데이터 가져오기
$sql = " SELECT c.calendar_schedule_no , c.calendar_schedule_category , c.calendar_schedule_title , c.calendar_schedule_content , c.calendar_schedule_writer , e.emp_name , c.calendar_schedule_start_date , c.calendar_schedule_color , c.calendar_schedule_location FROM calendar c JOIN employee e ON c.calendar_schedule_writer = e.emp_id;";
$result = $conn->query($sql);

$events = array();

if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $events[] = array(
            'id' => $row['c.calendar_schedule_no'],
            'category' => $row['c.calendar_schedule_category']
            'title' => $row['c.calendar_schedule_title'],
            'start' => $row['c.calendar_schedule_start_date'],
            'end' => $row['c.calendar_schedule_end_date'],
            'location' => $row['c.calendar_schedule_location'],
            'description' => $row['c.calendar_schedule_content'],
            'writer' => $row['e.emp_name'],
            'end' => $row['c.calendar_schedule_color'],
        );
    }
}

echo json_encode($events);

$conn->close();
?>
