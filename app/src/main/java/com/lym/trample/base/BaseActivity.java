package com.lym.trample.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Created by mao on 2015/11/5.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
