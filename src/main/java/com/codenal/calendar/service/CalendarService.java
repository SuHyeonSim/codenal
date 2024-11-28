package com.codenal.calendar.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;
import com.codenal.admin.repository.DepartmentsRepository;
import com.codenal.admin.repository.JobsRepository;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.domain.AnnualLeaveUsageDto;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.calendar.domain.Calendar;
import com.codenal.calendar.domain.CalendarDto;
import com.codenal.calendar.repository.CalendarRepository;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

@Service
public class CalendarService {
	
	private final CalendarRepository calendarRepository;
	private final EmployeeRepository employeeRepository;
	private final DepartmentsRepository departmentsRepository;
	private final JobsRepository jobsRepository;
	private final ApprovalRepository approvalRepository;
	private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
	
	@Autowired
	public CalendarService(CalendarRepository calendarRepository, EmployeeRepository employeeRepository, DepartmentsRepository departmentsRepository, JobsRepository jobsRepository, ApprovalRepository approvalRepository, AnnualLeaveUsageRepository annualLeaveUsageRepository) {
		this.calendarRepository = calendarRepository;
		this.employeeRepository = employeeRepository;
		this.departmentsRepository = departmentsRepository;
		this.jobsRepository = jobsRepository;
		this.approvalRepository = approvalRepository;
		this.annualLeaveUsageRepository = annualLeaveUsageRepository;
	}
	
	public String[] eventWriter(Long empId) {
		Employee emp = employeeRepository.findByEmpId(empId);
		EmployeeDto dto = EmployeeDto.fromEntity(emp);
		
		Long detpNo = dto.getDeptNo();
		Departments dept = departmentsRepository.findByDeptNo(detpNo);
		
		Long jodNo = dto.getJobNo();
		Jobs job = jobsRepository.findByJobNo(jodNo);
		String[] str = {dto.getEmpName(), dept.getDeptName(),job.getJobName()};
		// jobs, jobsDto 생성 후 부서명, 직급명 각자 레포지토리에서 가져온 후 String 객체 만들어서 거기다 넣어서 리턴 후 js에서 출력하기
		
		return str;
	}
	
	public void modifyEvent(CalendarDto dto) {
		CalendarDto res = selectOneEvent(dto.getCalendar_schedule_no());
		res.setCalendar_schedule_category(dto.getCalendar_schedule_category());
		res.setCalendar_schedule_title(dto.getCalendar_schedule_title());
		res.setCalendar_schedule_content(dto.getCalendar_schedule_content());
		res.setCalendar_schedule_writer(dto.getCalendar_schedule_writer());
		res.setCalendar_schedule_start_date(dto.getCalendar_schedule_start_date());
		res.setCalendar_schedule_end_date(dto.getCalendar_schedule_end_date());
		res.setCalendar_schedule_location(dto.getCalendar_schedule_location());
		res.setCalendar_schedule_color(dto.getCalendar_schedule_color());
		Calendar calendar = res.toEntity();
		Calendar result = calendarRepository.save(calendar);
	}
	
	public CalendarDto selectOneEvent(Long eventNo) {
		Calendar cal = calendarRepository.findBycalendarScheduleNo(eventNo);
		CalendarDto dto = CalendarDto.builder()
				.calendar_schedule_no(cal.getCalendarScheduleNo())
				.calendar_schedule_category(cal.getCalendarScheduleCategory())
				.calendar_schedule_title(cal.getCalendarScheduleTitle())
				.calendar_schedule_content(cal.getCalendarScheduleContent())
				.calendar_schedule_writer(cal.getCalendarScheduleWriter())
				.calendar_schedule_start_date(cal.getCalendarScheduleStartDate())
				.calendar_schedule_end_date(cal.getCalendarScheduleEndDate())
				.calendar_schedule_color(cal.getCalendarScheduleColor())
				.calendar_schedule_location(cal.getCalendarScheduleLocation())
				.build();
		return dto;
		
	}
	
