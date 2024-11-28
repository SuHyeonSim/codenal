package com.codenal.admin.domain;

import java.time.LocalDate;

//부서 인원 수 카운트 클래스 -> 집계 함수(count, sum) 사용을 위해 
public interface DepartmentsCount {	
	 
	Long getDeptNo();
	String getDeptName();
	LocalDate getDeptCreateDate();
	Long getEmpCount();
	
}