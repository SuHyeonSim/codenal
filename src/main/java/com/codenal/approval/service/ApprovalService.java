package com.codenal.approval.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.codenal.alarms.domain.Alarms;
import com.codenal.alarms.repository.AlarmsRepository;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.repository.AnnualLeaveUsageRepository;
import com.codenal.approval.domain.Approval;
import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.domain.ApprovalCategory;
import com.codenal.approval.domain.ApprovalDto;
import com.codenal.approval.domain.ApprovalFile;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.domain.Referrer;
import com.codenal.approval.repository.ApprovalBaseFormTypeRepository;
import com.codenal.approval.repository.ApprovalCategoryRepository;
import com.codenal.approval.repository.ApprovalFileRepository;
import com.codenal.approval.repository.ApprovalFormRepository;
import com.codenal.approval.repository.ApprovalRepository;
import com.codenal.approval.repository.ApproverRepository;
import com.codenal.approval.repository.ReferrerRepository;
import com.codenal.attendance.service.AttendanceService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ApprovalService {

   private final ApprovalRepository approvalRepository;
   private final EmployeeRepository employeeRepository;
   private final ApprovalCategoryRepository approvalCategoryRepository;
   private final ApprovalFormRepository approvalFormRepository;
   private final ApprovalBaseFormTypeRepository approvalBaseFormTypeRepository;
   private final AnnualLeaveUsageRepository annualLeaveUsageRepository;
   private final ApprovalFileRepository approvalFileRepository;
   private final ApproverRepository approverRepository;
   private final ReferrerRepository referrerRepository;
   private final AlarmsRepository alarmsRepository;


   @Autowired
   public ApprovalService(ApprovalRepository approvalRepository, 
         EmployeeRepository employeeRepository,
         ApprovalCategoryRepository approvalCategoryRepository,
         ApprovalFormRepository approvalFormRepository,
         ApprovalBaseFormTypeRepository approvalBaseFormTypeRepository,
         AnnualLeaveUsageRepository annualLeaveUsageRepositroy,
         ApprovalFileRepository approvalFileRepository,
         ApproverRepository approverRepository,
         ReferrerRepository referrerRepository,
         AlarmsRepository alarmsRepository) {

      this.approvalRepository = approvalRepository;
      this.employeeRepository = employeeRepository;
      this.approvalCategoryRepository = approvalCategoryRepository;
      this.approvalFormRepository = approvalFormRepository;
      this.approvalBaseFormTypeRepository = approvalBaseFormTypeRepository;
      this.annualLeaveUsageRepository = annualLeaveUsageRepositroy;
      this.approvalFileRepository = approvalFileRepository;
      this.approverRepository = approverRepository;
      this.referrerRepository = referrerRepository;
      this.alarmsRepository = alarmsRepository;
   }

   // 상신리스트 => 조회 0-> 대기 , 1-> 진행중,  2->완료 , 3-> 반려 , 4->회수 
   public Page<Map<String, Object>> selectApprovalList(Pageable pageable,int num2,Long id, String title) {
	  
	  Employee emp = employeeRepository.findByEmpId(id);
	  
	  // 리스트 개수 조회
	  int approvalCount = approvalRepository.countByEmployee_EmpId(emp.getEmpId());
	  
	  System.out.println(approvalCount);
     
      Page<Object[]> approvalList = approvalRepository.findList(num2,emp.getEmpId(),title,pageable);
	  
	  System.out.println(approvalList.getTotalElements());
      
      List<Map<String, Object>> responseList = new ArrayList<>();
      for (Object[] objects : approvalList.getContent()) {
         Approval approval = (Approval) objects[0];
         ApprovalForm approvalForm = (ApprovalForm) objects[1];
         Approver approver = (Approver) objects[2];
         
         int num = approval.getApprovalStatus();
         Map<String, Object> map = new HashMap<>();
         map.put("approval", approval);
         map.put("formType", approvalForm);
         map.put("approver",approver);
         map.put("num", num);
         responseList.add(map);
         
      }
      
      
      return new PageImpl<>(responseList, pageable, approvalList.getTotalElements());
   }
   
   // 수신리스트 조회
   public Page<Map<String, Object>> selectApprovalinBoxList(Pageable pageable,String title,int num2,Long id) {
		  
		  Employee emp = employeeRepository.findByEmpId(id);
		  System.out.println("로그인 한 사람 : "+emp.getEmpId());
		  
		  System.out.println("num2 : "+num2);
	      Page<Object[]> approvalList = approvalRepository.findinboxList(num2,emp.getEmpId(),title,pageable);
	      
	      System.out.println("토탈 : "+approvalList.getTotalElements());
	      
	      List<Map<String, Object>> responseList = new ArrayList<>();
	      for (Object[] objects : approvalList.getContent()) {
	         Approval approval = (Approval) objects[0];
	         ApprovalForm approvalForm = (ApprovalForm) objects[1];
	         Approver approver = (Approver) objects[2];
	         int num = approval.getApprovalStatus();
	         Map<String, Object> map = new HashMap<>();
	         map.put("approval", approval);
	         map.put("employee",approval.getEmployee());
	         map.put("formType", approvalForm);
	         map.put("approver",approver);
	         map.put("num", num);
	         responseList.add(map);
	         
	      }
	      return new PageImpl<>(responseList, pageable, approvalList.getTotalElements());
	   }
   
   // 수신참조 리스트 조회
   public Page<Map<String, Object>> selectReferrerList(Pageable pageable,String title,Long id) {
		  
		  Employee emp = employeeRepository.findByEmpId(id);
		  System.out.println("로그인 한 사람 : "+emp.getEmpId());

	      Page<Object[]> referrerList = approvalRepository.findReferrerList(emp.getEmpId(),title,pageable);
	      
	      System.out.println(referrerList);
	      
	      List<Map<String, Object>> responseList = new ArrayList<>();
	      for (Object[] objects : referrerList.getContent()) {
	         Approval approval = (Approval) objects[0];
	         ApprovalForm approvalForm = (ApprovalForm) objects[1];
	         int num = approval.getApprovalStatus();
	         
	         Map<String, Object> map = new HashMap<>();
	         map.put("approval", approval);
	         map.put("employee",approval.getEmployee());
	         map.put("formType", approvalForm);
	         map.put("num", num);
	         responseList.add(map);
	         
	      }
	      return new PageImpl<>(responseList, pageable, referrerList.getTotalElements());
	   }
   
   
   // 상세조회
   public Map<String, Object> detailApproval(Long approval_no) {
      List<Object[]> object = approvalRepository.selectByapprovalNo(approval_no);
      Map<String, Object> result = new HashMap<>();
      
      ApprovalFile file = approvalFileRepository.findByApprovalApprovalNo(approval_no);
      Object[] obj = object.get(0);
      
      Approval approval = (Approval) obj[0];
      Employee employee = (Employee) obj[1];
      ApprovalBaseFormType baseForm = (ApprovalBaseFormType) obj[2];
      
      AnnualLeaveUsage annualLeaveUsage = null;
      if (obj[3] != null) {
          annualLeaveUsage = (AnnualLeaveUsage) obj[3];
      }
      
      ApprovalForm af = (ApprovalForm) obj[4];
      
      List<Approver> agree = approverRepository.findByApprovalApprovalNoAndApproverType(approval_no,"합의자");
      List<Approver> approver = approverRepository.findByApprovalApprovalNoAndApproverType(approval_no,"결재자");
      List<Referrer> referrer = referrerRepository.findByApproval(approval);
      
      
      System.out.println("agree : "+agree);
      System.out.println("referrer : "+referrer);
      
      
      result.put("approval", approval);
      result.put("employee", employee);
      result.put("baseForm", baseForm);
      result.put("annualLeaveUsage", annualLeaveUsage);
      result.put("approvalForm", af);
      result.put("file", file);
      result.put("agree", agree);
      result.put("approver",approver);
      result.put("referrer", referrer);
      return result;
   }
   

   // 전자결재 저장 (품의서, 요청서)
   public Approval createApproval(Map<String,Object> obj) {
      
      Employee emp = employeeRepository.findByEmpId((Long)obj.get("이름"));
      
      
      ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
      System.out.println("카테고리 출력 : "+ac);
      Approval approval = Approval.builder()
                     .approvalTitle((String)obj.get("제목"))
                     .approvalContent((String)obj.get("내용"))
                     .approvalCategory(ac)
                     .employee(emp)
                     .build();

      return approvalRepository.save(approval);
   }
   
   
   @Transactional
   // 전자결재(휴가신청서) 저장
   public Approval createApprovalLeave(Map<String,Object> obj) {
         
         Employee emp = employeeRepository.findByEmpId((Long)obj.get("이름"));
         
     ApprovalForm af = approvalFormRepository.findByFormCode((Integer)obj.get("폼코드"));
     
     int type = 0;
     switch (af.getFormName()) {
        case "반차":
            type = 1;
            break;
        case "연차":
            type = 2;
            break;
        case "경조휴가":
            type = 3;
            break;
        case "병가":
            type = 4;
            break; 
     }
     
     AnnualLeaveUsage annual = AnnualLeaveUsage.builder()
                          .annualType(type)
                          .employee(emp)
                          .annualUsageStartDate((LocalDate)obj.get("시작일자"))
                          .annualUsageEndDate((LocalDate)obj.get("종료일자"))
                          .timePeriod((String)obj.get("반차시간대"))
                          .totalDay(((Number)obj.get("사용일수")).doubleValue())
                          .build();
                              
         
         AnnualLeaveUsage au = annualLeaveUsageRepository.save(annual);
         ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
         System.out.println("카테고리 출력 : "+ac);
         
         Approval approval = Approval.builder()
                        .approvalTitle((String)obj.get("제목"))
                        .approvalContent((String)obj.get("내용"))
                        .approvalCategory(ac)
                        .employee(emp)
                        .annualLeaveUsage(annual)
                        .build();
   
         return approvalRepository.save(approval);
      }
   
   
   // 카테고리 값 가져오기
   public List<ApprovalFormDto> selectApprovalCateList(int no){
      
      ApprovalBaseFormType at = approvalBaseFormTypeRepository.findByBaseFormCode(no);
      
      List<ApprovalForm> ac = approvalFormRepository.findByApprovalBaseFormType_BaseFormCodeAndFormVisibility(at.getBaseFormCode(),'Y');
      
      List<ApprovalFormDto> list = new ArrayList<ApprovalFormDto>();
      
       for(ApprovalForm a : ac) { 
          ApprovalFormDto dto = ApprovalFormDto.builder()
                               .form_code(a.getFormCode())
                               .base_form_code(at.getBaseFormCode())
                               .form_name(a.getFormName())
                               .form_content(a.getFormContent())
                               .build();
                list.add(dto);                
       }
       
       
      
      return list;
   }
    
   
   // 회수함으로 이동
   @Transactional
   public int revoke(Long approvalNo) {
	  int result = 4;
	  
	  Alarms alarms = alarmsRepository.findByAlarmReferenceNoAndAlarmType(approvalNo, "approval");
	  if (alarms != null) {
	      alarmsRepository.delete(alarms);
	  } else {
	      // 알람이 존재하지 않음을 처리하는 코드
	      throw new EntityNotFoundException("알림 없음 : " + approvalNo);
	  }
	  return approvalRepository.updateStatus(result,approvalNo);
   }
   
   // 게시글 수정
   @Transactional
   public Approval updateApproval(Map<String,Object> obj,Long no) {
	   
	   Employee emp = employeeRepository.findByEmpId((Long)obj.get("이름"));
	   System.out.println("출력 해 제발 !!!"+obj.get("폼코드"));
	      
	   ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
	   
	   System.out.println("카테고리 : "+ac.getCateCode());
	   
	   Approval approval = Approval.builder()
			   				.approvalNo(no)
			   				.approvalCategory(ac)
			   				.approvalContent((String)obj.get("내용"))
			   				.approvalTitle((String)obj.get("제목"))
			   				.employee(emp)
			   				.approvalStatus(0)
			   				.approvalRegDate((LocalDateTime)obj.get("날짜"))

			   				.build();
	   
	   
	   System.out.println("출력 : "+approval.getApprovalCategory().getCateCode());
	   
	   // 데이터베이스에 저장
	   approvalRepository.save(approval);
	   
	   return approval;
   }
   
	   // 전자결재(근태신청서) 수정
	   @Transactional
	   public Approval updateApprovalLeave(Map<String,Object> obj,Long no) {
		   
		   Employee emp = employeeRepository.findByEmpId((Long)obj.get("이름"));
		   System.out.println("출력 해 제발 !!!"+obj.get("폼코드"));
		      
		   ApprovalCategory ac = approvalCategoryRepository.findByCateCode((Integer)obj.get("폼코드"));
		   
		   System.out.println("카테이름 : "+ac.getCateCode());
		   ApprovalForm af = approvalFormRepository.findByFormCode((Integer)obj.get("폼코드"));
		     
		     int type = 0;
		     switch (af.getFormName()) {
		        case "반차":
		            type = 1;
		            break;
		        case "연차":
		            type = 2;
		            break;
		        case "경조휴가":
		            type = 3;
		            break;
		        case "병가":
		            type = 4;
		            break; 
		     }
		     
		     
		     AnnualLeaveUsage annualNo = annualLeaveUsageRepository.getAnnualLeaveUsageByApproval_ApprovalNo(no);
		     
		     AnnualLeaveUsage annual = AnnualLeaveUsage.builder()
		                          .annualType(type)
		                          .employee(emp)
		                          .annualUsageStartDate((LocalDate)obj.get("시작일자"))
		                          .annualUsageEndDate((LocalDate)obj.get("종료일자"))
		                          .timePeriod((String)obj.get("반차시간대"))
		                          .totalDay((Double)obj.get("사용일수"))
		                          .annualUsageNo(annualNo.getAnnualUsageNo())
		                          .build();
		                              
		         
		   AnnualLeaveUsage au = annualLeaveUsageRepository.save(annual);
		   
		   Approval approval = Approval.builder()
	   				.approvalNo(no)
	   				.approvalCategory(ac)
	   				.approvalContent((String)obj.get("내용"))
	   				.approvalTitle((String)obj.get("제목"))
	   				.employee(emp)
	   				.approvalStatus(0)
	   				.approvalRegDate((LocalDateTime)obj.get("날짜"))
	   				.annualLeaveUsage(au)
	   				.build();
		   
		   // 데이터베이스에 저장
		   approvalRepository.save(approval);
		   
		   return approval;
	   }
	   
	   // 전자결재 삭제
	   public int deleteApproval(Long no) {
		   int result = 0;
			try {
			    approvalRepository.deleteById(no);	
				result = 1;
			}catch(Exception e) {
				e.printStackTrace();
			}
			return result;
	   }
	   
	   
	   // admin 전자결재 리스트 
	   public Page<ApprovalForm> adminApprovalList(Pageable pageable){		      
		      return approvalFormRepository.findAll(pageable);
		   }
	   
	   
	   // 상태 전환
	   @Transactional
	   public int updateVisibility(int id, boolean checked) {
		   char result;
		   if(checked == true) {
			   result = 'Y';
		   }else{
			   result = 'N';
		   }
		   return approvalFormRepository.updateVisibility(id,result);
	   }
	   
	   // 폼 이름 찾기
	   public ApprovalForm formFind(String title) {
		   return approvalFormRepository.findByFormName(title);
	   }
	   
	   // 폼 생성
	   public ApprovalCategory formCreate(String title, String code, String content) {
		   ApprovalBaseFormType abf = approvalBaseFormTypeRepository.findByBaseFormName(code);
		   
		   System.out.println("폼 네임"+abf.getBaseFormCode());
		   
		   ApprovalForm af = ApprovalForm.builder()
				   					.approvalBaseFormType(abf)
				   					.formName(title)
				   					.formContent(content)
				   					.formVisibility('Y')
				   					.build();
		   ApprovalForm approvalForm = approvalFormRepository.save(af);
		   
		   ApprovalCategory ac = ApprovalCategory.builder()
				   					.approvalForm(approvalForm)
				   					.build();
		   
		   
		   return approvalCategoryRepository.save(ac) ;
	   }
	   
	   // 폼 정보 불러오기
	   public ApprovalForm approvalFormDetail(int form_code) {
		   return approvalFormRepository.findByFormCode(form_code);
	   }
	   
	   // 폼 수정
	   public ApprovalForm formUpdate(int formCode, String content) {
		   
		   ApprovalForm form = approvalFormRepository.findByFormCode(formCode);
		   
		   ApprovalForm af = ApprovalForm.builder()
				   				.formCode(formCode)
				   				.approvalBaseFormType(form.getApprovalBaseFormType())
				   				.formName(form.getFormName())
				   				.formVisibility(form.getFormVisibility())
				   				.formContent(content)
				   				.build();
		   return approvalFormRepository.save(af);
	   }				
	   // 승인된 연차 신청서 목록 조회 메서드
	    public List<ApprovalDto> getApprovedAnnualLeaves() {
	        List<Approval> approvals = approvalRepository.findApprovedAnnualLeaves();
	        return approvals.stream()
	                .map(ApprovalDto::toDto)
	                .collect(Collectors.toList());
	    }
	   
	   
	   // 메인화면 approvalcount
	   public int approvalCount(Long empId, int i) {
		   return approvalRepository.findByEmployeeEmpIdAndApprovalStatus(empId,i);
	   }
}
