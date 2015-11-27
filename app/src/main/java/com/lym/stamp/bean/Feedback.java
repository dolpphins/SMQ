package com.lym.stamp.bean;

import cn.bmob.v3.BmobObject;

public class Feedback extends BmobObject {

    private String guid;

    private String message;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
