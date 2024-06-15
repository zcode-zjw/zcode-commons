package com.zcode.zjw.web.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class UploadConfig {

	@Bean(name="multipartResolver")
	public MultipartResolver multipartResolver(){
		return new CommonsMultipartResolver();
	}

}