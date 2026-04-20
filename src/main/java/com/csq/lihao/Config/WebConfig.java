package com.csq.lihao.Config;

import org.aopalliance.intercept.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//拦截器配置类
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //重写拦截器addInterceptors方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册拦截器
        registry.addInterceptor(new LoginInterceptor())   //注册拦截器
                .addPathPatterns("/**")   //拦截路径
                .excludePathPatterns("/api/auth/**");  // 登录接口不拦截
    }
}
