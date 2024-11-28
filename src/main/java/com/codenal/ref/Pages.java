package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Pages {

	@GetMapping("/pages-starter")
	public String starter() {
		return "pages/starter";
	}
	
	@GetMapping("/pages-profile")
	public String profile() {
		return "pages/profile";
	}
	
	@GetMapping("/pages-profile-settings")
	public String profile_settings() {
		return "pages/mypage";
	}
	
	@GetMapping("/pages-team")
	public String team() {
		return "pages/team";
	}
	
	@GetMapping("/pages-timeline")
	public String timeline() {
		return "pages/timeline";
	}
	
	@GetMapping("/pages-faqs")
	public String faqs() {
		return "pages/faqs";
	}
	
	@GetMapping("/pages-pricing")
	public String pricing() {
		return "pages/pricing";
	}
	
	@GetMapping("/pages-gallery")
	public String gallery() {
		return "pages/gallery";
	}
	
	@GetMapping("/pages-maintenance")
	public String maintenance() {
		return "pages/maintenance";
	}
	
	@GetMapping("/pages-coming-soon")
	public String coming_soon() {
		return "pages/coming-soon";
	}
	
	@GetMapping("/pages-sitemap")
	public String sitemap() {
		return "pages/sitemap";
	}
	
	@GetMapping("/pages-search-results")
	public String search_results() {
		return "pages/search-results";
	}
	
	@GetMapping("/pages-privacy-policy")
	public String privacy_policy() {
		return "pages/privacy-policy";
	}
	
	@GetMapping("/pages-term-conditions")
	public String term_conditions() {
		return "pages/term-conditions";
	}
	
	@GetMapping("/landing")
	public String landing() {
		return "pages/landing";
	}
	
	@GetMapping("/nft-landing")
	public String nft_landing() {
		return "pages/nft-landing";
	}
	
	@GetMapping("/job-landing")
	public String job_landing() {
		return "pages/job-landing";
	}
	
}
