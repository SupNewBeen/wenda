package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

//表示事件触发的现场
public class EventModel {
    private EventType type;
    //触发者
    private int actorId;
    //触发的对象
    private int entityType;
    private int entityId;
    //触发对象的所有者
    private int entityOwnerId;

    //快速获取信息,类比vo
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {
    }


    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }


    public EventModel(EventType type) {
        this.type = type;
    }

    public String getExt(String key) {
        return exts.get(key);
    }


    public EventType getType() {
        return type;
    }

    //XX.setType().setActorId()
    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }


    public int getActorId() {
        return actorId;
    }


    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }


    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }


    public int getEntityId() {
        return entityId;
    }


    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }


    public int getEntityOwnerId() {
        return entityOwnerId;
    }


    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }


    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }
}
