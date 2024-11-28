package com.codenal.document.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documents")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Setter
public class Documents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_no")  // 문서 코드
    private Long docNo;

    @Column(name = "doc_name")  // 문서명
    private String docName;

    @Column(name = "doc_new_name")  // 문서 새로운 명
    private String docNewName;

    @Column(name = "doc_status")  // 문서 상태
    private int docStatus;

    @Temporal(TemporalType.DATE)
    @Column(name = "doc_created_date")  // 문서 등록일
    private Date docCreatedDate;

    @Column(name = "doc_path")  // 문서 경로
    private String docPath;

    @Column(name = "doc_size")  // 문서 사이즈
    private BigDecimal docSize;

    @Column(name = "doc_emp_id")  // 문서 등록한 직원 ID
    private Long docEmpId;
    
 // 직원과 연관관계 설정 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_emp_id", referencedColumnName = "emp_id", insertable = false, updatable = false)
    private Employee employee;
}
