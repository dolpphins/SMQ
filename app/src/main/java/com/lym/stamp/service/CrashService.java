package com.lym.stamp.service;

import android.app.IntentService;
import android.content.Intent;

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
