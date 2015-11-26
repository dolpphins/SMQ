package com.lym.trample.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by mao on 2015/11/24.
 */
public class Crash extends BmobObject implements Serializable{

    /** 屏幕宽度 */
    private int width;

    /** 屏幕高度 */
    private int height;

    /** Android版本 */
    private int sdkVersion;

    /** 手机型号 */
    private String phoneType;

    /** 异常信息 */
    private String message;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }
}
