package com.codenal.document.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class ShareRequest {

	
	
	private List<Long> docIds;
    private List<Long> empIds;

}
