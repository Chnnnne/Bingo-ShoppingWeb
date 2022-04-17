package com.leyou.user.api;


import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author WangChen
 * @create 2022-04-15 10:40
 * @project: leyou
 * @ClassName: UserApi
 */
public interface UserApi {
    @GetMapping("query")
    public User queryUser(@RequestParam("username") String username,
                          @RequestParam("password") String password
    );
}
