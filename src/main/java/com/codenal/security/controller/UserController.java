package com.codenal.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	
	@GetMapping("/auth-signin")
	public String loginPage(@RequestParam(value = "error", required = false) String error, 
	                        Model model) {
	    if (error != null) {
	        model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못 입력되었습니다.");
	    }
	    return "auth-signin-basic";
	}

  
}