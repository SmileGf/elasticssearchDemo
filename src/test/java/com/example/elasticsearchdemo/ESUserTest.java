package com.example.elasticsearchdemo;

import com.example.elasticsearchdemo.elasticsearch.User;
import com.example.elasticsearchdemo.elasticsearch.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *  test
 * @author ggf
 * @date 2019/12/4 16:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchDemoApplication.class)
@Slf4j
public class ESUserTest {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    UserRepository repository;

    @Test  //创建索引,并且建立类型映射
    public void testPre() throws Exception {
        //创建索引
        template.createIndex(User.class);
        //类型映射-自定义映射
        template.putMapping(User.class);
    }

    @Test   //添加1个
    public void testAdd() throws Exception {
        User user = new User(1L, "zheng qing", "zheng qing is a programmer!", 18);
        repository.save(user);
    }

    @Test   //批量添加
    public void testBatchAdd() throws Exception {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            User user = new User(i + 2L, "elasticsearch" + i + 1, "elasticsearch is a programmer!", 18 + i );
            list.add(user);
        }
        repository.saveAll(list);
    }

    @Test //获取1个
    public void testGetOne() throws Exception {
        System.out.println(repository.findById(1L));
    }

    @Test   //获取全部
    public void testGetAll() throws Exception {
        Iterable<User> all = repository.findAll();
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test //新增、修改 （判断是否有id即可~）
    public void testUpdate() throws Exception {
        Optional<User> byId = repository.findById(1L);
        User user = byId.get();
        System.out.println(user);
        user.setName("zq");
        repository.save(user);
        System.out.println(repository.findById(1L));
    }

    @Test  //删除
    public void testDel() throws Exception {
        repository.deleteById(1L);
        //repository.deleteAll();;
    }

    @Test //DSL查询与过滤
    public void testNativeSearchQueryBuilder() throws Exception {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        BoolQueryBuilder bool = QueryBuilders.boolQuery();
        //模糊查询
        bool.must(QueryBuilders.matchQuery("intro", "zheng"));
        //精确过滤
        List<QueryBuilder> filters = bool.filter();
        filters.add(QueryBuilders.rangeQuery("age").gte(0).lte(200));
        builder.withQuery(bool); //query bool must(filter)
        //排序
        builder.withSort(SortBuilders.fieldSort("age").order(SortOrder.ASC));
        //分页   注：当前页从0开始
        builder.withPageable(PageRequest.of(0, 10));
        //截取字段
        builder.withSourceFilter(new FetchSourceFilter(new String[]{"name", "age"}, null));
        //构造查询条件
        NativeSearchQuery query = builder.build();
        //查询
        Page<User> page = repository.search(query);
        System.out.println("总数:" + page.getTotalElements());
        System.out.println("总页数:" + page.getTotalPages());
        for (User user : page.getContent()) {
            System.out.println(user);
        }
    }
}
