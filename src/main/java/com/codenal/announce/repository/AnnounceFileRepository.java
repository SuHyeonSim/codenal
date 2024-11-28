package com.codenal.announce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceFile;

public interface AnnounceFileRepository extends JpaRepository<AnnounceFile, Integer> {
	
	AnnounceFile findByAnnounceAnnounceNo(int announceNo);

	void deleteByAnnounce_AnnounceNo(int announceNo);

	List<AnnounceFile> findByAnnounce_AnnounceNo(int announceNo);

	void deleteById(int announceNo);
}
