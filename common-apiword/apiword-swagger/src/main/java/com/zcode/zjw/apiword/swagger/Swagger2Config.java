package com.zcode.zjw.apiword.swagger;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Slf4j
@ConditionalOnProperty(name = "zcode-swagger.enabled", havingValue = "true")
public class Swagger2Config {

    /**
     * 标题
     */
    @Value("${zcode.swagger.title:Demo}")
    private String title;

    /**
     * 描述
     */
    @Value("${zcode.swagger.description:Demo}")
    private String description;

    /**
     * 版本
     */
    @Value("${zcode.swagger.version:1.0.0}")
    private String version;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select() // 要扫描的API(Controller)基础包
//                .apis(RequestHandlerSelectors.basePackage("com"))
//                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();

//                .apiInfo(new ApiInfoBuilder().title("微信支付案例接口文档").build())
//                .genericModelSubstitutes(new Class[]{DeferredResult.class})
//                .select().paths(PathSelectors.any())
//                // 不显示错误的接口地址
//                .paths(Predicates.not(PathSelectors.regex("/error.*")))// 错误路径不监控
//                .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
//                .build()
//                ;
    }

    private ApiInfo buildApiInfo() {
        Contact contact = new Contact("开发者", "", "");
        return new ApiInfoBuilder()
                .title(title)
                .contact(contact)
                .description(description)
                .version(version)
                .build();
    }

}


 
