package com.codenal.admin.domain;

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
@Builder
@ToString
public class JobsDto {

    private Long jobNo; // 직급 번호
    private String jobName; // 직급명
    private int jobPriority; // 우선순위

    public static JobsDto fromEntity(Jobs jobs) {
        return JobsDto.builder()
                .jobNo(jobs.getJobNo())
                .jobName(jobs.getJobName())
                .jobPriority(jobs.getJobPriority())
                .build();
    }

    public Jobs toEntity() {
        return Jobs.builder()
                .jobNo(jobNo)
                .jobName(jobName)
                .jobPriority(jobPriority)
                .build();
    }
}
