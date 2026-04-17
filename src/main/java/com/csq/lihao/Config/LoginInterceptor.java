package com.csq.lihao.Config;


import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    private static final String SECRET_KEY = "csq";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        token = token.substring(7);
        // 使用Hutool的JWTUtil验证token
        boolean verify = JWTUtil.verify(token, SECRET_KEY.getBytes());
        if (!verify) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        // 解析JWT，将用户信息存入请求属性
        JWT jwt = JWTUtil.parseToken(token);
        request.setAttribute("userId", jwt.getPayload("userId"));
        request.setAttribute("username", jwt.getPayload("username"));
        return true;
    }

}
