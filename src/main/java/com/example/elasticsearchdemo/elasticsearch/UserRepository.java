package com.example.elasticsearchdemo.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
/**
 *  user
 * @author ggf
 * @date 2019/12/4 16:10
 */
@Repository
public interface UserRepository extends ElasticsearchRepository<User,Long> { }
