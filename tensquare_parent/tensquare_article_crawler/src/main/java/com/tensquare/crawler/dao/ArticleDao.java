package com.tensquare.crawler.dao;

import com.tensquare.crawler.pojo.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
    /**
     * 文章审核
     * @param articleId
     */
    @Query("update Article set state='1' where id=?1")
    @Modifying//修改需要加@Modifying注解
    void examine(String articleId);
    /**
     * 文章审核
     * @param articleId
     */
    @Query("update Article set thumbup=thumbup+1 where id=?1")
    @Modifying//修改需要加@Modifying注解
    int updateTthumbup(String articleId);
}
