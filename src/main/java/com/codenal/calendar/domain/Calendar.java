package com.codenal.calendar.domain;

import java.time.LocalDateTime;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "calendar")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Calendar {
	
	@Id
	@Column(name = "calendar_schedule_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long calendarScheduleNo;
	
	@Column(name = "calendar_schedule_category")
	private Long calendarScheduleCategory;
	
	@Column(name = "calendar_schedule_title")
	private String calendarScheduleTitle;
	
	@Column(name = "calendar_schedule_location")
	private String calendarScheduleLocation;
	
	@Column(name = "calendar_schedule_content")
	private String calendarScheduleContent;
	
	@Column(name = "calendar_schedule_writer")
	private Long calendarScheduleWriter;
	
//	@ManyToOne
//	@JoinColumn(name = "calendar_schedule_writer")
//	private Employee employee;
	
	@Column(name = "calendar_schedule_start_date")
	private LocalDateTime calendarScheduleStartDate;
	
	@Column(name = "calendar_schedule_end_date")
	private LocalDateTime calendarScheduleEndDate;
	
	@Column(name = "calendar_schedule_color")
	private String calendarScheduleColor;

}
