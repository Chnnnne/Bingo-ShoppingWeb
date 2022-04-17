package com.leyou.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author WangChen
 * @create 2022-04-16 11:59
 * @project: leyou
 * @ClassName: GoodsClient
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
