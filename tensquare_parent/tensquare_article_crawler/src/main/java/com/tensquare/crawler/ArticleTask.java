package com.tensquare.crawler;

import com.tensquare.crawler.pipeline.ArticleDbPipeline;
import com.tensquare.crawler.processor.ArticleProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.RedisScheduler;

/**
 * @author:Haicz
 * @date:2019/03/12
 */
@Component
public class ArticleTask {
    @Autowired
    private ArticleProcessor articleProcessor;

    @Autowired
    private ArticleDbPipeline articleDbPipeline;

    @Autowired
    private RedisScheduler redisScheduler;
    //资讯的任务爬取  每日凌晨:     @Scheduled(cron = "0 0 0 * * ?") {秒数} {分钟} {小时} {日期} {月份} {星期} {年份(可为空)}
    @Scheduled(cron = "0 51 8 * * ?")
    public void aiTask() {

        //设置频道id
        articleDbPipeline.setChannelId("web");
        //设置页面处理器
        Spider spider = Spider.create(articleProcessor);
        //设置种子页面路径
        spider.addUrl("https://blog.csdn.net/nav/web");
        //设置队列
        spider.setScheduler(redisScheduler);
        //设置管道
        spider.addPipeline(articleDbPipeline);
        spider.run();



       /*
            //设置频道id
        articleDbPipeline.setChannelId("ai");
           Spider
                .create(articleProcessor)  //设置页面处理器
                .addPipeline(articleDbPipeline)  //设置管道
                .setScheduler(redisScheduler) //设置队列
                .addUrl("https://blog.csdn.net/nav/ai") //设置种子页面路径
                .run();//启动*/
    }

}
