package com.codenal.announce.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.announce.domain.Announce;
import com.codenal.announce.domain.AnnounceDto;
import com.codenal.announce.domain.AnnounceFileDto;
import com.codenal.announce.service.AnnounceService;
import com.codenal.announce.service.FileService;

@Controller
public class AnnounceApiController {

	private final AnnounceService announceService;
	private final FileService fileService;

	@Autowired
	public AnnounceApiController(AnnounceService announceService, FileService fileService) {
		this.announceService = announceService;
		this.fileService = fileService;
	}

	@ResponseBody
	@PostMapping("/announce/createEnd")
	public Map<String, String> createAnnounce(AnnounceDto dto,
	        @RequestParam(name= "selectedDeptIds", required=false) String selectedDeptIds,
	        @RequestParam(name= "selectedJobIds", required=false) String selectedJobIds,
	        @RequestParam(name = "file", required = false) List<MultipartFile> files) {

	    Map<String, String> resultMap = new HashMap<>();
	    List<AnnounceFileDto> fileDtos = new ArrayList<>();

	    // 부서 ID가 존재하는 경우에만 처리
	    List<Long> deptIds = (selectedDeptIds != null && !selectedDeptIds.isEmpty()) 
	        ? Arrays.stream(selectedDeptIds.split(","))
	                .map(Long::parseLong)  // String을 Long으로 변환
	                .collect(Collectors.toList())
	        : new ArrayList<>();  // 부서 ID가 없을 경우 빈 리스트 반환

	    // 직급 ID가 존재하는 경우에만 처리
	    List<Integer> jobIds = (selectedJobIds != null && !selectedJobIds.isEmpty()) 
	        ? Arrays.stream(selectedJobIds.split(","))
	                .map(Integer::parseInt)  // String을 Long으로 변환
	                .collect(Collectors.toList())
	        : new ArrayList<>();  // 직급 ID가 없을 경우 빈 리스트 반환
	    

	        if (files != null && !files.isEmpty()) {
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    String savedFileName = fileService.upload(file);
	                    if (savedFileName == null) {
	                        resultMap.put("res_code", "500");
	                        resultMap.put("res_msg", "파일 업로드에 실패했습니다.");
	                        return resultMap;
	                    }

	                    AnnounceFileDto fileDto = AnnounceFileDto.builder()
	                            .file_ori_name(file.getOriginalFilename())
	                            .file_new_name(savedFileName)
	                            .file_path("C:\\codenal\\announce\\upload\\")
	                            .build();

	                    fileDtos.add(fileDto);
	                }
	            }

	            Announce savedAnnounce = announceService.createAnnounceAndFile(dto, fileDtos, deptIds, jobIds);
	            if (savedAnnounce == null) {
	                throw new RuntimeException("게시글 저장에 실패했습니다.");
	            }

