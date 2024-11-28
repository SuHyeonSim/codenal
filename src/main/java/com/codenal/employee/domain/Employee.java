package com.codenal.employee.domain;

import java.time.LocalDate;
import java.util.List;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;
import com.codenal.alarms.domain.Alarms;
import com.codenal.announce.domain.Announce;
import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.Referrer;
import com.codenal.chat.domain.ChatParticipants;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Employee")
@Table(name="employee")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Setter
public class Employee {

	@Id
	@Column(name = "emp_id")
	private Long empId;

	@Column(name = "emp_pw")
	private String empPw;

	@Column(name = "emp_name")
	private String empName;

	@Column(name = "emp_ssn")
	private String empSsn;

	@Column(name = "emp_phone")
	private String empPhone;

	@Column(name = "emp_address")
	private String empAddress;

	@Column(name = "emp_address_detail")
	private String empAddressDetail;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "emp_hire")
	private LocalDate empHire;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "emp_end")
	private LocalDate empEnd;

	@Column(name = "emp_status")
	private String empStatus;

	@Column(name = "emp_profile_picture")
	private String empProfilePicture;

	@Column(name = "emp_sign_image")
	private String empSignImage;

	@ManyToOne
	@JoinColumn(name = "dept_no", referencedColumnName = "dept_no")
	private Departments departments;

	@ManyToOne
	@JoinColumn(name = "job_no",  referencedColumnName = "job_no")
	private Jobs jobs;
	
	@Column(name = "emp_auth")
	private String empAuth;

	//전자결재
	@OneToMany(mappedBy = "employee")
	private List<Approval> approvals;

	//연차사용내역
	@OneToMany(mappedBy = "employee")
	private List<AnnualLeaveManage> annualLeaveManages;

	//수신참조자
	@OneToMany(mappedBy = "employee")
	private List<Referrer> referrers;

	@OneToMany(mappedBy = "employee")
	private List<AnnualLeaveUsage> annualLeaveUsages;

	@OneToMany(mappedBy = "employee")
	private List<Announce> announces;

	@OneToMany(mappedBy = "employee")
	private List<ChatParticipants> participants;
	
	@OneToMany(mappedBy = "employee")
	private List<Alarms> alarm; 
}