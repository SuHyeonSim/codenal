package com.codenal.announce.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.codenal.employee.domain.Employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="announce")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class Announce {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="announce_no")
	private int announceNo;

	
	@OneToMany(mappedBy = "announce")
	private List<AnnounceFile> files;
	
	@OneToMany(mappedBy = "announce")
	private List<AnnounceReadAuthority> readAuthorities;
	
	@Column(name="announce_writer")
	private Long announceWriter;
	
	@ManyToOne
	@JoinColumn(name="announce_writer", insertable = false, updatable = false)
	private Employee employee;

	@Column(name="announce_title")
	private String announceTitle;

	@Column(name="announce_content")
	private String announceContent;
	
	@Column(name="reg_date")
	private LocalDateTime regDate;
	
	
	@Column(name="mod_date")
	private LocalDateTime modDate;
	
	@PrePersist
	public void prePersist() {
		this.regDate = LocalDateTime.now();
	}
	
	@Column(name="view_count")
	private int viewCount;
	
	@Column(name="read_authority_status")
	private char readAuthorityStatus;
	
}
