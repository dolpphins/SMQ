package com.lym.trample.service;

import android.app.IntentService;
import android.content.Intent;

import com.lym.trample.bean.Crash;

import cn.bmob.v3.listener.SaveListener;

public class CrashService extends IntentService {

    private final static String sDefaultName = "CrashService";

    public CrashService(){
        this(sDefaultName);
    }

    public CrashService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
