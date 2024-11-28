package com.codenal.ref;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Tables {

	@GetMapping("/tables-basic")
	public String tables_basic() {
		return "tables/basic";
	}
	
	@GetMapping("/tables-gridjs")
	public String tables_gridjs() {
		return "tables/gridjs";
	}
	
	@GetMapping("/tables-listjs")
	public String tables_listjs() {
		return "tables/listjs";
	}
	
	@GetMapping("/tables-datatables")
	public String tables_datatables() {
		return "tables/datatables";
	}
}
