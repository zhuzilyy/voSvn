package com.zl.vo_.main.Entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class LoginEntity {
    public currentyUesr getUser() {
        return user;
    }

    public void setUser(currentyUesr user) {
        this.user = user;
    }

    public List<MyFriends> getFriends() {
        return friends;
    }

    public void setFriends(List<MyFriends> friends) {
        this.friends = friends;
    }

    public currentyUesr user;
    public List<MyFriends> friends;

    public class currentyUesr{
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNike() {
            return nike;
        }

        public void setNike(String nike) {
            this.nike = nike;
        }

        public String username;
        public String avatar;
        public String nike;

    }
    public class MyFriends{
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNike() {
            return nike;
        }

        public void setNike(String nike) {
            this.nike = nike;
        }

        public String username;
        public String avatar;
        public String nike;
    }
}
