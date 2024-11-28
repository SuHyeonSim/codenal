package com.codenal.alarms.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.codenal.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "alarms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Alarms implements Serializable {
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarm_no")
	private Long alarmNo;  // 알림 번호 

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private Employee employee;  // Employee와 조인

	@Column(name = "alarm_title")
	@NotNull(message = "알림 제목은 필수입니다.")
	private String alarmTitle;  // 알림 제목

	@Column(name = "alarm_context")
	private String alarmContext;  // 알림 내용

	@Column(name = "alarm_type")
	@Pattern(regexp = "calendar|meeting|approval", message = "유효하지 않은 알림 유형입니다.")
	private String alarmType;  // 알림 유형

	@Column(name = "alarm_reference_no")
	private Long alarmReferenceNo;  // 알림 참조 번호

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "alarm_create_time")
	private LocalDateTime alarmCreateTime; // 알림 생성 시간

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "alarm_read_time")
	private LocalDateTime alarmReadTime;  // 알림 확인 시간

	@Column(name = "alarm_is_read")
	private String alarmIsRead;  // 알림 확인 여부 ('Y' 또는 'N')

	@Column(name = "alarm_is_deleted")
	private String alarmIsDeleted;  // 알림 삭제 여부 ('Y' 또는 'N')

	@Column(name = "alarm_url")
	private String alarmUrl;  // 관련 알림 URL
}
