package com.lym.trample.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by mao on 2015/11/25.
 */
public class UpdateBean extends BmobObject {

    /** 最新版本序号 */
    private long serialNumber;

    /** 更新链接 */
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
