package com.codenal.document.domain;
import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;




@Getter
@Setter
public class DocumentSharedUsersId implements Serializable {
    private Long docSharedNo;
    private Long docSharedWithEmpId;

    // 기본 생성자
    public DocumentSharedUsersId() {
    }

    public DocumentSharedUsersId(Long docSharedNo, Long docSharedWithEmpId) {
        this.docSharedNo = docSharedNo;
        this.docSharedWithEmpId = docSharedWithEmpId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DocumentSharedUsersId)) return false;
        DocumentSharedUsersId that = (DocumentSharedUsersId) o;
        return Objects.equals(docSharedNo, that.docSharedNo) &&
               Objects.equals(docSharedWithEmpId, that.docSharedWithEmpId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docSharedNo, docSharedWithEmpId);
    }
}

