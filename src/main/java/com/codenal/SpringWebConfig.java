package com.codenal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringWebConfig implements WebMvcConfigurer{
	
	private String mapping = "/meetingRoomImg/**";
	private String location = "file:///C:/meetingRoomImg/upload/";
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(mapping).addResourceLocations(location);
	}

}
