package org.lrx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {

    //设置访问thymeleaf页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //浏览器发送atlrx请求，也能来到success页面
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/select").setViewName("select");
        registry.addViewController("/register").setViewName("register");
    }

    @Bean
    public MyHandlerInterceptor createInterceptor(){
        return new MyHandlerInterceptor();
    }

    //拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        InterceptorRegistration loginRegistry = registry.addInterceptor(createInterceptor());
        // 拦截路径
        loginRegistry.addPathPatterns("/Emp/**");
        loginRegistry.addPathPatterns("/select");
        // 排除路径
        loginRegistry.excludePathPatterns("/Emp/indexSelect");
        loginRegistry.excludePathPatterns("/Emp/insertEmp");
        loginRegistry.excludePathPatterns("/Emp/selectAllEmp/**");
        loginRegistry.excludePathPatterns("/Emp/getCpacha");

    }
}
