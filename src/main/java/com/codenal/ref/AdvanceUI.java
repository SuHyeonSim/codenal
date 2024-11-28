package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdvanceUI {

	@GetMapping("/advance-ui-sweetalerts")
	public String ui_sweetalerts() {
		return "advance-ui/sweetalerts";
	}
	
	@GetMapping("/advance-ui-nestable")
	public String ui_nestable() {
		return "advance-ui/nestable";
	}
	
	@GetMapping("/advance-ui-scrollbar")
	public String ui_scrollbar() {
		return "advance-ui/scrollbar";
	}
	
	@GetMapping("/advance-ui-animation")
	public String ui_animation() {
		return "advance-ui/animation";
	}
	
	@GetMapping("/advance-ui-tour")
	public String ui_tour() {
		return "advance-ui/tour";
	}
	
	@GetMapping("/advance-ui-swiper")
	public String ui_swiper() {
		return "advance-ui/swiper";
	}
	
	@GetMapping("/advance-ui-ratings")
	public String ui_ratings() {
		return "advance-ui/ratings";
	}
	
	@GetMapping("/advance-ui-highlight")
	public String ui_highlight() {
		return "advance-ui/highlight";
	}
	
	@GetMapping("/advance-ui-scrollspy")
	public String ui_scrollspy() {
		return "advance-ui/scrollspy";
	}
	
	@GetMapping("/widgets")
	public String widgets() {
		return "advance-ui/widgets";
	}
}
