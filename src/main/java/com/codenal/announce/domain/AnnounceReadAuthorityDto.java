package com.codenal.announce.domain;

import com.codenal.admin.domain.Departments;
import com.codenal.admin.domain.Jobs;

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
public class AnnounceReadAuthorityDto {

	private int authority_no;
	private Announce announce;
	private Departments departments;
	private Jobs jobs;
	
	public AnnounceReadAuthority toEntity() {
		return AnnounceReadAuthority.builder()
				.authorityNo(authority_no)
				.announce(announce)
				.departments(departments)
				.jobs(jobs)
				.build();
		 
	}
	
	public AnnounceReadAuthorityDto toDto(AnnounceReadAuthority announceReadAuthority) {
		return AnnounceReadAuthorityDto.builder()
				.authority_no(announceReadAuthority.getAuthorityNo())
				.announce(announceReadAuthority.getAnnounce())
				.departments(announceReadAuthority.getDepartments())
				.jobs(announceReadAuthority.getJobs())
				.build();
	}
}
