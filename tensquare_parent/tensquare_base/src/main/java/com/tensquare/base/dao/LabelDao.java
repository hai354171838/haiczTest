package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author:Haicz
 * @date:2019/02/01
 */
public interface LabelDao  extends JpaRepository<Label,String>,JpaSpecificationExecutor<Label> {

}
