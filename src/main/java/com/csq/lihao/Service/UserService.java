package com.csq.lihao.Service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.csq.lihao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class UserService {
    // 使用内存Map模拟数据库
    private final Map<Long, User> userMap = new ConcurrentHashMap<>();

    // 使用Hutool的雪花算法生成分布式ID
    private final Snowflake snowflake = IdUtil.getSnowflake();

    public User create (User user){
        user.setId(snowflake.nextId());
        user.setCreateTime(DateUtil.date());
        userMap.put(user.getId(), user);
        log.info("创建用户: {}", user);
        return user;
    }

    //查询所有用户
    public List<User> findAll(){
        return new ArrayList<>(userMap.values());
    }

    //根据id查询用户
    public User findById(Long id){
        return userMap.get(id);
    }

    // 更新用户 (使用Hutool的BeanUtil复制属性)
    public User update(Long id,User updateUser){
        User user = userMap.get(id);
        if (user == null) {
            return null;
        }
        BeanUtil.copyProperties(updateUser,user,true);
        userMap.put(id, user);
        log.info("更新用户: {}", user);
        return user;
    }

    // 删除用户
    public boolean delete(Long id){
         return userMap.remove(id) != null;
    }
}
