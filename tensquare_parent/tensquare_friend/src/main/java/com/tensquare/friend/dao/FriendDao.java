package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author:Haicz
 * @date:2019/03/03
 */
public interface FriendDao extends JpaRepository<Friend,String> {
    @Query("select count(f ) from Friend f where f.userid=?1 and f.friendid =?2")
    int selectCount(String id, String friendid);
    @Query("update Friend  set islike=?3 where userid=?1 and friendid=?2")
    @Modifying
    void updateIsLike(String id, String friendid, String s);

    @Query("delete from Friend  where userid=?1 and friendid=?2")
    @Modifying
    void deleteFriend(String id, String friendid);
}
