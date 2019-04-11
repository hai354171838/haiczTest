package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * @author:Haicz
 * @date:2019/02/25
 */
@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询所有吐槽
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id查询吐槽内容
     * @param spitId
     * @return
     */
    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    /**
     * 增加吐槽
     * @param spit
     */
    public void add(Spit spit) {
        //添加主键
        spit.set_id(idWorker.nextId()+"");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        if(StringUtils.isNotBlank(spit.getParentid())){//
            Query query=new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update=new Update();
            update.inc("comment",1);
            mongoTemplate.updateFirst(query,update,"spit");
        }
        spitDao.save(spit);
    }

    /**
     * 根据id修改吐槽
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 根据id删除吐槽
     * @param spitId
     */
    public void delete(String spitId) {
        spitDao.deleteById(spitId);
    }

    /**
     * 吐槽点赞
     * @param spitId
     */
    public void updateThumbup(String spitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query,  update,"spit");
    }

    /**
     * 根据条件查询
     * @param spit
     * @return
     */
    public List<Spit> findSearch(Spit spit) {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//改变默认字符串匹配方式：模糊查询
               // .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withIgnoreNullValues()
                //.withIncludeNullValues();
               .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
               // .withMatcher("userid",ExampleMatcher.GenericPropertyMatchers.storeDefaultMatching() )
               .withIgnorePaths("_id");  //忽略属性，不参与查询

        Example<Spit> example= Example.of(spit,matcher);
        return spitDao.findAll(example);
    }
    /**
     * 带条件+分页搜索
     * @param spit
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findSearch(Spit spit, int page, int size) {
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)//改变默认字符串匹配方式：模糊查询
                // .withIgnoreCase(true) //改变默认大小写忽略方式：忽略大小写
                .withIgnoreNullValues()
                //.withIncludeNullValues();
                .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains()) //采用“包含匹配”的方式查询
                // .withMatcher("userid",ExampleMatcher.GenericPropertyMatchers.storeDefaultMatching() )
                .withIgnorePaths("_id");  //忽略属性，不参与查询
        Pageable pageable=PageRequest.of(page-1, size);
        Example<Spit> example= Example.of(spit,matcher);
        return spitDao.findAll(example,pageable);
    }
    /**
     *
     * 根据上级ID查询吐槽数据（分页）
     * @return
     */
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        Pageable pageable=PageRequest.of(page-1, size);

        return spitDao.findByParentid(parentid,pageable );
    }

    /**
     * 吐槽增加浏览量
     * @param spitId
     * @return
     */
    public void updateShare(String spitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("share", 1);
        mongoTemplate.updateFirst(query,  update,"spit");
    }
    /**
     * 吐槽增加分享量
     * @param spitId
     * @return
     */
    public void updateVisits(String spitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("visits", 1);
        mongoTemplate.updateFirst(query,  update,"spit");
    }
}
