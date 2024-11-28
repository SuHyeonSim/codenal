package com.codenal.document.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class DocumentDto {

    private Long docNo;
    private String docName;
    private String docNewName;
    private int docStatus;
    private Date docCreatedDate;
    private String docPath;
    private BigDecimal docSize;
    private Long docEmpId;
    private String uploaderName; 

    // 엔터티를 DTO로 변환하는 메서드
    public static DocumentDto fromEntity(Documents documents) {
        return DocumentDto.builder()
                .docNo(documents.getDocNo())
                .docName(documents.getDocName())
                .docNewName(documents.getDocNewName())
                .docStatus(documents.getDocStatus())
                .docCreatedDate(documents.getDocCreatedDate())
                .docPath(documents.getDocPath())
                .docSize(documents.getDocSize())
                .docEmpId(documents.getDocEmpId())
                .uploaderName(documents.getEmployee() != null ? documents.getEmployee().getEmpName() : "Unknown")  // 직원 이름 설정
                .build();
    }

    // DTO를 엔터티로 변환하는 메서드
    public Documents toEntity() {
        return Documents.builder()
                .docNo(docNo)
                .docName(docName)
                .docNewName(docNewName)
                .docStatus(docStatus)
                .docCreatedDate(docCreatedDate)
                .docPath(docPath)
                .docSize(docSize)
                .docEmpId(docEmpId)
                .build();
    }
}
