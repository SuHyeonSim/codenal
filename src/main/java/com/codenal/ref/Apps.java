package com.codenal.ref;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.codenal.employee.domain.Employee;
import com.codenal.employee.domain.EmployeeDto;
import com.codenal.employee.service.EmployeeService;
import com.codenal.meeting.domain.MeetingRoom;
import com.codenal.meeting.domain.MeetingRoomDto;
import com.codenal.meeting.domain.MeetingRoomTimeDto;
import com.codenal.meeting.service.MeetingRoomService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class Apps {
	
	private MeetingRoomService meetingRoomService;
	private EmployeeService employeeService;
	
	@Autowired
	public Apps(MeetingRoomService meetingRoomService, EmployeeService employeeService) {
		this.meetingRoomService = meetingRoomService;
		this.employeeService = employeeService;
	}
	

	@GetMapping("/apps-calendar")
	public String apps_calendar(Model model) {
		model.addAttribute("empId" , SecurityContextHolder.getContext().getAuthentication().getName());
		return "apps/calendar";
	}
	
	@GetMapping("/apps-chat")
	public String apps_chat(Model model) {
		model.addAttribute("page","appchat");
		return "apps/chat";
	}
	
	@GetMapping("/apps-mailbox")
	public String apps_mailbox() {
		return "apps/mailbox";
	}
	
	@GetMapping("/apps-email-basic")
	public String apps_email_basic() {
		return "apps/email-basic";
	}
	
	@GetMapping("/apps-email-ecommerce")
	public String apps_email_ecommerce() {
		return "apps/email-ecommerce";
	}
	
	@GetMapping("/apps-ecommerce-products")
	public String apps_ecommerce_products() {
		return "apps/ecommerce-products";
	}
	
	
	
	@GetMapping("/apps-ecommerce-add-product")
	public String apps_ecommerce_add_product() {
		return "apps/ecommerce-add-product";
	}

	
	@GetMapping("/apps-ecommerce-order-details")
	public String apps_ecommerce_orders_details() {
		return "apps/ecommerce-orders-details";
	}
	
	@GetMapping("/apps-ecommerce-customers")
	public String apps_ecommerce_customers() {
		return "apps/ecommerce-customers";
	}
	
	
	
	@GetMapping("/apps-ecommerce-checkout")
	public String apps_ecommerce_checkout() {
		return "apps/ecommerce-checkout";
	}
	
	@GetMapping("/apps-ecommerce-sellers")
	public String apps_ecommerce_sellers() {
		return "apps/ecommerce-sellers";
	}
	
	@GetMapping("/apps-ecommerce-seller-details")
	public String apps_ecommerce_seller_details() {
		return "apps/ecommerce-seller-details";
	}
	
	@GetMapping("/apps-projects-list")
	public String apps_projects_list() {
		return "apps/projects-list";
	}
	
	@GetMapping("/apps-projects-overview")
	public String apps_projects_overview() {
		return "apps/projects-overview";
	}
	
	@GetMapping("/apps-projects-create")
	public String apps_projects_create() {
		return "apps/projects-create";
	}
	
	@GetMapping("/apps-tasks-kanban")
	public String apps_tasks_kanban() {
		return "apps/tasks-kanban";
	}
	
	@GetMapping("/apps-tasks-list-view")
	public String apps_tasks_list_view() {
		return "apps/tasks-list-view";
	}
	
	@GetMapping("/apps-tasks-details")
	public String apps_tasks_list_details() {
		return "apps/tasks-list-details";
	}
	
	@GetMapping("/apps-crm-contacts")
	public String apps_crm_contacts() {
		return "apps/crm-contacts";
	}
	
	@GetMapping("/apps-crm-companies")
	public String apps_crm_companies() {
		return "apps/crm-companies";
	}
	
	@GetMapping("/apps-crm-deals")
	public String apps_crm_deals() {
		return "apps/crm-deals";
	}
	
	@GetMapping("/apps-crm-leads")
	public String apps_crm_leads() {
		return "apps/crm-leads";
	}
	
	@GetMapping("/apps-crypto-transactions")
	public String apps_crypto_transactions() {
		return "apps/crypto-transactions";
	}
	
	@GetMapping("/apps-crypto-buy-sell")
	public String apps_crypto_buy_sell() {
		return "apps/crypto-buy-sell";
	}
	
	@GetMapping("/apps-crypto-orders")
	public String apps_crypto_orders() {
		return "apps/crypto-orders";
	}
	
	@GetMapping("/apps-crypto-wallet")
	public String apps_crypto_wallet() {
		return "apps/crypto-wallet";
	}
	
	@GetMapping("/apps-crypto-ico")
	public String apps_crypto_ico() {
		return "apps/crypto-ico";
	}
	
	@GetMapping("/apps-crypto-kyc")
	public String apps_crypto_kyc() {
		return "apps/crypto-kyc";
	}
	
	@GetMapping("/apps-invoices-list")
	public String apps_invoices_list() {
		return "apps/invoices-list";
	}
	
	@GetMapping("/apps-invoices-details")
	public String apps_invoices_details() {
		return "apps/invoices-details";
	}
	
	@GetMapping("/apps-invoices-create")
	public String apps_invoices_create() {
		return "apps/invoices-create";
	}
	
	@GetMapping("/apps-tickets-list")
	public String apps_tickets_list() {
		return "apps/tickets-list";
	}
	
	@GetMapping("/apps-tickets-details")
	public String apps_tickets_details() {
		return "apps/tickets-details";
	}
	
	@GetMapping("/apps-nft-marketplace")
	public String apps_nft_marketplace() {
		return "apps/nft-marketplace";
	}
	
	@GetMapping("/apps-nft-explore")
	public String apps_nft_explore() {
		return "apps/nft-explore";
	}
	
	@GetMapping("/apps-nft-auction")
	public String apps_nft_auction() {
		return "apps/nft-auction";
	}
	
	@GetMapping("/apps-nft-item-details")
	public String apps_nft_item_details() {
		return "apps/nft-item-details";
	}
	
	@GetMapping("/apps-nft-collections")
	public String apps_nft_collections() {
		return "apps/nft-collections";
	}
	
	@GetMapping("/apps-nft-creators")
	public String apps_nft_creators() {
		return "apps/nft-creators";
	}
	
	@GetMapping("/apps-nft-ranking")
	public String apps_nft_ranking() {
		return "apps/nft-ranking";
	}
	
	@GetMapping("/apps-nft-wallet")
	public String apps_nft_wallet() {
		return "apps/nft-wallet";
	}
	
	@GetMapping("/apps-nft-create")
	public String apps_nft_create() {
		return "apps/nft-create";
	}
	
	@GetMapping("/apps-file-manager")
	public String apps_file_manager() {
		return "apps/file-manager";
	}
	
	@GetMapping("/appss-attendance")
	public String apps_attendance() {
		return "apps/attendance";	
	}
	@GetMapping("/apps-todo")
	public String apps_todo() {
		return "apps/todo";
	}
	
	@GetMapping("/apps-job-statistics")
	public String apps_job_statistics() {
		return "apps/job-statistics";
	}
	
	@GetMapping("/apps-job-lists")
	public String apps_job_lists() {
		return "apps/job-lists";
	}
	
	@GetMapping("/apps-job-grid-lists")
	public String apps_job_grid_lists() {
		return "apps/job-grid-lists";
	}
	
	@GetMapping("/apps-job-details")
	public String apps_job_details() {
		return "apps/job-details";
	}
	
	@GetMapping("/apps-job-candidate-lists")
	public String apps_job_candidate_lists() {
		return "apps/job-candidate-lists";
	}
	
	@GetMapping("/apps-job-candidate-grid")
	public String apps_job_candidate_grid() {
		return "apps/job-candidate-grid";
	}
	
	@GetMapping("/apps-job-application")
	public String apps_job_application() {
		return "apps/job-application";
	}
	
	@GetMapping("/apps-job-new")
	public String apps_job_new() {
		return "apps/job-new";
	}
	
	@GetMapping("/apps-job-companies-lists")
	public String apps_job_companies_lists() {
		return "apps/job-companies-lists";
	}
	
	@GetMapping("/apps-job-categories")
	public String apps_job_categories() {
		return "apps/job-categories";
	}
	
	@GetMapping("/apps-api-key")
	public String apps_api_key() {
		return "apps/api-key";
	}
}