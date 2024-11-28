package com.codenal.calendar.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CalendarDto {
	
	private Long calendar_schedule_no;
	private Long calendar_schedule_category;
	private String calendar_schedule_title;
	private String calendar_schedule_location;
	private String calendar_schedule_content;
	private Long calendar_schedule_writer;
	private String calendar_schedule_writer_name;
	private LocalDateTime calendar_schedule_start_date;
	private LocalDateTime calendar_schedule_end_date;
	private String calendar_schedule_color;
	
	public Calendar toEntity() {
		return Calendar.builder()
				.calendarScheduleNo(calendar_schedule_no)
				.calendarScheduleCategory(calendar_schedule_category)
				.calendarScheduleTitle(calendar_schedule_title)
				.calendarScheduleLocation(calendar_schedule_location)
				.calendarScheduleContent(calendar_schedule_content)
				.calendarScheduleWriter(calendar_schedule_writer)
				.calendarScheduleStartDate(calendar_schedule_start_date)
				.calendarScheduleEndDate(calendar_schedule_end_date)
				.calendarScheduleColor(calendar_schedule_color)
				.build();
	}
	
	public CalendarDto toDto(Calendar calendar) {
		return CalendarDto.builder()
				.calendar_schedule_no(calendar.getCalendarScheduleNo())
				.calendar_schedule_category(calendar.getCalendarScheduleCategory())
				.calendar_schedule_title(calendar.getCalendarScheduleTitle())
				.calendar_schedule_location(calendar.getCalendarScheduleLocation())
				.calendar_schedule_content(calendar.getCalendarScheduleContent())
				.calendar_schedule_writer(calendar.getCalendarScheduleWriter())
				.calendar_schedule_start_date(calendar.getCalendarScheduleStartDate())
				.calendar_schedule_end_date(calendar.getCalendarScheduleEndDate())
				.calendar_schedule_color(calendar.getCalendarScheduleColor())
				.build();
	}

}
