package com.nowcoder.model;

import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    //为每一个线程都保存了一个本地的User变量
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUsers(User user) {
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}

