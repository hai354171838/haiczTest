package com.tensquare.search.dao;

import com.tensquare.search.pojo.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author:Haicz
 * @date:2019/02/26
 */
public interface ArticleSearchDao extends ElasticsearchRepository<Article,String> {

    public Page<Article> findByContentLikeOrTitleLike(String content,String title, Pageable pageable);
}
