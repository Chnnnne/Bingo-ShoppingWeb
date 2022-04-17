package com.leyou.search.repository;

import com.leyou.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author WangChen
 * @create 2022-04-12 15:59
 * @project: leyou
 * @ClassName: GoodsRepository
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {

}
