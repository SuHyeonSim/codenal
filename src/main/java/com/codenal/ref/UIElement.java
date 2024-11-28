package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIElement {

	@GetMapping("/ui-alerts")
	public String ui_alerts() {
		return "ui-elements/alerts";
	}
	
	@GetMapping("/ui-badges")
	public String ui_badges() {
		return "ui-elements/badges";
	}
	
	@GetMapping("/ui-buttons")
	public String ui_buttons() {
		return "ui-elements/buttons";
	}
	
	@GetMapping("/ui-colors")
	public String ui_colors() {
		return "ui-elements/colors";
	}
	
	@GetMapping("/ui-cards")
	public String ui_cards() {
		return "ui-elements/cards";
	}
	
	@GetMapping("/ui-carousel")
	public String ui_carousel() {
		return "ui-elements/carousel";
	}
	
	@GetMapping("/ui-dropdowns")
	public String ui_dropdowns() {
		return "ui-elements/dropdowns";
	}
	
	@GetMapping("/ui-grid")
	public String ui_grid() {
		return "ui-elements/grid";
	}
	
	@GetMapping("/ui-images")
	public String ui_images() {
		return "ui-elements/images";
	}
	
	@GetMapping("/ui-tabs")
	public String ui_tabs() {
		return "ui-elements/tabs";
	}
	
	@GetMapping("/ui-accordions")
	public String ui_accordions() {
		return "ui-elements/accordions";
	}
	
	@GetMapping("/ui-modals")
	public String ui_modals() {
		return "ui-elements/modals";
	}
	
	@GetMapping("/ui-offcanvas")
	public String ui_offcanvas() {
		return "ui-elements/offcanvas";
	}
	
	@GetMapping("/ui-placeholders")
	public String ui_placeholders() {
		return "ui-elements/placeholders";
	}
	
	@GetMapping("/ui-progress")
	public String ui_progress() {
		return "ui-elements/progress";
	}
	
	@GetMapping("/ui-notifications")
	public String ui_notifications() {
		return "ui-elements/notifications";
	}
	
	@GetMapping("/ui-media")
	public String ui_media() {
		return "ui-elements/media";
	}
	
	@GetMapping("/ui-embed-video")
	public String ui_embed_video() {
		return "ui-elements/embed-video";
	}
	
	@GetMapping("/ui-typography")
	public String ui_typography() {
		return "ui-elements/typography";
	}
	
	@GetMapping("/ui-lists")
	public String ui_lists() {
		return "ui-elements/lists";
	}
	
	@GetMapping("/ui-general")
	public String ui_general() {
		return "ui-elements/general";
	}
	
	@GetMapping("/ui-ribbons")
	public String ui_ribbons() {
		return "ui-elements/ribbons";
	}
	
	@GetMapping("/ui-utilities")
	public String ui_utilities() {
		return "ui-elements/utilities";
	}
}




