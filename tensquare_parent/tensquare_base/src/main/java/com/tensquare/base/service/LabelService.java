package com.tensquare.base.service;

/**
 * @author:Haicz
 * @date:2019/02/01
 */

import util.IdWorker;
import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签业务逻辑类
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;
    /**
     * 查询所有
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Label findById(String id ) {

        return  labelDao.findById(id).get();
    }

    /**
     * 添加
     * @param label
     */
    public void add(Label  label) {
        label.setId(idWorker.nextId()+"");
         labelDao.save(label);
    }

    /**
     * 修改
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     *g根据id删除
     * @param id
     */
    public void delete(String id ) {

        labelDao.deleteById(id);
    }

    /**
     * 根据条件查询
     * @param label
     * @return
     */
    public List<Label> findSearch(Label label) {
        Specification<Label> specification = createSpecification(label);
        List<Label> labelList = labelDao.findAll(specification);
        return labelList;
    }

    /**
     * 分页条件查询
     * @param label
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Label label, int page, int size) {
        Specification<Label> specification = createSpecification(label);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification, pageRequest);
    }


    /**
     * 构建查询条件
     * @param label
     * @return
     */
    private Specification<Label> createSpecification(Label label){
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> al = new ArrayList();
                if (StringUtils.isNotBlank(label.getLabelname())){
                    al.add(criteriaBuilder.like(root.get("labelname").as(String.class), "%"+label.getLabelname()+"%"));
                }
                if (StringUtils.isNotBlank(label.getState())){
                    al.add(criteriaBuilder.equal(root.get("state").as(String.class),label.getState() ));
                }

                if (StringUtils.isNotBlank(label.getRecommend())){
                    al.add(criteriaBuilder.equal(root.get("recommend").as(String.class), label.getRecommend()));
                }
                return criteriaBuilder.and(al.toArray(new Predicate[al.size()]));
            }
        };
    }

}
