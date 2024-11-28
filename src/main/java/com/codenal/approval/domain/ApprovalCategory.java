package com.codenal.approval.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="approval_category")
@Getter
@Builder

public class ApprovalCategory {
	
	@Id
	@Column(name="cate_code")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cateCode;
	
	// 외래키
	@ManyToOne
	@JoinColumn(name="form_code")
	private ApprovalForm approvalForm;
	
	@OneToMany(mappedBy = "approvalCategory")
	private List<Approval> approvals;
	
}
