package com.csq.lihao.Controller;

import cn.hutool.jwt.JWTUtil;
import com.csq.lihao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final String SECRET_KEY = "csq";
    @Autowired
    private UserController userController;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        // 简化登录验证，实际应查询数据库
        if ("admin".equals(loginData.get("username")) &&
                "password".equals(loginData.get("password"))) {
            // 生成JWT Token
            Map<String, Object> payload = new HashMap<>();
            payload.put("userId", 1L);
            payload.put("username", "admin");
            payload.put("exp", System.currentTimeMillis() + 1000 * 60 * 10); // 24小时
            String token = JWTUtil.createToken(payload, SECRET_KEY.getBytes());
            return Map.of("success", true, "token", token);
        }
        return Map.of("success", false, "message", "用户名或密码错误");
    }
}
