package com.codenal.document.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codenal.document.domain.DocumentDto;
import com.codenal.document.domain.DocumentSharedUsers;
import com.codenal.document.domain.DocumentSharedUsersId;
import com.codenal.document.domain.Documents;
import com.codenal.document.domain.Storage;
import com.codenal.document.repository.DocumentRepository;
import com.codenal.document.repository.DocumentSharedUsersRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    
    @Autowired
    private DocumentSharedUsersRepository documentSharedUsersRepository;
    
    private static final int STATUS_PERSONAL = 0; // 개인 문서
    private static final int STATUS_SHARED = 1;  // 공유 문서
    private static final int STATUS_TRASH = 2;   // 휴지통 문서
    private static final int STATUS_PERSONAL_FAVORITE = 3;   // 개인문서 + 즐겨찾기
    private static final int STATUS_SHARED_FAVORITE = 4;   // 공유문서 + 즐겨찾기
    
    private Long getCurrentUserId() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String empIdString = authentication.getName();  // 현재 사용자의 empId를 가져옴
	    return Long.parseLong(empIdString);  // empId를 Long 타입으로 변환하여 반환
	}

    // 모든 문서를 조회하는 메서드
    public List<DocumentDto> getDocumentList() {
        List<Documents> documents = documentRepository.findAll();
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 문서를 저장하는 메서드
    public void saveDocument(DocumentDto documentDto) {
        Documents document = Documents.builder()
                .docName(documentDto.getDocName())
                .docNewName(documentDto.getDocNewName())
                .docStatus(documentDto.getDocStatus())
                .docCreatedDate(documentDto.getDocCreatedDate())
                .docPath(documentDto.getDocPath())
                .docSize(documentDto.getDocSize())
                .docEmpId(documentDto.getDocEmpId())
                .build();

        documentRepository.save(document);
    }

    // 키워드(파일명)로만 문서를 검색하는 메서드
    public List<DocumentDto> searchDocumentsByKeyword(String keyword) {
        List<Documents> documents = documentRepository.findByDocNameContainingIgnoreCase(keyword);
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

 // 문서 상태를 2로 변경하여 휴지통으로 이동시키는 메서드
    public void moveDocumentsToTrash(List<Long> docIds) {
        List<Documents> documents = documentRepository.findAllById(docIds);
        documents.forEach(document -> document.setDocStatus(2)); // 모든 문서를 휴지통(2)로 이동
        documentRepository.saveAll(documents);
    }

    // 상태별 문서 목록 조회
    public List<DocumentDto> getDocumentsByStatus(int status) {
        List<Documents> documents = documentRepository.findByDocStatus(status);
        return documents.stream()
                .map(DocumentDto::fromEntity)
                .collect(Collectors.toList());
    }

   
    // 문서를 복원하는 메서드 (휴지통에서 원래 상태로 복원)
    @Transactional
    public void restoreDocuments(List<Long> docIds) {
        List<Documents> documents = documentRepository.findAllById(docIds);
        for (Documents document : documents) {
            // 문서가 공유된 상태인지 확인
            boolean isShared = documentSharedUsersRepository.existsByDocSharedNo(document.getDocNo());
            
            if (isShared) {
                // 공유 문서로 복원
                // 즐겨찾기 상태도 유지
                if (document.getDocStatus() == STATUS_SHARED_FAVORITE) {
                    document.setDocStatus(STATUS_SHARED_FAVORITE);
                } else {
                    document.setDocStatus(STATUS_SHARED);
                }
            } else {
                // 개인 문서로 복원
                // 즐겨찾기 상태도 유지
                if (document.getDocStatus() == STATUS_PERSONAL_FAVORITE) {
                    document.setDocStatus(STATUS_PERSONAL_FAVORITE);
                } else {
                    document.setDocStatus(STATUS_PERSONAL);
                }
            }
        }
        documentRepository.saveAll(documents);
    }


    // 문서를 영구적으로 삭제하는 메서드 (휴지통에서 완전히 삭제)
    public void deleteDocumentsPermanently(List<Long> docIds) {
        documentRepository.deleteAllById(docIds); // 문서 영구 삭제
    }
    
    public List<DocumentDto> getDocumentsByStatusNot(int status) {
        List<Documents> documents = documentRepository.findAllByDocStatusNot(status);
        return documents.stream()
                        .map(DocumentDto::fromEntity)
                        .collect(Collectors.toList());
    }
    
    // 공유한 문서 조회
    public List<DocumentDto> getDocumentsSharedByMe(Long empId) {
        List<Documents> documents = documentRepository.findByDocEmpIdAndDocStatus(empId, 1);  // 상태 1: 공유 문서
        return documents.stream()
                        .map(DocumentDto::fromEntity)
                        .collect(Collectors.toList());
    }

 // 공유받은 문서 조회
    public List<DocumentDto> getDocumentsSharedWithMe(Long empId) {
        // DocumentSharedUsers 테이블을 통해 공유된 문서 ID를 가져옴
        List<DocumentSharedUsers> sharedUsers = documentSharedUsersRepository.findByDocSharedWithEmpId(empId);
        List<Documents> documents = sharedUsers.stream()
                .map(DocumentSharedUsers::getDocuments)
                .collect(Collectors.toList());

        // 문서에서 등록자의 정보 (employee)를 제대로 로드하여 DTO로 변환
        return documents.stream()
                .map(document -> {
                    // 여기서 employee를 확인하고 uploaderName을 설정
                    String uploaderName = (document.getEmployee() != null) ? document.getEmployee().getEmpName() : "Unknown";
                    DocumentDto documentDto = DocumentDto.fromEntity(document);
                    documentDto.setUploaderName(uploaderName);  // uploaderName을 설정
                    return documentDto;
                })
                .collect(Collectors.toList());
    }
    
    
 // 파일 ID로 문서를 조회하는 메서드
    public Documents getDocumentById(Long docId) {
        return documentRepository.findById(docId)
            .orElseThrow(() -> new RuntimeException("해당 문서를 찾을 수 없습니다: " + docId));
    }
    
    
    public Page<Documents> getDocumentsByStatus(int docStatus, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return documentRepository.findByDocStatus(docStatus, pageable);
    }
 
    
    @Transactional
    public void toggleFavoriteDocuments(List<Long> docIds) throws Exception {
        List<Documents> documents = documentRepository.findAllById(docIds);
        for (Documents doc : documents) {
            if (doc.getDocStatus() == 2) { // 휴지통에 있는 문서는 즐겨찾기할 수 없음
                throw new Exception("휴지통에 있는 문서는 즐겨찾기할 수 없습니다.");
            }
            switch (doc.getDocStatus()) {	
                case 0: // 개인문서
                    doc.setDocStatus(3); // 개인문서 + 즐겨찾기
                    break;
                case 1: // 공유문서
                    doc.setDocStatus(4); // 공유문서 + 즐겨찾기
                    break;
                case 3: // 개인문서 + 즐겨찾기
                    doc.setDocStatus(0); // 개인문서로 되돌리기
                    break;
                case 4: // 공유문서 + 즐겨찾기
                    doc.setDocStatus(1); // 공유문서로 되돌리기
                    break;
                default:
                    throw new Exception("지원되지 않는 문서 상태입니다.");
            }
        }
        documentRepository.saveAll(documents);
    }

    // 즐겨찾기 문서 조회
    public Page<DocumentDto> getFavoriteDocuments(Long empId, Pageable pageable) {
        List<Integer> statuses = Arrays.asList(STATUS_PERSONAL_FAVORITE, STATUS_SHARED_FAVORITE); // 3, 4
        Page<Documents> documentsPage = documentRepository.findByDocEmpIdAndDocStatusIn(empId, statuses, pageable);
        return documentsPage.map(DocumentDto::fromEntity);
    }

    @Transactional
    public void shareDocuments(List<Long> docIds, List<Long> empIds) {
        List<Documents> documents = documentRepository.findAllById(docIds);
        List<Documents> updatedDocuments = new ArrayList<>();

        for (Documents document : documents) {
            int currentStatus = document.getDocStatus();
            boolean statusChanged = false;

            // 현재 docStatus에 따라 적절히 업데이트
            if (currentStatus == 0) { // 0: 개인문서
                document.setDocStatus(1); // 1: 공유문서
                statusChanged = true;
            } else if (currentStatus == 3) { // 3: 개인문서 + 즐겨찾기
                document.setDocStatus(4); // 4: 공유문서 + 즐겨찾기
                statusChanged = true;	
            }
            // 이미 공유된 문서 (1 또는 4)는 상태 변경하지 않음

            if (statusChanged) {
                updatedDocuments.add(document);
            }

            // 공유 대상자 관리
            for (Long empId : empIds) {
                DocumentSharedUsersId id = new DocumentSharedUsersId(document.getDocNo(), empId);
                if (!documentSharedUsersRepository.existsById(id)) {
                    DocumentSharedUsers sharedUser = DocumentSharedUsers.builder()
                        .docSharedNo(document.getDocNo())
                        .docSharedWithEmpId(empId)
                        .documents(document)
                        .build();
                    documentSharedUsersRepository.save(sharedUser);
                }
            }
        }

        // 변경된 문서들을 한 번에 저장
        if (!updatedDocuments.isEmpty()) {
            documentRepository.saveAll(updatedDocuments);
        }
    }
    
    // 공유된 사용자 ID들을 가져오는 메서드 추가
    public List<Long> findSharedUsersByDocIds(List<Long> docIds) {
        return documentRepository.findSharedUsersByDocIds(docIds);
    }
    
    public long getUsedStorage(Long empId) {
        // 개인 문서와 공유 문서의 크기를 합산하여 long으로 반환
        Long totalUsedStorage = documentRepository.sumDocSize(empId);
        return totalUsedStorage != null ? totalUsedStorage : 0L;
    }

    // 고정된 총 용량을 반환하는 메서드 (MB 단위)
    public long getTotalStorage() {
        return 100L * 1024 * 1024 * 1024; // 300GB를 바이트 단위로 변환
    }

    // Storage 객체를 반환하는 메서드
    public Storage getStorageInfo(Long empId) {
        long usedStorage = getUsedStorage(empId);   // 바이트 단위
        long totalStorage = getTotalStorage();      // 바이트 단위
        return new Storage(usedStorage, totalStorage);
    }
}
	
    
    

