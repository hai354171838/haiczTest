package com.tensquare.crawler.pipeline;

import com.tensquare.crawler.dao.ArticleDao;
import com.tensquare.crawler.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import util.IdWorker;

/**
 * @author:Haicz
 * @date:2019/03/12
 * 入库类
 */
@Component
public class ArticleDbPipeline implements Pipeline {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private IdWorker idWorker;

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    private String channelId;
    @Override
    public void process(ResultItems resultItems, Task task) {
        String title = resultItems.get("title");
        String content = resultItems.get("content");

        Article article = new Article();
        article.setId(idWorker.nextId()+"");
        article.setTitle(title);
        article.setContent(content);
        article.setChannelid(channelId);
        articleDao.save(article);

    }
}
