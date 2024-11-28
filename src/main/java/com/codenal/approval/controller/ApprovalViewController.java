package com.codenal.approval.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.service.AddressBookService;
import com.codenal.annual.domain.AnnualLeaveManage;
import com.codenal.annual.domain.AnnualLeaveUsage;
import com.codenal.annual.service.AnnualLeaveManageService;
import com.codenal.approval.domain.ApprovalBaseFormType;
import com.codenal.approval.domain.ApprovalForm;
import com.codenal.approval.domain.ApprovalFormDto;
import com.codenal.approval.domain.Approver;
import com.codenal.approval.service.ApprovalService;
import com.codenal.approval.service.ApproverService;
import com.codenal.employee.domain.Employee;
import com.codenal.employee.service.EmployeeService;
import com.codenal.security.service.SecurityService;

@Controller
public class ApprovalViewController {

   private final ApprovalService approvalService;
   private final EmployeeService employeeService;
   private final AnnualLeaveManageService annualLeaveManageService;
   private final AddressBookService addressBookService;
   private final ApproverService approverService;

   @Autowired
   public ApprovalViewController(ApprovalService approvalService, SecurityService securityService,
         EmployeeService employeeService, AnnualLeaveManageService annualLeaveManageService,
         AddressBookService addressBookService, ApproverService approverService) {
      this.approvalService = approvalService;
      this.annualLeaveManageService = annualLeaveManageService;
      this.employeeService = employeeService;
      this.addressBookService = addressBookService;
      this.approverService = approverService;
   }
   
   // 품의서 , 요청서 이동
   // 생성 nav에서 숫자값을 받아와서 create로 전달(휴가신청서는 따로 관리)
   @GetMapping("/approval/create/{no}")
   public String createApproval(Model model, @PathVariable("no") int no) {
      LocalDateTime time = LocalDateTime.now();
      String ldt = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

      List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
      cateList = approvalService.selectApprovalCateList(no);
      model.addAttribute("ldt", ldt);
      model.addAttribute("no", no);
      model.addAttribute("cateList", cateList);
      
      return "apps/approval_create";
   }

   // 휴가신청서로 이동
   @GetMapping("/approval/leave_create/{no}")
   public String createLeaveApproval(Model model, @PathVariable("no") int no) {
	  LocalDateTime time = LocalDateTime.now();
	  String ldt = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      Long id = Long.parseLong(username);

      Employee emp = employeeService.getEmployeeById(id);
      AnnualLeaveManage alm = annualLeaveManageService.getAnnualLeaveManageById(emp.getEmpId());

      List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
      cateList = approvalService.selectApprovalCateList(no);

      model.addAttribute("ldt", ldt);
      model.addAttribute("no", no);
      model.addAttribute("cateList", cateList);
      model.addAttribute("remainDay", alm.getAnnualRemainDay());
      
      System.out.println("remain : "+alm.getAnnualRemainDay());
      return "apps/approval__leave_create";
   }

   // 상신 리스트
   @GetMapping("/approval/list")
   public String listApproval(Model model,   @RequestParam(value="num") int num,
		   @RequestParam(value = "title", required = false) String title,
         @PageableDefault(page = 0, size = 10, sort = "approvalNo", direction = Sort.Direction.DESC) Pageable pageable) {
      
      // 현재 인증된 사용자 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      Long id = Long.parseLong(username);
      
      System.out.println(title);
      Page<Map<String, Object>> resultList = approvalService.selectApprovalList(pageable, num, id, title);
      
      model.addAttribute("resultList", resultList);
      model.addAttribute("num", num);
      model.addAttribute("title",title);

      return "apps/approval_list";
   }
   
   // 수신 리스트
   @GetMapping("/approval/inboxList")
   public String inboxListApproval(Model model, @RequestParam(value="num", defaultValue="1") int num,
		   @RequestParam(value = "title", required = false) String title,
         @PageableDefault(page = 0, size = 10, sort = "approvalNo", direction = Sort.Direction.DESC) Pageable pageable) {
      
      // 현재 인증된 사용자 정보 가져오기
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      Long id = Long.parseLong(username);
      
      Page<Map<String, Object>> resultList = approvalService.selectApprovalinBoxList(pageable, title,num, id);
      
      if(num == 4) {
         System.out.println("지금 상태 : "+num);
         resultList = approvalService.selectReferrerList(pageable, title, id);
      }
      
      model.addAttribute("resultList", resultList);
      model.addAttribute("title",title);
      model.addAttribute("num", num);
      
      return "apps/approval_list_inbox";
   }

