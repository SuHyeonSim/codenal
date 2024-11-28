package com.codenal.document.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.service.DocumentService;

@Controller
@RequestMapping("/documents")
public class DocumentViewController {

    @Autowired
    private DocumentService documentService;

   

    // HTML 페이지를 렌더링
    @GetMapping
    public String showFileManagerPage(Model model) {
        // 여기에서는 별도의 데이터가 필요 없으면 그냥 바로 페이지를 렌더링
        return "apps/file-manager";  // Thymeleaf 템플릿 이름
    }
}
