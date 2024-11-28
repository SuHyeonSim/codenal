package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Forms {

	@GetMapping("/forms-elements")
	public String forms_elements() {
		return "forms/elements";
	}
	
	@GetMapping("/forms-select")
	public String forms_select() {
		return "forms/select";
	}
	
	@GetMapping("/forms-checkboxs-radios")
	public String forms_checkboxs_radios() {
		return "forms/checkboxs-radios";
	}
	
	@GetMapping("/forms-pickers")
	public String forms_pickers() {
		return "forms/pickers";
	}
	
	@GetMapping("/forms-masks")
	public String forms_masks() {
		return "forms/masks";
	}
	
	@GetMapping("/forms-advanced")
	public String forms_advanced() {
		return "forms/advanced";
	}
	
	@GetMapping("/forms-range-sliders")
	public String forms_range_sliders() {
		return "forms/range-sliders";
	}
	
	@GetMapping("/forms-validation")
	public String forms_validation() {
		return "forms/validation";
	}
	
	@GetMapping("/forms-wizard")
	public String forms_wizard() {
		return "forms/wizard";
	}
	
	@GetMapping("/forms-editors")
	public String forms_editors() {
		return "forms/editors";
	}
	
	@GetMapping("/forms-file-uploads")
	public String forms_file_uploads() {
		return "forms/file-uploads";
	}
	
	@GetMapping("/forms-layouts")
	public String forms_layouts() {
		return "forms/layouts";
	}
	
	@GetMapping("/forms-select2")
	public String forms_select2() {
		return "forms/select2";
	}
}
