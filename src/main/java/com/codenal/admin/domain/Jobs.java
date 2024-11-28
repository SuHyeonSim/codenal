package com.codenal.admin.domain;

import java.util.List;

import com.codenal.announce.domain.AnnounceReadAuthority;
import com.codenal.employee.domain.Employee;

import lombok.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "jobs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_no")
    private Long jobNo; // 직급 번호

    @Column(name = "job_name", nullable = false)
    private String jobName; // 직급명

    @Column(name = "job_priority", nullable = false)
    private int jobPriority; // 우선순위
    
    @OneToMany(mappedBy = "jobs")
    private List<Employee> employee;
    
    @OneToMany(mappedBy = "jobs")
    private List<AnnounceReadAuthority> readAuthorities;
}