package com.lym.trample.generator;

import android.graphics.Rect;

/**
 * Created by mao on 2015/11/8.
 *
 * 产生器配置
 *
 * @author 麦灿标
 */
public class SquareGeneratorConfiguration {

    /** 产生方块的范围 */
    private Rect rect;

    /** 产生的方块数 */
    private int num;

    /** 产生方块的宽度 */
    private int width;

    /** 产生方块的高度 */
    private int height;

    public Rect getRect() {
        return rect;
    }

    public int getNum() {
        return num;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
