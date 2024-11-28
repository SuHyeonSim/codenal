package com.codenal.announce.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceFile;
import com.codenal.announce.repository.AnnounceFileRepository;
import com.codenal.announce.repository.AnnounceRepository;

import jakarta.transaction.Transactional;

@Service
public class FileService {
	
	private final AnnounceRepository announceRepository;
	private final AnnounceFileRepository announceFileRepository;
	
	@Autowired
	public FileService(AnnounceRepository announceRepository, AnnounceFileRepository announceFileRepository) {
		this.announceRepository = announceRepository;
		this.announceFileRepository = announceFileRepository;
	}
	
	private String fileDir = "C:\\codenal\\announce\\upload\\";

	public String upload(MultipartFile file) {

		String newFileName = null;

			// 1. 파일 원래 이름
			String oriFileName = file.getOriginalFilename();
			// 2. 파일 자르기
			String fileExt = oriFileName.substring(oriFileName.lastIndexOf("."),oriFileName.length());
			// 3. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID();
			// 4. 8자리마다 포함되는 하이픈 제거
			String uniqueName = uuid.toString().replace("-", "");
			// 5. 새로운 파일명
			newFileName = uniqueName+fileExt;
			// 6. 파일 저장 경로 설정 -> 상단에 위치함
			// 7. 파일 껍데기 생성
			File saveFile = new File(fileDir+newFileName);
			// 8. 경로 존재 여부 확인
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// 9. 껍데기에 파일 정보 복제
			try {
				file.transferTo(saveFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
		return newFileName;
		
	}

	@Transactional
	public int deletefile(int announceNo) {    // announce 삭제시
	    int result = -1;

	    try {
	        Announce announce = announceRepository.findByAnnounceNo(announceNo);
	        if (announce == null || announce.getFiles().isEmpty()) {
	            return result; // Announce 또는 파일이 없는 경우
	        }

	        // 파일 처리
	        
	        for (AnnounceFile file : announce.getFiles()) {
	            String newFileName = file.getFileNewName();
	            String oriFileName = file.getFileOriName();
	            String resultDir = fileDir + newFileName;
	            System.out.println("디코딩된 파일 경로: " + resultDir);

	        if (resultDir != null && !resultDir.isEmpty()) {
	            File fileToDelete  = new File(resultDir);
	            if (fileToDelete .exists()) {
	                if (fileToDelete .delete()) {
	                    result = 1; // 삭제 성공
	                } else {
	                    result = 0; // 삭제 실패
	                    System.err.println("파일 삭제에 실패했습니다: " + resultDir);
	                }
	            } else {
	                result = 0; // 파일이 존재하지 않음
	                System.err.println("파일이 존재하지 않습니다: " + resultDir);
	            }
	        }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = -1; // 예외 발생
	    }

	    return result;
	}
	
	
	
	public ResponseEntity<Object> announceFiledownload(int file_no) {
	    try {
	        AnnounceFile a = announceFileRepository.findById(file_no)
	            .orElseThrow(() -> new FileNotFoundException("파일을 찾을 수 없습니다: " + file_no));
	        
	        String newFileName = a.getFileNewName();
	        String oriFileName = URLEncoder.encode(a.getFileOriName(), "UTF-8");
	        String downDir = fileDir + newFileName;
	        
	        Path filePath = Paths.get(downDir);
	        Resource resource = new InputStreamResource(Files.newInputStream(filePath));
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(oriFileName).build());
	        
	        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
	    }
	}
	
}
