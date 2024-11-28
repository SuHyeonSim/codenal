package com.codenal.approval.domain;

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
@Table(name="referrer")

public class Referrer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="referrer_no")
    private Long referrerNo;
	
	@ManyToOne
	@JoinColumn(name="approval_no")
	private Approval approval;
	
	@ManyToOne
	@JoinColumn(name="referrer_id")
	private Employee employee;
}
