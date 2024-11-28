package com.codenal.announce.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.codenal.employee.domain.Employee;

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
public class AnnounceDto {

	private int announce_no;
	private Long announce_writer;
	private Employee employee;
	private List<AnnounceFile> announceFile;
	private List<AnnounceReadAuthority> announceReadAuthority;
    private boolean canAccess;  // 접근 권한 여부 boolean 타입 추가

	private String announce_title;
	private String announce_content;
	private LocalDateTime reg_date;
	private LocalDateTime mod_date;
	private int view_count;
	private char read_authority_status;
	
	private int search_type = 1;
	private String search_text;

	
	
	public Announce toEntity() {
		return Announce.builder()
				.announceNo(announce_no)
				.announceWriter(announce_writer)
				.announceTitle(announce_title)
				.announceContent(announce_content)
				.regDate(reg_date)
				.modDate(mod_date)
				.viewCount(view_count)
				.readAuthorityStatus(read_authority_status)
				.employee(employee)
				.files(announceFile)
				.readAuthorities(announceReadAuthority)
				.build();
		 
	}
	
	public AnnounceDto toDto(Announce announce) {
		return AnnounceDto.builder()
				.announce_no(announce.getAnnounceNo())
				.announce_writer(announce.getAnnounceWriter())
				.announce_title(announce.getAnnounceTitle())
				.announce_content(announce.getAnnounceContent())
				.reg_date(announce.getRegDate())
				.mod_date(announce.getModDate())
				.view_count(announce.getViewCount())
				.read_authority_status(announce.getReadAuthorityStatus())
				.employee(announce.getEmployee())
				.announceFile(announce.getFiles())
				.announceReadAuthority(announce.getReadAuthorities())
				.build();
	}
	
}
