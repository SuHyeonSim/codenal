package com.codenal.document.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor  
public class Storage {
    private long usedStorage; 
    private long totalStorage; 
}
