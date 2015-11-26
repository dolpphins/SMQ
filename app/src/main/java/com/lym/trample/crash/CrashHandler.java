package com.lym.trample.crash;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.lym.trample.bean.Crash;
import com.lym.trample.screen.DisplayUitls;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by mao on 2015/11/24.
 *
 * @author 麦灿标
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = "CrashHandler";

    private Activity mActivity;

    private final static CrashHandler mCrashHandler = new CrashHandler();

    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    private final static String crash_log = "crash_log.txt";

    private CrashHandler() {}

    public static CrashHandler getInstance() {
        return mCrashHandler;
    }

    /**
     * 将当前异常处理器作为默认的异常处理器
     *
     * @param at
     */
    public void registerUncaughtExceptionHandler(Activity at) {
        mActivity = at;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        handleUncaughtException(thread, ex);

        defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    private boolean handleUncaughtException(Thread thread, Throwable ex) {
        if(ex == null || mActivity == null) {
            return false;
        }

        Crash crash = new Crash();
        crash.setWidth(DisplayUitls.getScreenWidthPixels(mActivity));
        crash.setHeight(DisplayUitls.getScreenHeightPixels(mActivity));
        crash.setPhoneType(android.os.Build.MODEL);
        crash.setSdkVersion(android.os.Build.VERSION.SDK_INT);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while(cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        crash.setMessage(sw.toString());
        pw.close();

        crash.save(mActivity);

        mActivity = null;

        return true;
    }
}