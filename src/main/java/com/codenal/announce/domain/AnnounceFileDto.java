package com.codenal.announce.domain;

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
public class AnnounceFileDto {
	
	private int file_no;
	private Announce announce;
	private String file_ori_name;
	private String file_new_name;
	private String file_path;
	
	
	public AnnounceFile toEntity() {
		return AnnounceFile.builder()
				.fileNo(file_no)
				.announce(announce)
				.fileOriName(file_ori_name)
				.fileNewName(file_new_name)
				.filePath(file_path)
				.build();
		 
	}
	
	public AnnounceFileDto toDto(AnnounceFile announceFile) {
		return AnnounceFileDto.builder()
				.file_no(announceFile.getFileNo())
				.announce(announceFile.getAnnounce())
				.file_ori_name(announceFile.getFileOriName())
				.file_new_name(announceFile.getFileNewName())
				.file_path(announceFile.getFilePath())
				.build();
	}
}
