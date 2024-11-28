package com.codenal.approval.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="approval_base_form_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@Builder

public class ApprovalBaseFormType {
	
	@Id
	@Column(name="base_form_code")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int baseFormCode;
	
	@Column(name="base_form_name")
	private String baseFormName;
	
	@OneToMany(mappedBy="approvalBaseFormType")
	private List<ApprovalForm> approvalForms;
}
