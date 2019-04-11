package com.tensquare.crawler.processor;

import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author:Haicz
 * @date:2019/03/12
 * 文章爬取类
 */
@Component
public class ArticleProcessor implements PageProcessor {


    @Override
    public void process(Page page) {
        //只提取播客的文章详细页内容
        page.addTargetRequests(page.getHtml().links().regex("https://blog.csdn.net/[a-zA-Z0-9_]+/article/details/[0-9]{8}").all());
        //把文章标题和文章内容获取得到保存//*[@id="mainBox"]/main/div[1]/div/div/div[1]/h1
        String title = page.getHtml().xpath("//*[@id=\"mainBox\"]/main/div[1]/div/div/div[1]/h1/text()").toString();
        //
        String content =page.getHtml().xpath("//*[@id=\"article_content\"]/div[2]").toString();
        System.out.println("title=" + title);
        System.out.println("content=" + content);
        if (title!=null&&content!=null){
            System.out.println("title=" + title);
            System.out.println("content=" + content);
            page.putField("title",title);
            page.putField("content",content);
        }else{
            page.setSkip(true);
        }


    }

    @Override
    public Site getSite() {
        Site site = new Site();
        //设置失败后重试时长为3s, 设置两个页面之间爬去间隔为0.1s
        site.setRetryTimes(3000).setSleepTime(100);
        return site;
    }
}
