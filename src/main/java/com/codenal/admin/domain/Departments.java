package com.codenal.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.codenal.announce.domain.AnnounceReadAuthority;
import com.codenal.employee.domain.Employee;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="departments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter 
@Builder
public class Departments { 

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dept_no")
	private Long deptNo; // 부서 번호

    @Column(name = "dept_name", nullable = false)
    private String deptName;	// 부서명

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dept_create_date", nullable = false)
    @CreationTimestamp
    private LocalDate deptCreateDate;	// 부서 개설일
    
    @OneToMany(mappedBy = "departments")
    private List<Employee> employee;
    
    @OneToMany(mappedBy = "departments")
    private List<AnnounceReadAuthority> readAuthorities;

    public int getEmpCount() {
        return employee != null ? employee.size() : 0;
    }
}