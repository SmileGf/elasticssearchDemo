package com.example.elasticsearchdemo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  elastic search
 * @author ggf
 * @date 2019/12/5 11:22
 */
@Repository
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {

    /**
     *  根据价格区间查询
     * @param price1  价格
     * @param price2 价格
     * @return
     */
    List<Item> findByPriceBetween(double price1, double price2);
}
