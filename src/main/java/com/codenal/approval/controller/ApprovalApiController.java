package com.codenal.approval.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.domain.ApprovalFileDto;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.service.ApprovalFileService;
import com.codenal.approval.service.ApprovalService;
import com.codenal.approval.service.ApproverService;
import com.codenal.employee.service.EmployeeService;

@Controller
public class ApprovalApiController {

	private final ApprovalService approvalService;
	private final ApprovalFileService approvalFileService;
	private final EmployeeService employeeService;
	private final ApproverService approverService;
	
	@Autowired
	public ApprovalApiController(ApprovalService approvalService, ApprovalFileService approvalFileService,
			EmployeeService employeeService, ApproverService approverService) {
		this.approvalService = approvalService;
		this.approvalFileService = approvalFileService;
		this.employeeService = employeeService;
		this.approverService = approverService;
	}

	// 전자결재 등록 (요청서, 품의서)
	@ResponseBody
	@PostMapping("/approval")
	public Map<String, String> createApproval(@RequestPart("approval_content") String approvalContent,
			@RequestParam("approval_title") String approvalTitle, @RequestParam("emp_id") String empId,
			@RequestParam("approval_reg_date") String approvalRegDate, @RequestParam("form_code") String formCode,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "agree", required = false)List<Long> agree, @RequestParam("approver")List<Long> approver,
			@RequestParam(value = "references", required = false) List<Long> references) {
		Map<String, String> resultMap = new HashMap<String, String>();
		System.out.println("시작");
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
		
		approvalContent = approvalContent.replaceAll("<hr\\s*/?>", "<br>"); // <hr>를 <br>로 변환
		
		
		// 타입 형변환
		Long emp_id = Long.parseLong(empId);
		Integer form_code = Integer.parseInt(formCode);

		Map<String, Object> list = new HashMap<String, Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", emp_id);
		list.put("폼코드", form_code);

		Approval createdApproval = approvalService.createApproval(list);

		if (createdApproval != null) {
			
			// 결재자 테이블로 넘기기
			Map<String, List<Long>> approverList = new HashMap<String,List<Long>>();
			approverList.put("합의자", agree);
			approverList.put("결재자", approver);
			
			// 결재자 정보 등록
			approverService.createApprover(approverList,createdApproval);
			
			// 수신참조자 정보 등록
			approverService.createReferences(references,createdApproval);
			
			
			//파일 데이터
			if (file != null) {
				if (approvalFileService.upload(file, createdApproval) != null) {
					resultMap.put("res_code", "200");
					resultMap.put("res_msg", "전자결재 등록 성공 하였습니다.");
				}
			}
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "전자결재 등록 성공 하였습니다.");

		}
		return resultMap;
	}

	// 전자결재 등록 (휴가신청서)
	@ResponseBody
	@PostMapping("/approval_leave")
	public Map<String, String> createApprovalLeave(@RequestParam("approval_content") String approvalContent,
			@RequestParam("approval_title") String approvalTitle, 
			@RequestParam("emp_id") String empId,
			@RequestParam("approval_reg_date") String approvalRegDate, 
			@RequestParam("start_date") LocalDate startDate,
			@RequestParam(value = "end_date", required = false) LocalDate endDate,
			@RequestParam("form_code") String formCode,
			@RequestParam(value = "time_period", required = false) String timePeriod,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "agree", required = false)List<Long> agree, @RequestParam("approver")List<Long> approver,
			@RequestParam(value = "references", required = false) List<Long> references,
			@RequestParam(value="totalDay") Double totalDay) {

		Map<String, String> resultMap = new HashMap<String, String>();
		System.out.println("시작");
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 등록 중 오류가 발생했습니다.");
		
		
		approvalContent = approvalContent.replaceAll("<hr\\s*/?>", "<br>"); // <hr>를 <br>로 변환
		
		
		// 타입 형변환
		Long emp_id = Long.parseLong(empId);
		Integer form_code = Integer.parseInt(formCode);

		Map<String, Object> list = new HashMap<String, Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", emp_id);
		list.put("폼코드", form_code);
		list.put("시작일자", startDate);
		list.put("종료일자", endDate);
		list.put("사용일수", totalDay);
		list.put("반차시간대", timePeriod);
		Approval createdApproval = approvalService.createApprovalLeave(list);

		if (createdApproval != null) {
			
		// 결재자 테이블로 넘기기
			Map<String, List<Long>> approverList = new HashMap<String,List<Long>>();
			approverList.put("합의자", agree);
			approverList.put("결재자", approver);
						
			Map<String, Long> no = new HashMap<String,Long>();
			no.put("결재번호", createdApproval.getApprovalNo());
						
			approverService.createApprover(approverList,createdApproval);	
			
			// 수신참조자 정보 등록
			approverService.createReferences(references,createdApproval);

			if (file != null) {
				if (approvalFileService.upload(file, createdApproval) != null) {
					resultMap.put("res_code", "200");
					resultMap.put("res_msg", "등록 성공하였습니다. 리스트로 이동합니다.");
				}
			}
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "등록 성공하였습니다. 리스트로 이동합니다.");
		}
		return resultMap;
	}

	// 파일 다운로드
	@GetMapping("/approval/download/{approval_no}")
	public ResponseEntity<Object> approvalImgDownload(@PathVariable("approval_no") Long approvalNo) {
		return approvalFileService.download(approvalNo);
	}

	// 상신함 대기 -> 회수로 수정
	@ResponseBody // html을 반환하지 않고 JSON 응답을 반환
	@PostMapping("/approval/revoke")
	public Map<String, String> revokeApproval(@RequestBody Map<String, String> request) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "404");
		String no = request.get("approvalNo");
		Long num = Long.parseLong(no);

		System.out.println("번호 : " + num);

		if (approvalService.revoke(num) > 0) {
			System.out.println("회수 이동");
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "회수함으로 이동합니다.");
		}

		return resultMap;
	}

	// 결재서 수정 (품의서, 요청서)
	@ResponseBody
	@PutMapping("/approval/update/{approvalNo}")
	public Map<String, String> updateApproval(@RequestParam("approval_content") String approvalContent,
			@RequestParam("approval_title") String approvalTitle, @RequestParam("emp_id") Long empId,
			@RequestParam("approval_reg_date") String approvalRegDate, @RequestParam("form_code") String formCode,
			@PathVariable("approvalNo") Long no, @RequestPart(name = "file", required = false) MultipartFile file,
			@RequestParam(value = "agree", required = false)List<Long> agree, @RequestParam("approver")List<Long> approver,
			@RequestParam(value = "references", required = false) List<Long> references) {
		
		
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 수정 중 오류가 발생했습니다.");
		
		approvalContent = approvalContent.replaceAll("<hr\\s*/?>", "<br>"); // <hr>를 <br>로 변환
		
		// 날짜 최신 날짜로 수정
		LocalDateTime ldt = LocalDateTime.now();

		// 타입 형변환
		Integer form_code = Integer.parseInt(formCode);

		Map<String, Object> list = new HashMap<String, Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", empId);
		list.put("폼코드", form_code);
		list.put("날짜", ldt);

		Approval updateApproval = approvalService.updateApproval(list, no);

		System.out.println("파일 수정 : " + file.getName());

		if (updateApproval != null) {
			
			// 결재자 테이블로 수정하기
			Map<String, List<Long>> approverList = new HashMap<String,List<Long>>();
			approverList.put("합의자", agree);
			approverList.put("결재자", approver);
			
			System.out.println("Updated Approval: " + updateApproval);
			System.out.println("Approver List: " + approverList);
			
			// 결재 수정
			approverService.updateApprover(approverList,updateApproval);
			
			// 수신 참조자 수정
			approverService.updateReferrer(references,updateApproval);
			
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "대기함으로 이동합니다.");
			ApprovalFileDto alf = new ApprovalFileDto();
			alf.setApproval(updateApproval);

			int deleteFile = approvalFileService.deleteFile(no);

			// 기존 파일 삭제 => 1 , 기존에 파일이 없으면 => 0
			if (deleteFile == 0 || deleteFile == 1) {
				if (file != null && "".equals(file.getOriginalFilename()) == false) {

					String savedFileName = approvalFileService.upload(file, updateApproval);
					if (savedFileName == null) {
						resultMap.put("res_code", "200");
						resultMap.put("res_msg", "전자결재 상신대기함으로 이동합니다.");
						return resultMap;
					}
				}
			} else {
				resultMap.put("res_code", "404");
				resultMap.put("res_msg", "전자결재 파일 삭제 중 오류가 발생했습니다.");
			}

		} else {
			resultMap.put("res_code", "404");
			resultMap.put("res_msg", "전자결재 수정 중 오류가 발생했습니다.");

		}
		return resultMap;
	}

	// 결재서 수정(근태 신청서)
	@ResponseBody
	@PutMapping("/approval/leaveUpdate/{approvalNo}")
	public Map<String, String> updateLeaveApproval(@RequestParam("approval_content") String approvalContent,
			@RequestParam("approval_title") String approvalTitle, @RequestParam("emp_id") Long empId,
			@RequestParam("approval_reg_date") String approvalRegDate, @RequestParam("start_date") LocalDate startDate,
			@RequestParam(value = "end_date", required = false) LocalDate endDate,
			@RequestParam("form_code") String formCode,
			@RequestParam(value = "time_period", required = false) String timePeriod,
			@RequestParam(value = "file", required = false) MultipartFile file, @PathVariable("approvalNo") Long no,
			@RequestParam(value = "agree", required = false) List<Long> agree,
			@RequestParam("approver") List<Long> approver,
			@RequestParam(value = "references", required = false) List<Long> references,
			@RequestParam(value="totalDay") Double totalDay) {

		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 수정 중 오류가 발생했습니다.");
		
		approvalContent = approvalContent.replaceAll("<hr\\s*/?>", "<br>"); // <hr>를 <br>로 변환
		
		// 날짜 최신 날짜로 수정
		LocalDateTime ldt = LocalDateTime.now();
		

		// 타입 형변환
		Integer form_code = Integer.parseInt(formCode);

		System.out.println(empId);
		
		Map<String, Object> list = new HashMap<String, Object>();
		list.put("제목", approvalTitle);
		list.put("내용", approvalContent);
		list.put("이름", empId);
		list.put("폼코드", form_code);
		list.put("시작일자", startDate);
		list.put("종료일자", endDate);
		list.put("반차시간대", timePeriod);
		list.put("사용일수", totalDay);
		list.put("날짜", ldt);
		
		// 전자결재 수정
		Approval updateApproval = approvalService.updateApprovalLeave(list, no);

		System.out.println("파일 수정 : " + file.getName());

		if (updateApproval != null) {
			
			// 결재자 테이블 수정하기
			Map<String, List<Long>> approverList = new HashMap<String, List<Long>>();
			approverList.put("합의자", agree);
			approverList.put("결재자", approver);

			System.out.println("Updated Approval: " + updateApproval);
			System.out.println("Approver List: " + approverList);

			// 결재자 수정
			approverService.updateApprover(approverList, updateApproval);

			// 수신 참조자 수정
			approverService.updateReferrer(references, updateApproval);

			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "전자결재 상신대기함으로 이동합니다.");
			ApprovalFileDto alf = new ApprovalFileDto();
			alf.setApproval(updateApproval);

			int deleteFile = approvalFileService.deleteFile(no);

			// 기존 파일 삭제 => 1 , 기존에 파일이 없으면 => 0
			if (deleteFile == 0 || deleteFile == 1) {
				if (file != null && "".equals(file.getOriginalFilename()) == false) {

					String savedFileName = approvalFileService.upload(file, updateApproval);
					if (savedFileName == null) {
						resultMap.put("res_code", "404");
						resultMap.put("res_msg", "파일 업로드에 실패했습니다.");
						return resultMap;
					}
				}
			} else {
				resultMap.put("res_code", "404");
				resultMap.put("res_msg", "전자결재 파일 삭제 중 오류가 발생했습니다.");
			}

		} else {
			resultMap.put("res_code", "404");
			resultMap.put("res_msg", "전자결재 수정 중 오류가 발생했습니다.");

		}
		return resultMap;
	}

	// 전자결재 품의서, 요청서 삭제
	@ResponseBody
	@DeleteMapping("/approval/delete/{approvalNo}")
	public Map<String, String> deleteApproval(@PathVariable("approvalNo") Long no) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "전자결재 삭제 중 오류가 발생했습니다.");

		System.out.println("삭제중");
		if (approvalFileService.deleteFile(no) > 0) {
			resultMap.put("res_msg", "기존 파일이 정상적으로 삭제되었습니다.");
			}
		if (approvalService.deleteApproval(no) > 0) {
			resultMap.put("res_code", "200");
			resultMap.put("res_msg", "정상적으로 게시글이 삭제되었습니다.");
			}
		return resultMap;
		
	}
	
	// 전자결재 승인 (우선순위 순서대로)
	@ResponseBody
	@PostMapping("/approver/consent")
	public Map<String, String> consentApprover(@RequestBody Map<String, String> request) {
	    Map<String, String> resultMap = new HashMap<>();
	    resultMap.put("res_code", "404");

	    Long approvalNo = Long.valueOf(request.get("approvalNo"));
	    Long loginId = Long.valueOf(request.get("loginId"));

	    if (approverService.consentApprover(approvalNo, loginId) > 0) {
	        resultMap.put("res_code", "200");
	        resultMap.put("res_msg", "승인되었습니다.");
	    }

	    return resultMap;
	}
	
	
	// 전자결재 반려
	@ResponseBody
	@PostMapping("/approver/reject")
	public Map<String,String> rejectApprover(@RequestBody Map<String,String> request){
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("res_code", "404");
		resultMap.put("res_msg", "반려 중 오류가 발생하였습니다.");
		Long no = Long.valueOf(request.get("approvalNo"));
		Long loginId= Long.valueOf(request.get("loginId"));
		String rejectText = request.get("reject");
		
		if(approverService.rejectApprover(no,loginId,rejectText)>0) {
			resultMap.put("res_code","200");
			resultMap.put("res_msg", "반려처리되었습니다.");
		}
		
		return resultMap;
	}
	
	// 전자결재 비공개 설정
	@ResponseBody
	@PostMapping("/admin/updateVisibiliy")
	public void updateVisibility(@RequestBody Map<String,Object> request ) {
		System.out.println(1); 
		
		int id = Integer.parseInt((String) request.get("id")); // String -> int 변환
		boolean checked = (boolean) request.get("checked");

		int result = approvalService.updateVisibility(id,checked);
	}
	
	// 이름 조회
	@ResponseBody
	@PostMapping("/form_find")
	public Map<String,String> formFind(@RequestParam("title") String title){
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_msg", "존재하는 이름입니다.");
		result.put("res_code", "404");
		
		ApprovalForm form = approvalService.formFind(title);
		
		if(form == null) {
			result.put("res_code","200");
			
		}
		
		return result;
		
	}
	
	// 폼 생성
	@ResponseBody
	@PostMapping("/form_create")
	public Map<String,String> formCreate(@RequestParam("title") String title, @RequestParam("base_form_code") String code,
										@RequestParam("content") String content){
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_msg", "등록 실패하였습니다.");
		result.put("res_code", "404");
		
		
		if(approvalService.formCreate(title,code,content) != null) {
			result.put("res_code","200");
			result.put("res_msg", "등록되었습니다.");
		}
		
		return result;
	}
	
	//  폼 수정
	@ResponseBody
	@PostMapping("/admin/approvalForm_update")
	public Map<String,String> formUpdate(@RequestBody Map<String, Object> request){
		Map<String,String> result = new HashMap<String,String>();
		result.put("res_code","404");
		result.put("res_msg","수정 실패하였습니다.");
		
		int formCode = (int) request.get("formCode");
		
		System.out.println(formCode);
		
	    String content = (String) request.get("content");
	    
	    System.out.println(content);
		
		if(approvalService.formUpdate(formCode,content) != null) {
			result.put("res_code","200");
			result.put("res_msg","수정되었습니다.");
		}
		return result;
	}
	
		//승인된 연차 목록을 반환하는 api  ( 종우) 
	@ResponseBody
	@GetMapping("/api/approved-annual-leaves")
	public ResponseEntity<List<ApprovalDto>> getApprovedAnnualLeaves() {
	    List<ApprovalDto> approvedAnnualLeaves = approvalService.getApprovedAnnualLeaves();
	    return ResponseEntity.ok().body(approvedAnnualLeaves);
	}
	
	
}