package com.lym.trample.generator;

import android.util.Log;

import com.lym.trample.bean.Square;

import java.util.List;

/**
 * Created by mao on 2015/11/8.
 *
 * 基本的方块产生器
 *
 * @author 麦灿标
 */
public abstract class BaseSquareGenerator implements ISquareGenerator {

    private final static String TAG = "BaseSquareGenerator";

    @Override
    public List<Square> generate(SquareGeneratorConfiguration config) {
        if(!checkConfig(config)) {
            return null;
        }
        return generateSquareList(config);
    }

    //检查配置参数是否合法
    private boolean checkConfig(SquareGeneratorConfiguration config) {
        if(config == null) {
            Log.w(TAG, "config is null");
            return false;
        }
        if(config.getRect() == null) {
            Log.w(TAG, "config rect is null");
            return false;
        }
        if(config.getNum() < 0) {
            Log.w(TAG, "config num < 0");
            return false;
        }
        if(config.getWidth() < 0) {
            Log.w(TAG, "config width < 0");
            return false;
        }
        if(config.getHeight() < 0) {
            Log.w(TAG, "config height < 0");
            return false;
        }
        if(checkOtherConfig(config)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查配置其它信息是否合法，子类可以重写该方法以自己检查参数的合法性
     *
     * @param config 配置
     *
     * @return 合法返回true，非法返回false
     * */
    protected boolean checkOtherConfig(SquareGeneratorConfiguration config) {
        return true;
    }

    /**
     * 产生方块集合，具体实现类必须重写该方法
     *
     * @param config 配置
     *
     * @return 产生成功返回方块集合，失败返回null.
     * */
    protected abstract List<Square> generateSquareList(SquareGeneratorConfiguration config);
}