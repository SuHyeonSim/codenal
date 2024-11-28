package com.codenal.addressBook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.service.AddressBookService;
import com.codenal.employee.domain.EmployeeDto;


@Controller
@RequestMapping("/employee/addressBook")
public class AddressBookController {

	private final AddressBookService addressBookService;

	@Autowired
	public AddressBookController(AddressBookService addressBookService) {
		this.addressBookService = addressBookService;
	}

	// 주소록 검색
	@GetMapping("")
	public String searchByEmployeeInfo(Model model,
			@PageableDefault(page = 0, size = 10, sort = "empId", direction = Sort.Direction.DESC) Pageable pageable,
			@ModelAttribute("searchDto") EmployeeDto searchDto) {

		Page<EmployeeDto> resultList = addressBookService.searchByEmployeeInfo(searchDto, pageable);

		model.addAttribute("resultList", resultList);
		model.addAttribute("searchDto", searchDto);

		return "employee/addressBook";
	}


	// TreeMenu(JsTree)
	@GetMapping("/tree-menu")
	@ResponseBody
	public List<TreeMenuDto> getTreeMenu() {
		return addressBookService.getTreeMenu();
	}

	// TreeMenu(JsTree)
	@GetMapping("/tree-menu-announce")
	@ResponseBody
	public List<TreeMenuDto> getTreeMenuAnnounce() {
		return addressBookService.getTreeMenuAnnounce();
	}


}