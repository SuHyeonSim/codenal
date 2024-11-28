package com.codenal.document.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.codenal.document.domain.Documents;

@Repository
public interface DocumentRepository extends JpaRepository<Documents, Long> {

  

    // findByDocName으로 수정
    List<Documents> findByDocNameContainingIgnoreCase(String keyword);

    // 상태로 문서 찾기
    List<Documents> findByDocStatus(int status);
    
    
   
        List<Documents> findAllByDocStatusNot(int docStatus);  // 휴지통 상태가 아닌 문서 조회
        
        // 공유한 문서 조회 (등록자 기준)
        List<Documents> findByDocEmpIdAndDocStatus(Long docEmpId, int docStatus);

      
        Page<Documents> findByDocStatus(int docStatus, Pageable pageable);


       
        
        Page<Documents> findByDocEmpIdAndDocStatusIn(Long docEmpId, List<Integer> statuses, Pageable pageable);
        
     // 문서 ID 리스트를 기반으로 공유된 사용자 ID 리스트를 반환하는 쿼리
        @Query("SELECT dsu.docSharedWithEmpId FROM DocumentSharedUsers dsu WHERE dsu.docSharedNo IN :docIds")
        List<Long> findSharedUsersByDocIds(@Param("docIds") List<Long> docIds);


        // 직원 ID에 따른 전체 문서 용량 합산 (long으로 반환)
        @Query("SELECT SUM(d.docSize) FROM Documents d WHERE d.docEmpId = :empId")
        Long sumDocSize(@Param("empId") Long empId);
        
}
