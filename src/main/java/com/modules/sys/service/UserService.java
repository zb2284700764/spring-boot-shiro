package com.modules.sys.service;

import com.common.service.CrudService;
import com.modules.sys.dao.UserDao;
import com.modules.sys.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends CrudService<UserDao, User> {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据登录名获取用户信息
     *
     * @param loginName
     * @return
     * @throws
     * @Title getByLoginName
     * @require
     * @author zhoubin
     * @date 2017年7月6日 下午4:55:50
     * @history
     */
    public User getByLoginName(String loginName) {

        return dao.getUserByLoginName(loginName);
    }

    /**
     * 查询所有用户
     *
     * @return
     * @throws
     * @Title findAllUser
     * @require
     * @author zhoubin
     * @date 2017年9月18日 下午5:26:30
     * @history
     */
    public List<User> findAllUser() {

        System.out.println("没从缓存查");
        List<User> userList = dao.findAllUser();


        for (User user : userList) {
            redisTemplate.opsForList().leftPush("userList", user);
        }

        // 获取 3、4、5 条数据
        List<User> list = (List<User>) redisTemplate.opsForList().range("userList", 2, 4);


        redisTemplate.opsForValue().set("list", userList);
        List<User> l = (List<User>) redisTemplate.opsForValue().get("list");

        return userList;
    }

}
