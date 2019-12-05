package com.example.elasticsearchdemo.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
/**
 *  indexName:索引库     type:类型
 * @author gongguifang
 * @date 2019/12/4 16:09
 */
@Document(indexName = "zq_test",type = "user")
@Data
public class User {
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Text)
    private String intro;
    @Field(type = FieldType.Integer)
    private Integer age;
    public User() { }
    public User(Long id, String name, String intro, Integer age) {
        this.id = id;
        this.name = name;
        this.intro = intro;
        this.age = age;
    }
}
