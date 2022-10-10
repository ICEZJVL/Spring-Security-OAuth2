package com.cos.security1.config;

import com.cos.security1.auth.PrincipalDetails;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //헤당 메서드의 리턴되는 오브젝트를 IOC로 등록해준다
    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication){
        System.out.println("/test/login===================================");
        PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal();
        System.out.println("authentication : "+ principalDetails);
        return "세션정보확인하기";

    }
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MustacheViewResolver resolver=new MustacheViewResolver();
        resolver.setCharset(("UTF-8"));
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        registry.viewResolver(resolver);
    }
}
