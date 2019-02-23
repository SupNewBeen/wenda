package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    private JedisAdapter jedisAdapter;

    /**
     * 用户关注了某个实体，实体可以是问题，用户，评论等任何实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();

        //实体的粉丝增加当前用户
        Jedis jedis = jedisAdapter.getJedis();
        //开启事务
        //意味着要执行多个命令
        Transaction transaction = jedisAdapter.multi(jedis);
        //在该实体的粉丝列表中加入该用户
        transaction.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //在该用户的关注列表中加入该实体
        transaction.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(transaction,jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    /**
     * 取关；从两个队列中移除
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean unFollow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        Date date = new Date();

        //实体的粉丝增加当前用户
        Jedis jedis = jedisAdapter.getJedis();
        //开启事务
        //意味着要执行多个命令
        Transaction transaction = jedisAdapter.multi(jedis);
        //在该实体的粉丝列表中移除该用户
        transaction.zrem(followerKey,String.valueOf(userId));
        //在该用户的关注列表中移除该实体
        transaction.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(transaction,jedis);
        return ret.size() == 2 && (Long) ret.get(0) > 0 && (Long) ret.get(1) > 0;
    }

    public List<Integer> getFollowers(int entityType,int entityId,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFormSet(jedisAdapter.zrevrange(followerKey,0,count));
    }

    public List<Integer> getFollowers(int entityType,int entityId,int offset,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return getIdsFormSet(jedisAdapter.zrevrange(followerKey,offset,offset+count));
    }

    public List<Integer> getFollowees(int entityType,int userId,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(userId,entityType);
        return getIdsFormSet(jedisAdapter.zrevrange(followerKey,0,count));
    }

    public List<Integer> getFollowees(int entityType,int userId,int offset,int count){
        String followerKey = RedisKeyUtil.getFollowerKey(userId,entityType);
        return getIdsFormSet(jedisAdapter.zrevrange(followerKey,offset,offset+count));
    }

    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    private List<Integer> getIdsFormSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        if(idset != null){
            for (String str : idset) {
                ids.add(Integer.parseInt(str));
            }
        }
        return ids;
    }


    public boolean isFollower(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId)) != null;
    }
}