	public int deleteEvent(Long eventNo) {
		int result = 0;
		try {
			calendarRepository.deleteById(eventNo);
			result = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 일정 목록 조회
	public List<CalendarDto> selectEvent(Long writerId){
		List<Calendar> eventList = calendarRepository.findAll();
		List<CalendarDto> eventDtoList = new ArrayList<CalendarDto>();
		List<Approval> approvalList = approvalRepository.findByApprovalStatus(2);
		for(Calendar c : eventList) {
			CalendarDto calendarDto = new CalendarDto().toDto(c);
			Long writer = c.getCalendarScheduleWriter();
			Employee empName = employeeRepository.findByEmpId(writer);
			EmployeeDto empNameDto = EmployeeDto.fromEntity(empName);
			calendarDto.setCalendar_schedule_writer_name(empNameDto.getEmpName());
			Employee nowEmp = employeeRepository.findByEmpId(writerId);
			EmployeeDto nowEmpDto = EmployeeDto.fromEntity(nowEmp);
			Long nowDept = nowEmpDto.getDeptNo(); // 현재 로그인한 사원의 부서
			Long writerDept = empNameDto.getDeptNo(); // 일정 작성자의 부서
			if((calendarDto.getCalendar_schedule_category() == 5) || (calendarDto.getCalendar_schedule_category() == 4) || (calendarDto.getCalendar_schedule_category() == 3) || (calendarDto.getCalendar_schedule_category() == 2 && writerDept == nowDept) || (calendarDto.getCalendar_schedule_category() == 1 && calendarDto.getCalendar_schedule_writer().equals(writerId))) {
				eventDtoList.add(calendarDto);
				
			}
		}
		for(Approval a : approvalList) {
			new ApprovalDto();
			ApprovalDto approvalDto = ApprovalDto.toDto(a);
			if(approvalDto.getAnnual_leave_usage_no() != null) {
				
				AnnualLeaveUsage annualLeaveList = annualLeaveUsageRepository.findByAnnualUsageNo(approvalDto.getAnnual_leave_usage_no());
	
				new AnnualLeaveUsageDto();
				AnnualLeaveUsageDto annualLeaveListDto = AnnualLeaveUsageDto.toDto(annualLeaveList);
				CalendarDto annualLeaveAdd = new CalendarDto();
				annualLeaveAdd.setCalendar_schedule_category((long) 4);
				
				Employee emp = employeeRepository.findByEmpId(approvalDto.getEmp_id());
				EmployeeDto empDto = EmployeeDto.fromEntity(emp);
				String title = "연차";
				switch(annualLeaveListDto.getAnnual_type()) {
				case 1: title="[반차] "+empDto.getEmpName()+" "+empDto.getJobName(); break;
				case 2: title="[연차] "+empDto.getEmpName()+" "+empDto.getJobName(); break;
				case 3: title="[경조휴가] "+empDto.getEmpName()+" "+empDto.getJobName(); break;
				case 4: title="[병가] "+empDto.getEmpName()+" "+empDto.getJobName(); break;
				}
				annualLeaveAdd.setCalendar_schedule_no(annualLeaveListDto.getAnnual_usage_no());
				annualLeaveAdd.setCalendar_schedule_title(title);
				annualLeaveAdd.setCalendar_schedule_start_date(LocalDateTime.of(annualLeaveListDto.getAnnual_usage_start_date().getYear(), annualLeaveListDto.getAnnual_usage_start_date().getMonth(), annualLeaveListDto.getAnnual_usage_start_date().getDayOfMonth(), 0, 0));
				if(annualLeaveListDto.getAnnual_usage_end_date() != null) {
					annualLeaveAdd.setCalendar_schedule_end_date(LocalDateTime.of(annualLeaveListDto.getAnnual_usage_end_date().getYear(), annualLeaveListDto.getAnnual_usage_end_date().getMonth(), annualLeaveListDto.getAnnual_usage_end_date().getDayOfMonth(), 0, 0));
				}
				annualLeaveAdd.setCalendar_schedule_writer(annualLeaveListDto.getEmp_id());
				eventDtoList.add(annualLeaveAdd);
			}
		}
		
		return eventDtoList;
	}
	
	public Calendar createEvent(CalendarDto dto) {
		Calendar calendar = Calendar.builder()
				.calendarScheduleCategory(dto.getCalendar_schedule_category())
				.calendarScheduleTitle(dto.getCalendar_schedule_title())
				.calendarScheduleLocation(dto.getCalendar_schedule_location())
				.calendarScheduleContent(dto.getCalendar_schedule_content())
				.calendarScheduleWriter(dto.getCalendar_schedule_writer())
				.calendarScheduleStartDate(dto.getCalendar_schedule_start_date())
				.calendarScheduleEndDate(dto.getCalendar_schedule_end_date())
				.build();
		return calendarRepository.save(calendar);
	}

}