package com.codenal.alarms.domain;

public class AlarmType {

	public static final String CALENDAR = "calendar";         // 캘린더 알림
	public static final String MEETING = "meeting";           // 회의실 알림
	public static final String APPROVAL = "approval";         // 전자결재 알림

	private AlarmType() {
	}
}
