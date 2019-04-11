package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author:Haicz
 * @date:2019/03/03
 */
@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友
     * @param id
     * @param friendid
     * @return
     */
    @Transactional
    public int addFriend(String id, String friendid) {
        //调用Dao, 判断是否添加过
        int count= friendDao.selectCount(id,friendid);
        if (count>0){
            return  0;
        }
        //添加好友,调用Dao,向tb_friend插入一条数据
        Friend friend = new Friend();
        friend.setUserid(id);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        //更新关注数
        userClient.incFollowcount(id, 1);
        //更新对方的粉丝数
        userClient.incFanscount(friendid,1 );
        friendDao.save(friend);
        //判断对方是否添加过你,添加过,把isLike置为1
        if (friendDao.selectCount(friendid,id)>0){
            friendDao.updateIsLike(id,friendid,"1");
            friendDao.updateIsLike(friendid,id,"1");
        }
        return  1;
    }

    /**
     * 添加非好友
     * @param id
     * @param friendid
     */
    public void addNoFriend(String id, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(id);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除成功
     * @param id
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String id, String friendid) {
        friendDao.deleteFriend(id,friendid);
        //取消对方的互相喜欢
        friendDao.updateIsLike(friendid,id ,"0" );
        //取消自己的关注数
        userClient.incFollowcount(id,-1 );
        //取消对方的粉丝数
        userClient.incFanscount(friendid,-1 );
        //添加不喜欢
        addNoFriend(id,friendid );
    }

}
