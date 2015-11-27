package com.lym.trample.generator;

import android.util.Log;

import com.lym.trample.bean.Square;

import java.util.List;

public abstract class BaseSquareGenerator implements ISquareGenerator {

    @Override
    public List<Square> generate(SquareGeneratorConfiguration config) {
        if(!checkConfig(config)) {
            return null;
        }
        return generateSquareList(config);
    }

    private boolean checkConfig(SquareGeneratorConfiguration config) {
        if(config == null) {
            return false;
        }
        if(config.getRect() == null) {
            return false;
        }
        if(config.getNum() < 0) {
            return false;
        }
        if(config.getWidth() < 0) {
            return false;
        }
        if(config.getHeight() < 0) {
            return false;
        }
        if(checkOtherConfig(config)) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkOtherConfig(SquareGeneratorConfiguration config) {
        return true;
    }

    protected abstract List<Square> generateSquareList(SquareGeneratorConfiguration config);
}