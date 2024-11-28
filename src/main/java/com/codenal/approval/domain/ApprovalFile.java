package com.codenal.approval.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="approval_file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ApprovalFile {
	
	@Id
	@Column(name="file_no")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long fileNo;
	
	@Column(name="file_ori_name")
	private String fileOriName;
	
	@Column(name="file_new_name")
	private String fileNewName;
	
	@Column(name="file_path")
	private String filePath;
	
	@OneToOne
	@JoinColumn(name="approval_no")
	private Approval approval;
}
