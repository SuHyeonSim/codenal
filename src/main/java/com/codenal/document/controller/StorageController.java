package com.codenal.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.document.domain.Storage;
import com.codenal.document.service.DocumentService;

@Controller
@RequestMapping("/api/storage")  // 클래스 레벨 매핑 추가
public class StorageController {
    
    @Autowired
    private DocumentService documentService;

    @GetMapping("/storage-usage")
    @ResponseBody
    public Storage getStorageUsage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long empId = Long.parseLong(authentication.getName());  // 현재 사용자의 empId 가져오기
        return documentService.getStorageInfo(empId);
    }
}
