package com.codenal.announce.domain;

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
import lombok.Setter;

@Entity
@Table(name="announce_file")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class AnnounceFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_no")
    private int fileNo;

    @ManyToOne
    @JoinColumn(name="announce_no", nullable = false)
    private Announce announce;

	@Column(name="file_ori_name")
	private String fileOriName;
	
	@Column(name="file_new_name")
	private String fileNewName;
	
	@Column(name="file_path")
	private String filePath;
	
}
