package com.codenal.approval.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ApprovalFormDto {
	
	private int form_code;
	private int base_form_code;
	private String form_name;
	private String form_content;
	private char form_visibility;
	
	public ApprovalForm toEntity() {
		return ApprovalForm.builder()
				.formCode(form_code)
				.formName(form_name)
				.formContent(form_content)
				.formVisibility(form_visibility)
				.build();
	}
	
	public ApprovalFormDto toDto(ApprovalForm alf) {
		return ApprovalFormDto.builder()
				.form_code(alf.getFormCode())
				.form_name(alf.getFormName())
				.form_content(alf.getFormContent())
				.form_visibility(alf.getFormVisibility())
				.build();
	}
}
