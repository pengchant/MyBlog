package com.pengchant.configure;

import com.pengchant.intercceptor.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.MultipartConfigElement;

/**
 * SpringMVC配置
 */
@Configuration
public class SpringMVCConfig  extends WebMvcConfigurerAdapter {

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(authenticationInterceptor())
                 .addPathPatterns("/**");// 拦截所有请求
    }

    /**
     * 试图解析器
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * 验证token的bean
     * @return
     */
    @Bean
    public AuthenticationInterceptor authenticationInterceptor(){
        return new AuthenticationInterceptor();
    }



    /**
     * springmvc关于multipart的配置
     * @return
     */
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("5MB");
        factory.setMaxRequestSize("50M");
        return factory.createMultipartConfig();
    }


}
