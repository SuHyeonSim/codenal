package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Icons {

	@GetMapping("/icons-remix")
	public String icons_remix() {
		return "icons/remix";
	}
	
	@GetMapping("/icons-boxicons")
	public String icons_boxicons() {
		return "icons/boxicons";
	}
	
	@GetMapping("/icons-materialdesign")
	public String icons_materialdesign() {
		return "icons/materialdesign";
	}
	
	@GetMapping("/icons-lineawesome")
	public String icons_lineawesome() {
		return "icons/lineawesome";
	}
	
	@GetMapping("/icons-feather")
	public String icons_feather() {
		return "icons/feather";
	}
	
	@GetMapping("/icons-crypto")
	public String icons_crypto() {
		return "icons/crypto";
	}
}