	            resultMap.put("res_code", "200");
	            resultMap.put("res_msg", "성공적으로 게시글을 작성했습니다.");
	            resultMap.put("announceNo", String.valueOf(savedAnnounce.getAnnounceNo()));

	        } else {
	            Announce savedAnnounce = announceService.createAnnounce(dto, deptIds, jobIds);
	            if (savedAnnounce == null) {
	                throw new RuntimeException("게시글 저장에 실패했습니다.");
	            }

	            resultMap.put("res_code", "200");
	            resultMap.put("res_msg", "성공적으로 게시글을 작성했습니다.");
	            resultMap.put("announceNo", String.valueOf(savedAnnounce.getAnnounceNo()));
	        }

	    return resultMap;
	}

	@ResponseBody
	@DeleteMapping("/announce/delete/{announceNo}")
	public Map<String, String> deleteAnnounce(@PathVariable("announceNo") int announceNo) {
	    Map<String, String> resultMap = new HashMap<>();
	    resultMap.put("res_code", "404");
	    resultMap.put("res_msg", "게시글 삭제 중 오류가 발생했습니다.");

	    try {
	        AnnounceDto announce = announceService.selectAnnounceDetail(announceNo);
	        if (announce != null) {
	            // 게시글에 파일이 존재하는 경우에만 삭제
	            if (announce.getAnnounceFile() != null && !announce.getAnnounceFile().isEmpty()) {
	                int fileDeletionResult = fileService.deletefile(announceNo);
	                if (fileDeletionResult <= 0) {
	                    resultMap.put("res_msg", "기존 파일 삭제에 실패했습니다.");
	                    return resultMap;
	                }
	            }

	            // 게시글 삭제
	            int announceDeletionResult = announceService.deleteAnnounce(announceNo);
	            if (announceDeletionResult > 0) {
	                resultMap.put("res_code", "200");
	                resultMap.put("res_msg", "정상적으로 게시글이 삭제되었습니다.");
	            } else {
	                resultMap.put("res_msg", "게시글 삭제에 실패했습니다.");
	            }
	        } else {
	            resultMap.put("res_msg", "게시글을 찾을 수 없습니다.");
	        }
	    } catch (Exception e) {
	        resultMap.put("res_code", "500");
	        resultMap.put("res_msg", "서버 오류: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return resultMap;
	}
	
	
	
	@ResponseBody
	@PostMapping("/announce/updateEnd/{announceNo}")
	public Map<String, String> updateAnnounce(AnnounceDto dto,
			@RequestParam(name = "selectedDeptIds", required = false) String selectedDeptIds,
	        @RequestParam(name = "selectedJobIds", required = false) String selectedJobIds,
	        @RequestParam(name ="file", required = false) List<MultipartFile> files) {
		System.out.println(files);
	    List<AnnounceFileDto> fileDtos = new ArrayList<>();
	    Map<String, String> resultMap = new HashMap<>();

	    // 부서 ID가 존재하는 경우에만 처리
	    List<Long> deptIds = (selectedDeptIds != null && !selectedDeptIds.isEmpty()) 
	        ? Arrays.stream(selectedDeptIds.split(","))
	                .map(Long::parseLong)  // String을 Long으로 변환
	                .collect(Collectors.toList())
	        : new ArrayList<>();  // 부서 ID가 없을 경우 빈 리스트 반환

	    // 직급 ID가 존재하는 경우에만 처리
	    List<Integer> jobIds = (selectedJobIds != null && !selectedJobIds.isEmpty()) 
	        ? Arrays.stream(selectedJobIds.split(","))
	                .map(Integer::parseInt)  // String을 Long으로 변환
	                .collect(Collectors.toList())
	        : new ArrayList<>();  // 직급 ID가 없을 경우 빈 리스트 반환
	    
	    if (files != null && !files.isEmpty()) {
	        // 파일이 있을 때 처리 로직
	        for (MultipartFile file : files) {
	            System.out.println(file);
	            if (!file.isEmpty()) { // 파일이 실제로 비어있지 않은 경우
	                // 파일 처리 로직
	                System.out.println("File name: " + file.getOriginalFilename());

	                try {
	                    // 파일 업로드 처리
	                    String savedFileName = fileService.upload(file);
	                    if (savedFileName == null) {
	                        resultMap.put("res_code", "500");
	                        resultMap.put("res_msg", "파일 업로드에 실패했습니다.");
	                        return resultMap;
	                    }

	                    // AnnounceFileDto 생성
	                    AnnounceFileDto fileDto = new AnnounceFileDto();
	                    fileDto.setFile_ori_name(file.getOriginalFilename());
	                    fileDto.setFile_new_name(savedFileName);
	                    fileDto.setFile_path("C:\\codenal\\announce\\upload\\");
	                    fileDtos.add(fileDto);
	                } catch (Exception e) {
	                    resultMap.put("res_code", "500");
	                    resultMap.put("res_msg", "서버 오류: " + e.getMessage());
	                    e.printStackTrace();
	                    return resultMap;
	                }
	            }
	        }

	        // 모든 파일 처리 후 Announce 및 AnnounceFile 저장
	        try {
	            Announce savedAnnounce = announceService.updateAnnounceAndFile(dto, fileDtos, deptIds, jobIds);
	            if (savedAnnounce == null) {
	                resultMap.put("res_code", "500");
	                resultMap.put("res_msg", "게시글 수정에 실패했습니다.");
	                return resultMap;
	            }

	            // 성공 응답
	            resultMap.put("res_code", "200");
	            resultMap.put("res_msg", "성공적으로 게시글을 수정했습니다.");
	            resultMap.put("announceNo", String.valueOf(savedAnnounce.getAnnounceNo()));
	        } catch (Exception e) {
	            resultMap.put("res_code", "500");
	            resultMap.put("res_msg", "서버 오류: " + e.getMessage());
	            e.printStackTrace();
	            return resultMap;
	        }
	    } else {
	        // 파일이 없을 때 단일 Announce 저장
	        Announce savedAnnounce = announceService.updateAnnounce(dto, deptIds, jobIds);
	        if (savedAnnounce == null) {
	            resultMap.put("res_code", "500");
	            resultMap.put("res_msg", "게시글 수정에 실패했습니다.");
	            return resultMap;
	        }

	        // 성공 응답
	        resultMap.put("res_code", "200");
	        resultMap.put("res_msg", "성공적으로 게시글을 수정했습니다.");
	        resultMap.put("announceNo", String.valueOf(savedAnnounce.getAnnounceNo()));
	    }

	    return resultMap;
	}

	@PostMapping("/announce/deleteFile")
	public ResponseEntity<?> deleteFile(@RequestBody Map<String, Integer> request) {
	    Integer fileId = request.get("fileId");
	        // 파일 삭제 로직 수행
	        announceService.deleteFile(fileId);
	        return ResponseEntity.ok("파일이 삭제되었습니다.");
	}
	
	
	@GetMapping("/download/{no}")
	public ResponseEntity<Object> announceFileDownload(
		@PathVariable("no") int file_no){
			return fileService.announceFiledownload(file_no);
		}


}
