package com.codenal.announce.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.codenal.announce.domain.Announce;
import com.codenal.employee.domain.Employee;

public interface AnnounceRepository extends JpaRepository<Announce, Integer>{

	Page<Announce> findByAnnounceTitleContaining(String keyword, Pageable pageable);
	Page<Announce> findByEmployeeEmpNameContaining(String keyword, Pageable pageable);

	@Query(value="SELECT a FROM Announce a "
			+ "WHERE a.announceTitle Like CONCAT('%',?1,'%') "
			+ "OR a.employee.empName LIKE CONCAT('%',?1,'%') "
			+ "ORDER BY a.regDate DESC",
			countQuery="SELECT COUNT(a) FROM Announce a "
					+ "WHERE a.announceTitle LIKE CONCAT('%',?1,'%') "
					+ "OR a.employee.empName LIKE CONCAT('%',?1,'%') ")
	Page<Announce> findByAnnounceTitleOrAnnounceWriterContaining(String keyword, Pageable pageable);

    

    Announce findByAnnounceNo(int announceNo);

    @Modifying
    @Query("UPDATE Announce a SET a.viewCount = a.viewCount + 1 WHERE a.announceNo = ?1 AND a.employee != ?2")
	void updateViewCount(int announceNo, Employee empId);

    
    // *메인화면 출력용 *
    @Query("SELECT a FROM Announce a ORDER BY regDate DESC LIMIT 10")
    List<Announce> findMainAnnounce();
    
    @Query("SELECT COUNT(a) FROM Announce a")
    int findAllCount();
    //* 메인화면 끝*
    
}
