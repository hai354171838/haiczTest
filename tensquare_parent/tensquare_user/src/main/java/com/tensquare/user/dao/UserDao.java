package com.tensquare.user.dao;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    User findByMobile(String mobile);

    /**
     * 更新粉丝数
     * @param userId
     * @param count
     */
    @Query("update User u set u.fanscount=u.fanscount+?2 where u.id=?1")
    @Modifying
    void incFanscount(String userId, int count);

    /**
     * 更新关注数
     * @param userId
     * @param count
     */
    @Query("update User u set u.followcount=u.followcount+?2 where u.id=?1")
    @Modifying
    void incFollowcount(String userId, int count);
}
