package com.lym.stamp.bean;

import cn.bmob.v3.BmobObject;

public class UpdateBean extends BmobObject {

    private long serialNumber;

    private String updateLink;

    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUpdateLink() {
        return updateLink;
    }

    public void setUpdateLink(String updateLink) {
        this.updateLink = updateLink;
    }
}
