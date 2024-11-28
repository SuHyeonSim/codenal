package com.codenal.approval.domain;

import java.time.LocalDateTime;

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

public class ApproverDto {
	
	private Long approver_no;
	private Long approval_no;
	private Long emp_id;
	private String approver_type;
	private int approval_status;
	private LocalDateTime approval_date;
	private String reject_comment;
	private int approver_priority;
	
	public Approver toEntity() {
		return Approver.builder()
				.approverType(approver_type)
				.approvalStatus(approval_status)
				.approvalDate(approval_date)
				.rejectComment(reject_comment)
				.approverPriority(approver_priority)
				.build();
	}
	
	public ApproverDto toDto(Approver a) {
		return ApproverDto.builder()
				.approver_type(a.getApproverType())
				.approval_status(a.getApprovalStatus())
				.approval_date(a.getApprovalDate())
				.reject_comment(a.getRejectComment())
				.approver_priority(a.getApproverPriority())
				.emp_id(a.getEmployee().getEmpId())
				.build();
	}
}
