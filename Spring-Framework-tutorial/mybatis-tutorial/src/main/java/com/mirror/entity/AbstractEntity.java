package com.mirror.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author mirror
 */ //表示用于继承
public abstract class AbstractEntity {

    private Long id;
    private Long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    //返回虚拟的属性
    public ZonedDateTime getCreatedDateTime() {
        if (createdAt == null) {
            return null; // Handle null case gracefully
        }
        return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
    }

    public void preInsert() {
        setCreatedAt(System.currentTimeMillis());
    }
}
