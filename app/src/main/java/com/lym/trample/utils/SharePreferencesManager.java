package com.lym.trample.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by yao on 2015/11/11.
 */
public class SharePreferencesManager {

    private final static String TAG = "SharePreferenceManager";

    /** SharePreferenceManager唯一实例对象 */
    private static SharePreferencesManager sSpManager = new SharePreferencesManager();

    /** 默认的SharePreferences文件名 */
    private final String defaultSpName = "default_sharepreferences";

    /** 创建SharePreference文件默认的操作模式,私有模式,只能
     *  由当前应用和与当前应用相同UserID的Application使用 */
    private final int defaultSpMode = Context.MODE_PRIVATE;
    /** 分数SharePreferences文件名 */
    private final String scoreSpName = "scores";
    /** 创建分数SharePreference文件默认的操作模式,私有模式,只能
     *  由当前应用和与当前应用相同UserID的Application使用 */
    private final int scoresSpMode = Context.MODE_PRIVATE;


    /**
     * <p>
     * 	获取SharePreferenceManager唯一实例对象
     * </p>
     *
     * @return 返回SharePreferenceManager唯一实例对象
     * */
    public static SharePreferencesManager getInstance()
    {
        return sSpManager;
    }

    /**
     * <p>
     * 	添加一个键值对到默认的SharePreference文件中.
     * </p>
     *
     * @param context 上下文
     * @param key  键,不能为空
     * @param value 值
     *
     * @return 添加成功返回true,失败返回false.
     * */
    public boolean putInt(Context context, String key, int value) {
        return putInt(context, defaultSpName, defaultSpMode, key, value);
    }

    /**
     * <p>
     * 	添加一个键值对到指定的SharePreference文件中.注意如果该文件不存在会被自动创建.
     * </p>
     *
     * @param context 上下文
     * @param name 指定的SharePreference文件名,不能为空
     * @param mode 创建的SharePreference文件模式
     * @param key  键,不能为空
     * @param value 值
     *
     * @return 添加成功返回true,失败返回false.
     * */
    public boolean putInt(Context context, String name, int mode, String key, int value) {
        if(!checkParameters(context, name, mode, key)) {
            return false;
        }
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }


    /**
     * <p>
     * 	从位置SharePreferences文件中获取相应的值
     * </p>
     *
     * @param context 上下文
     * @param key 键
     * @param defValue 获取失败返回的值
     *
     * @return 获取成功返回相应的值,获取失败返回defValue
     * */
    public int getScoresInt(Context context, String key, int defValue) {
        return getInt(context, scoreSpName, scoresSpMode, key, defValue);
    }
    /**
     * <p>
     * 	从默认SharePreference文件中获取指定键对应的值
     * </p>
     *
     * @param context 上下文
     * @param key 键
     * @param defValue 获取失败时返回的值
     *
     * @return 获取成功返回相应的值,获取失败返回defValue
     * */
    public int getInt(Context context, String key, int defValue) {
        return getInt(context, defaultSpName, defaultSpMode, key, defValue);
    }

    /**
     * <p>
     * 	从指定SharePreference文件中获取指定键对应的值
     * </p>
     *
     * @param context 上下文
     * @param name 指定的SharePreference文件,不能为空
     * @param mode 创建的SharePreference文件模式
     * @param key 键
     * @param defValue 获取失败时返回的值
     *
     * @return 获取成功返回相应的值,获取失败返回defValue
     * */
    public int getInt(Context context, String name, int mode, String key, int defValue) {
        if(!checkParameters(context, name, mode, key)) {
            return defValue;
        }
        SharedPreferences sp = context.getSharedPreferences(name, mode);
        return sp.getInt(key, defValue);
    }

    //检查参数的合法性
    @SuppressWarnings("deprecation")
    private boolean checkParameters(Context context, String name, int mode, String key) {
        if(context == null) {
            return false;
        }
        if(TextUtils.isEmpty(name)) {
            return false;
        }
        if(mode != Context.MODE_PRIVATE
                && mode != Context.MODE_WORLD_READABLE
                && mode != Context.MODE_WORLD_WRITEABLE) {
            return false;
        }
        //不允许存空键
        if(TextUtils.isEmpty(key)) {
            return false;
        }
        return true;
    }


}