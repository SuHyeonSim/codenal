package com.codenal.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codenal.document.domain.DocumentSharedUsers;
import com.codenal.document.domain.DocumentSharedUsersId; // 복합 키 클래스

@Repository
public interface DocumentSharedUsersRepository extends JpaRepository<DocumentSharedUsers, DocumentSharedUsersId> {
	boolean existsById(DocumentSharedUsersId id);
    List<DocumentSharedUsers> findByDocSharedWithEmpId(Long docSharedWithEmpId);
    
    @Query("SELECT COUNT(dsu) > 0 FROM DocumentSharedUsers dsu WHERE dsu.docSharedNo = :docId")
    boolean existsByDocSharedNo(@Param("docId") Long docId);
}
