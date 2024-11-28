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

public class ApprovalCategoryDto {
	
	private int cate_code;
	private int form_code;
	
	public ApprovalCategory toEntity() {
		return ApprovalCategory.builder()
				.cateCode(cate_code)
				.build();
	}
	
	public ApprovalCategoryDto toDto(ApprovalCategory ac) {
		return ApprovalCategoryDto.builder()
				.cate_code(ac.getCateCode())
				.build();
	}
}
