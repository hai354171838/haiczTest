package com.tensquare.article.dao;

import com.tensquare.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 评论dao
 * @author:Haicz
 * @date:2019/02/25
 */
public interface CommentDao extends MongoRepository<Comment,String> {
    /**
     * 根据文章ID查询评论
     * @param articleid
     * @return
     */
    List<Comment> findByArticleid(String articleid);
}
