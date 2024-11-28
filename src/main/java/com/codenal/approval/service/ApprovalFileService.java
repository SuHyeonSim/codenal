package com.codenal.approval.service;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalFile;
import com.codenal.approval.domain.ApprovalFileDto;
import com.codenal.approval.repository.ApprovalFileRepository;
import com.codenal.approval.repository.ApprovalRepository;

import jakarta.transaction.Transactional;

@Service
public class ApprovalFileService {
	
	private String fileDir = "C:\\codenal\\approval\\upload\\";
	
	private final ApprovalFileRepository approvalFileRepository;
	private final ApprovalRepository approvalRepository;
	
	@Autowired
	public ApprovalFileService(ApprovalFileRepository approvalFileRepository,
			ApprovalRepository approvalRepository) {
		this.approvalFileRepository = approvalFileRepository;
		this.approvalRepository = approvalRepository;
	}
	
	// 파일 업로드
	public String upload(MultipartFile file, Approval approval) {
		
		if (file == null || file.isEmpty()) {
	        return null;
	    }
		
		String newFileName = null;
		
		try {
			String oriFileName = file.getOriginalFilename();
			String fileExt = oriFileName.substring(oriFileName.lastIndexOf("."),oriFileName.length());
			// 파일 명칭바꾸기
			UUID uuid = UUID.randomUUID();
			String uniqueName = uuid.toString().replace("-", "");
			newFileName = uniqueName+fileExt;
			File saveFile = new File(fileDir+newFileName);
			
			// 경로존재확인
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			
			file.transferTo(saveFile);
			
			ApprovalFile dto = ApprovalFile.builder()
									.approval(approval)
									.fileOriName(oriFileName)
									.fileNewName(newFileName)
									.filePath(fileDir)
									.build();
			
			
			ApprovalFile af = approvalFileRepository.save(dto);			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return newFileName;
	}
	
	// 파일 다운로드
	public ResponseEntity<Object> download(Long approvalNo){
		try {
			ApprovalFile a = approvalFileRepository.findByApprovalApprovalNo(approvalNo);
			
			String oriName = URLEncoder.encode(a.getFileOriName(),"UTF-8");
			String newName = a.getFileNewName();
			String downDir = a.getFilePath()+newName;
			
			Path filePath = Paths.get(downDir);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(oriName).build());
			
			return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
			
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
		}
	}
	
	// 수정 ( 폴더 안에 파일 삭제 후 파일 데이터 삭제)
	@Transactional
	public int  deleteFile(Long no) {
		int result = -1;
		System.out.println(no);
		try {
			
			ApprovalFile af = approvalFileRepository.findByApprovalApprovalNo(no);
			
			if(af != null) {
				String newFileName = af.getFileNewName();
				String resultDir = fileDir + URLDecoder.decode(newFileName,"UTF-8");
				
				if(resultDir != null && resultDir.isEmpty() ==false) {
					File file = new File(resultDir);
					if(file.exists()) {
						file.delete();
					}
				}	
			}
			
			result = approvalFileRepository.deleteByApprovalNo(no);
			
		}catch(Exception e) {
			e.printStackTrace();		
		}
		return result;
	}
}