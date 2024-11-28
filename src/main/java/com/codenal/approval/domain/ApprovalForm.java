package com.codenal.approval.domain;

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
@Table(name="approval_form")

public class ApprovalForm {

	@Id
	@Column(name="form_code")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int formCode;
	
	@Column(name="form_name")
	private String formName;
	
	@Column(name="form_content")
	private String formContent;
	
	@Column(name="form_visibility")
	private char formVisibility;
	
	// 외래키
	@ManyToOne
	@JoinColumn(name="base_form_code")
	private ApprovalBaseFormType approvalBaseFormType;
	
}