   // 상신함 상세 조회
   @GetMapping("/approval/{approval_no}")
   public String selectApprovalOne(Model model, @PathVariable("approval_no") Long approval_no) {
      String returnResult = null;
      
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      Long id = Long.parseLong(username);

      Employee emp = employeeService.getEmployeeById(id);
      
      Map<String, Object> resultList = approvalService.detailApproval(approval_no);

      // 타입에 따라 리턴값 다르게 처리 -> 1이면 근태신청서 2면 품의서 3이면 요청서
      ApprovalBaseFormType af = (ApprovalBaseFormType) resultList.get("baseForm");
      int typeInt = af.getBaseFormCode();

      if (typeInt == 1) {
         returnResult = "approval__leave_detail";
      } else {
         returnResult = "approval_detail";
      }

      // annualLeaveUsage가 null인지 확인하여 처리
      AnnualLeaveUsage annualLeaveUsage = (AnnualLeaveUsage) resultList.get("annualLeaveUsage");
      if (annualLeaveUsage == null) {
         model.addAttribute("annualLeaveUsage", ""); // null일 경우 기본 메시지
      } else {
         model.addAttribute("annualLeaveUsage", annualLeaveUsage); // null이 아닐 경우 그대로 사용
      }

      List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
      cateList = approvalService.selectApprovalCateList(typeInt);
      
      // 반려 상태 가져오기
      Approver reject = approverService.findReject(approval_no);
      
      System.out.println("반려 : "+reject);
      
      model.addAttribute("dto", resultList);
      model.addAttribute("type", typeInt);
      model.addAttribute("cateList", cateList);
      model.addAttribute("emp", emp);
      model.addAttribute("reject", reject);
      
      return "apps/" + returnResult;
   }
   

   // 회수함에서 상세조회(update로 감)
   @GetMapping("/approval/detail/{approval_no}")
   public String selectApproval(Model model, @PathVariable("approval_no") Long approval_no) {
      String returnResult = null;
      
      LocalDateTime time = LocalDateTime.now();
	  String ldt = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
      
      System.out.println("상세조회 시작");

      Map<String, Object> resultList = approvalService.detailApproval(approval_no);
      
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication.getName();
      Long id = Long.parseLong(username);

      Employee emp = employeeService.getEmployeeById(id);
      

      // 타입에 따라 리턴값 다르게 처리 -> 1이면 근태신청서 2면 품의서 3이면 요청서
      ApprovalBaseFormType af = (ApprovalBaseFormType) resultList.get("baseForm");
      int typeInt = af.getBaseFormCode();

      if (typeInt == 1) {
         returnResult = "approval__leave_update";
      } else {
         returnResult = "approval_update";
      }

      // annualLeaveUsage가 null인지 확인하여 처리
      AnnualLeaveUsage annualLeaveUsage = (AnnualLeaveUsage) resultList.get("annualLeaveUsage");
      if (annualLeaveUsage == null) {
         model.addAttribute("annualLeaveUsage", ""); // null일 경우 기본 메시지
      } else {
         model.addAttribute("annualLeaveUsage", annualLeaveUsage); // null이 아닐 경우 그대로 사용
      }

      List<ApprovalFormDto> cateList = new ArrayList<ApprovalFormDto>();
      cateList = approvalService.selectApprovalCateList(typeInt);
      
      AnnualLeaveManage alm = annualLeaveManageService.getAnnualLeaveManageById(emp.getEmpId());


      model.addAttribute("dto", resultList);
      model.addAttribute("ldt",ldt);
      model.addAttribute("type", typeInt);
      model.addAttribute("cateList", cateList);
      model.addAttribute("emp", emp);
      model.addAttribute("remainDay", alm.getAnnualRemainDay());
      return "apps/" + returnResult;
   }
   
   // 모달 데이터 보내기
   @GetMapping("/approval/modal")
   @ResponseBody
   public List<TreeMenuDto> getTreeMenu(Model model) {
       return addressBookService.getTreeMenu();
   }
   
   // admin 전자결재 추가
   @GetMapping("/admin/approvalCreate")
   public String adminCreate() {
      return "apps/adminApprovalCreate";
   }
   
   // admin 전자결재 리스트 
   @GetMapping("/admin/approval_list")
   public String adminList(Model model,
         @PageableDefault(page = 0, size = 10, sort = "formCode", direction = Sort.Direction.DESC) Pageable pageable){
      
      Page<ApprovalForm> resultList = approvalService.adminApprovalList(pageable);
      
      model.addAttribute("resultList",resultList);
      
      return "apps/admin_approval_list";
   }
   
   // admin 리스트로 정보 보내기 / json으로 반환
   @GetMapping("/admin/approvalDetail/{no}")
   public String approvalDetail(@PathVariable("no") int form_no,Model model){
      
      ApprovalForm aft = approvalService.approvalFormDetail(form_no);
      
      model.addAttribute("approvalForm",aft);
      return "apps/admin_approval_detail";
   }
   
}