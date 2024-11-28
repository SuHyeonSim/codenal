package com.codenal.meeting.service;

import java.io.File;
import java.net.URLDecoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.repository.MeetingRoomRepository;


@Service
public class MeetingRoomImgFileService {
	
	private String uploadDir = "C:\\meetingRoomImg\\upload\\";
	private MeetingRoomRepository meetingRoomRepository;
	
	@Autowired
	public MeetingRoomImgFileService(MeetingRoomRepository meetingRoomRepository) {
		this.meetingRoomRepository = meetingRoomRepository;
	}
	
	// 파일 삭제
	public int delete(Long roomNo){
		int result = 0;
		
		try {
			MeetingRoom mr = meetingRoomRepository.findByMeetingRoomNo(roomNo);
			String imgPath = mr.getMeetingRoomImg();
			String resultDir = uploadDir + URLDecoder.decode(imgPath, "UTF-8");
			
			if(imgPath != null || !imgPath.equals("")) {
				
				if(resultDir != null && resultDir.isEmpty() == false) {
					File file = new File(resultDir);
					
					if(file.exists()) {
						file.delete();
						result = 1;
					} else {
						result = 0;
					}
					
				}
			} else {
				result = 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	// 파일 업로드
	public String Img(MultipartFile img) {
		
		String newFileName = "";
		if(!img.getOriginalFilename().equals("")) {
			
			try {
				
				String fileOriName = img.getOriginalFilename();
				
				String fileExtension = fileOriName.substring(fileOriName.lastIndexOf("."),fileOriName.length());
				
				UUID uuid = UUID.randomUUID();
				
				String uniqueName = uuid.toString().replaceAll("-", "");
				
				newFileName = uniqueName+fileExtension;
				
//			String uploadDir = "src\\main\\resources\\static\\assets\\images";
				
				
				File saveFile = new File(uploadDir+"\\"+uniqueName+fileExtension);
				
				if(!saveFile.exists()) {
					saveFile.mkdirs();
				}
				
				img.transferTo(saveFile);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return newFileName;
		
	}

}
