package com.leyou.auth.client;

import com.leyou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author WangChen
 * @create 2022-04-15 10:44
 * @project: leyou
 * @ClassName: UserClient
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
