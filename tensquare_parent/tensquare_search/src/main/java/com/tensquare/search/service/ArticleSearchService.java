package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleSearchDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import util.IdWorker;

/**
 * @author:Haicz
 * @date:2019/02/26
 */
@Service
public class ArticleSearchService {

    @Autowired
    private ArticleSearchDao articleSearchDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 添加文章
     * @param article
     */
    public void add(Article article) {
        article.setId(idWorker.nextId()+"");
        articleSearchDao.save(article);
    }

    public Page<Article> search(String keywords, int page, int size) {

        Pageable pageable=PageRequest.of(page-1, size);
        Page<Article> articles = articleSearchDao.findByContentLikeOrTitleLike(keywords,keywords, pageable);

        return articles;
    }
}
