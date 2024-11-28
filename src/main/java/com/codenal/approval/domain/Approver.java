package com.codenal.approval.domain;

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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Table(name="approver")

public class Approver {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="approver_no")
	private Long approverNo;
	
	@Column(name="approver_type")
	private String approverType;
	
	@Column(name="approval_status")
	private int approvalStatus;
	
	@Column(name="approval_date")
	private LocalDateTime approvalDate;
	
	@Column(name="reject_comment")
	private String rejectComment;
	
	// 결재 우선순위
	@Column(name="approver_priority")
	private int approverPriority;
	
	// 외래키
	@ManyToOne
	@JoinColumn(name="approval_no")
	private Approval approval;
	
	@ManyToOne
	@JoinColumn(name="approver_id")
	private Employee employee;
}
