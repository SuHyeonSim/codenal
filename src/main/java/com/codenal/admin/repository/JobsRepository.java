package com.codenal.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codenal.admin.domain.Jobs;

@Repository
public interface JobsRepository extends JpaRepository<Jobs, Integer> {

    // 1. 직원 정보 수정
    Jobs findByJobNo(Long jobNo);
}