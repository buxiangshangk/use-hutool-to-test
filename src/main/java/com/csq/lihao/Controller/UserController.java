package com.csq.lihao.Controller;

import cn.hutool.core.util.StrUtil;
import com.csq.lihao.Service.UserService;
import com.csq.lihao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Map<String, Object> create(@RequestBody User user) {
        if(StrUtil.isBlank(user.getUsername())){
            return Map.of("success", false, "message", "用户名不能为空");
        }
        User created = userService.create(user);
        return Map.of("success", true, "data", created);
    }

    @GetMapping
    public Map<String, Object> findAll() {
        return Map.of("success",true, "data", userService.findAll());
    }

    @GetMapping("/{id}")
    public Map<String, Object> findById(@PathVariable Long id) {
        if(userService.findById(id) == null){
            return Map.of("success", false, "message", "用户不存在");
        }
        return Map.of("success",true,"data",userService.findById(id));
    }

    @PutMapping("/{id}")
    public Map<String, Object> update(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.update(id, user);
        if (updated == null) {
            return Map.of("success", false, "message", "用户不存在");
        }
        return Map.of("success", true, "data", updated);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return Map.of("success", deleted);
    }

}
