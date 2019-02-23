package com.nowcoder.util;

//针对不同的业务构建不同的Key,保证每个Key都是唯一的
public class RedisKeyUtil {

    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENT_QUEU";
    private static String BIZ_TIMELINE = "TIMELINE";

    private static String BIZ_FOLLOWER = "FOLLOWER";
    private static String BIZ_FOLLOWEE= "FOLLOWER";
    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE + SPLIT + entityId +SPLIT + entityType;
    }

    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE + SPLIT + entityType + entityId;
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    //某个实体对应的粉丝的Key
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    //某个用户关注的某一类问题的Key
    public static String getFolloweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    public static String getTimeline(int userId){
        return BIZ_TIMELINE + SPLIT + userId;
    }
}
