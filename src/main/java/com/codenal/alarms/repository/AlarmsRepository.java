package com.codenal.alarms.repository;

import com.codenal.alarms.domain.Alarms;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmsRepository extends JpaRepository<Alarms, Long> {

	List<Alarms> findByEmployee_EmpIdAndAlarmIsDeleted(Long empId, String alarmIsDeleted);

	Optional<Alarms> findById(Long alarmNo);

	List<Alarms> findByEmployee_EmpId(Long empId);

    // 해당 날짜의 오전 9시에 알림이 전송
    @Query("SELECT a FROM Alarms a WHERE a.alarmCreateTime >= :start AND a.alarmCreateTime < :end")
    List<Alarms> findAlarmsByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 알림 삭제
    @Query("SELECT a FROM Alarms a WHERE a.alarmCreateTime < :date")
    List<Alarms> findAlarmsBeforeDate(@Param("date") LocalDateTime date);
    
    // 알림 조회
    Alarms findByAlarmReferenceNoAndAlarmType(Long referenceNo,String type);
    
    

}