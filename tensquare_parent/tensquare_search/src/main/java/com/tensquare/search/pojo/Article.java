package com.tensquare.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;

/**
 * @author:Haicz
 * @date:2019/02/26
 */
@Document(indexName = "tensquare",type = "article")
public class Article implements Serializable {

    @Id
    //ID
    private String id;
    //索引: 决定该域是否能被搜索
    //分词: 决定搜索的时候是整体匹配还是单词匹配
    //存储: 决定是否能展示
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String title;//标题
    @Field(index = true,analyzer = "ik_max_word",searchAnalyzer ="ik_max_word" )
    private String content;//文章正文

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
