package com.codenal.admin.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codenal.addressBook.domain.TreeMenuDto;
import com.codenal.addressBook.service.AddressBookService;
import com.codenal.admin.domain.DepartmentsDto;
import com.codenal.admin.service.DeptService;

@Controller
@RequestMapping("/admin/dept")
public class DeptViewController {

	private final AddressBookService addressBookService;
	private final DeptService deptService;

	@Autowired
	public DeptViewController(AddressBookService addressBookService, DeptService deptService) {
		this.addressBookService = addressBookService;
		this.deptService = deptService;
	}

	// 부서 관리
	@GetMapping("")
	public String searchAll(Model model,
			@PageableDefault(page = 0, size = 10, sort = "deptCreateDate", direction = Sort.Direction.DESC) Pageable pageable,
			@ModelAttribute("searchDto") DepartmentsDto searchDto) {
		// System.out.println("확인");

		// 검색
		Page<DepartmentsDto> resultList = deptService.searchDeptName(searchDto, pageable);

		model.addAttribute("resultList", resultList);
		model.addAttribute("searchDto", searchDto);

		return "admin/dept";
	}


	// TreeMenu(JsTree)
	@GetMapping("/tree-menu")
	@ResponseBody
	public List<TreeMenuDto> getTreeMenu() {
		List<TreeMenuDto> treeMenu = addressBookService.getTreeMenu();
		// System.out.println("Tree Menu Data: " + treeMenu);  
		return addressBookService.getTreeMenu();
	}


}