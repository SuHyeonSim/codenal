package com.codenal.approval.domain;

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
@ToString
@Builder

public class ReferrerDto {
	
	private Long reffere_no;
	private Long approval_no;
	private Long emp_id;
	
	
	
}
