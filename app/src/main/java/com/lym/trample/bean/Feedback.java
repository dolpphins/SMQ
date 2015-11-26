package com.lym.trample.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by mao on 2015/11/25.
 */
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
