package com.codenal.calendar.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codenal.calendar.domain.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar, Long>{
	
	Calendar findBycalendarScheduleNo(Long eventNo);

}
