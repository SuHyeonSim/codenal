package com.codenal.document.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.domain.Documents;
import com.codenal.document.domain.ShareRequest;
import com.codenal.document.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentApiController {

    @Autowired
    private DocumentService documentService;


    private static final int STATUS_PERSONAL = 0; // 개인 문서
    private static final int STATUS_SHARED = 1;  // 공유 문서
    private static final int STATUS_TRASH = 2;   // 휴지통 문서
    private static final int STATUS_PERSONAL_FAVORITE = 3;   //개인문서 + 즐겨찾기
    private static final int STATUS_SHARED_FAVORITE = 4;   //공유문서 + 즐겨찾기

    // 파일 업로드 엔드포인트
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어 있습니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String empIdString = authentication.getName(); // empId를 String으로 받아옴 (username 대신 empId로 가정)
        Long empId = Long.parseLong(empIdString);

        // 파일 저장 경로 설정 (예: 파일을 로컬에 저장)
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = file.getOriginalFilename();
        String newFilename = generateUniqueFilename(uploadDir, originalFilename); // 파일명 중복 방지

        Path filePath = uploadDir.resolve(newFilename);
        Files.copy(file.getInputStream(), filePath);

        // 파일 정보를 DocumentDto로 변환하여 서비스에 넘김
        DocumentDto documentDto = DocumentDto.builder()
                .docName(originalFilename)
                .docNewName(newFilename)
                .docStatus(STATUS_PERSONAL)  // 상태를 기본값 '0'(개인 문서)으로 설정
                .docCreatedDate(new Date())
                .docPath(filePath.toString())
                .docSize(BigDecimal.valueOf(file.getSize()))
                .docEmpId(empId)
                .build();

        documentService.saveDocument(documentDto);

        return ResponseEntity.ok("파일이 성공적으로 업로드되었습니다.");
    }

    // 파일명 중복 방지를 위한 고유 파일명 생성
    private String generateUniqueFilename(Path dir, String originalFilename) throws IOException {
        String newFilename = originalFilename;
        int count = 1;
        while (Files.exists(dir.resolve(newFilename))) {
            int dotIndex = originalFilename.lastIndexOf('.');
            String name = (dotIndex == -1) ? originalFilename : originalFilename.substring(0, dotIndex);
            String extension = (dotIndex == -1) ? "" : originalFilename.substring(dotIndex);
            newFilename = name + "_" + count + extension;
            count++;
        }
        return newFilename;
    }

 // 문서 목록 반환 엔드포인트 (JSON)
    @GetMapping("/list")
    public ResponseEntity<List<DocumentDto>> getDocumentList(@RequestParam(value = "excludeTrash", required = false) boolean excludeTrash) {
        List<DocumentDto> documents;

        if (excludeTrash) {
            // 휴지통 상태(STATUS_TRASH)가 아닌 문서만 조회
            documents = documentService.getDocumentsByStatusNot(STATUS_TRASH);
        } else {
            // 모든 문서를 조회 (기본)
            documents = documentService.getDocumentList();
        }

        return ResponseEntity.ok(documents);  // JSON 형식으로 문서 목록을 반환
    }
    
 // 공유한 문서 목록 반환
    @GetMapping("/shared-by-me")
    public ResponseEntity<List<DocumentDto>> getSharedByMeDocuments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long empId = Long.parseLong(authentication.getName());  // 로그인한 사용자 ID를 가져옴
        
        List<DocumentDto> documents = documentService.getDocumentsSharedByMe(empId);
        return ResponseEntity.ok(documents);
    }
    
 // 공유받은 문서 목록 반환
    @GetMapping("/shared-with-me")
    public ResponseEntity<List<DocumentDto>> getSharedWithMeDocuments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long empId = Long.parseLong(authentication.getName());  // 로그인한 사용자 ID를 가져옴
        
        List<DocumentDto> documents = documentService.getDocumentsSharedWithMe(empId);
        return ResponseEntity.ok(documents);
    }
    
    
    // 문서 검색 기능 (파일명으로만 검색)
    @GetMapping("/search")
    public ResponseEntity<List<DocumentDto>> searchDocuments(@RequestParam("keyword") String keyword) {
        List<DocumentDto> documents = documentService.searchDocumentsByKeyword(keyword); // 기본 파일명으로만 검색
        return ResponseEntity.ok(documents);
    }


 // 선택된 파일 삭제(휴지통으로 이동)
    @PostMapping("/delete")
    public ResponseEntity<String> moveToTrash(@RequestBody List<Long> docIds) {
        try {
            documentService.moveDocumentsToTrash(docIds); // 서비스에서 문서 상태 변경
            return ResponseEntity.ok("선택한 파일이 휴지통으로 이동되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일을 휴지통으로 이동하는 데 실패했습니다.");
        }
    }
    // 휴지통 파일 목록 가져오기
    @GetMapping("/trash")
    public ResponseEntity<List<DocumentDto>> getTrashDocuments() {
        List<DocumentDto> documents = documentService.getDocumentsByStatus(STATUS_TRASH);
        return ResponseEntity.ok(documents);  // JSON 형식으로 문서 목록을 반환
    }

    @PostMapping("/restore")
    public ResponseEntity<String> restoreDocuments(@RequestBody List<Long> docIds) {
        try {
            documentService.restoreDocuments(docIds); // 여러 문서를 복원 (상태 0으로 변경)
            return ResponseEntity.ok("선택한 파일이 성공적으로 복원되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일을 복원하는 데 실패했습니다.");
        }
    }

    // 휴지통에서 완전 삭제 (영구 삭제)
    
    @PostMapping("/delete-permanently")
    public ResponseEntity<String> deletePermanently(@RequestBody List<Long> docIds) {
        try {
            documentService.deleteDocumentsPermanently(docIds); // 문서를 완전히 삭제
            return ResponseEntity.ok("선택한 파일이 완전히 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("파일을 완전히 삭제하는 데 실패했습니다.");
        }
    }
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadDocument(@RequestParam("docId") Long docId) {
        try {
            Documents document = documentService.getDocumentById(docId);
            Path filePath = Paths.get(document.getDocPath());
            byte[] fileBytes = Files.readAllBytes(filePath);

            // 파일의 확장자에 따라 MIME 타입을 설정
            String mimeType = Files.probeContentType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream"; // 기본값
            }

            // 파일 이름을 명시적으로 설정
            String fileName = document.getDocName();

            // 파일명을 UTF-8로 인코딩하고 URL 인코딩함
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            // Content-Disposition 헤더 설정
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .header(HttpHeaders.CONTENT_TYPE, mimeType)  // 올바른 MIME 타입 설정
                .body(fileBytes);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @GetMapping("/list-paged")
    public ResponseEntity<Page<DocumentDto>> getPagedDocuments(
            @RequestParam(value = "status", defaultValue = "0") int status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        Page<Documents> documentPage = documentService.getDocumentsByStatus(status, page, size);
        Page<DocumentDto> documentDtoPage = documentPage.map(DocumentDto::fromEntity);

        return ResponseEntity.ok(documentDtoPage);
    }

    @PostMapping("/favorite")
    public ResponseEntity<String> toggleFavorite(@RequestBody List<Long> docIds) {
        try {
            documentService.toggleFavoriteDocuments(docIds);  // 문서의 즐겨찾기 상태 변경
            return ResponseEntity.ok("선택한 파일의 즐겨찾기 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("즐겨찾기 상태 변경에 실패했습니다.");
        }
    }
    @GetMapping("/favorites")
    public ResponseEntity<Page<DocumentDto>> getFavoriteDocuments(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long empId = Long.parseLong(authentication.getName());  // 로그인한 사용자 ID를 가져옴
        Page<DocumentDto> favoriteDocuments = documentService.getFavoriteDocuments(empId, PageRequest.of(page, size));
        return ResponseEntity.ok(favoriteDocuments);
    }
    
    @PostMapping("/share")
    public ResponseEntity<String> shareDocuments(@RequestBody ShareRequest shareRequest) {
        try {
            documentService.shareDocuments(shareRequest.getDocIds(), shareRequest.getEmpIds());
            return ResponseEntity.ok("파일이 성공적으로 공유되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("파일 공유에 실패했습니다.");
        }
    }

    @PostMapping("/shared-users")
    public ResponseEntity<List<Long>> getSharedUsers(@RequestBody List<Long> docIds) {
        List<Long> sharedUsers = documentService.findSharedUsersByDocIds(docIds);
        return ResponseEntity.ok(sharedUsers);
    }

}
