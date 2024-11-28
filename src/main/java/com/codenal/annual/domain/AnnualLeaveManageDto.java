package com.codenal.annual.domain;

import com.codenal.employee.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AnnualLeaveManageDto {


    private Long annual_manage_no;
    private Double annual_total_day;
    private Double annual_used_day;
    private Double annual_remain_day;
    private Long emp_id;

  


	
	
	public AnnualLeaveManage toEntity(Employee employee) {
		return AnnualLeaveManage.builder()
				.annualManageNo(annual_manage_no)
				.annualTotalDay(annual_total_day)
				.annualUsedDay(annual_used_day)
				.annualRemainDay(annual_remain_day)
				.employee(employee)
				.build();
	}
	
	public AnnualLeaveManageDto toDto(AnnualLeaveManage alm) {
		return AnnualLeaveManageDto.builder()
				.annual_manage_no(alm.getAnnualManageNo())
				.annual_total_day(alm.getAnnualTotalDay())
				.annual_used_day(alm.getAnnualUsedDay())
				.annual_remain_day(alm.getAnnualRemainDay())
				.build();
	}
}
