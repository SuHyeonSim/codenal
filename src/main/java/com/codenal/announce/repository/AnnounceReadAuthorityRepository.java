package com.codenal.announce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.codenal.announce.domain.AnnounceReadAuthority;

public interface AnnounceReadAuthorityRepository extends JpaRepository<AnnounceReadAuthority, Integer> {

    void deleteByAnnounce_AnnounceNo(int no);

	List<AnnounceReadAuthority> findByAnnounce_AnnounceNo(int announceNo);

}
