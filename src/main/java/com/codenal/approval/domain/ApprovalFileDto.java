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

public class ApprovalFileDto {
	
	private Long file_no;
	private Long approval_no;
	private String file_ori_name;
	private String file_new_name;
	private String file_path;
	private Approval approval;
	
	public ApprovalFile toEntity() {
		return ApprovalFile.builder()
				.fileNo(file_no)
				.fileOriName(file_ori_name)
				.fileNewName(file_new_name)
				.filePath(file_path)
				.approval(approval)
				.build();
	}
	
	public ApprovalFileDto toDto(ApprovalFile af) {
		return ApprovalFileDto.builder()
				.file_no(af.getFileNo())
				.file_ori_name(af.getFileOriName())
				.file_new_name(af.getFileNewName())
				.file_path(af.getFilePath())
				.approval(af.getApproval())
				.build();
	}
}