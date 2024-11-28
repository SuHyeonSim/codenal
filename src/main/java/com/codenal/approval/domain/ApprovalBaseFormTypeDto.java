package com.codenal.approval.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class ApprovalBaseFormTypeDto {
	
	private int base_form_code;
	private String base_form_name;
	
	public ApprovalBaseFormType toEntity(){
		return ApprovalBaseFormType.builder()
				.baseFormCode(base_form_code)
				.baseFormName(base_form_name)
				.build();
	}
	
	public ApprovalBaseFormTypeDto toDto(ApprovalBaseFormType approvalBaseFormType) {
		return ApprovalBaseFormTypeDto.builder()
				.base_form_code(approvalBaseFormType.getBaseFormCode())
				.base_form_name(approvalBaseFormType.getBaseFormName())
				.build();
	}
}
