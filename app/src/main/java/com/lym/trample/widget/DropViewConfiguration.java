package com.lym.trample.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.lym.trample.bean.Square;
import com.lym.trample.screen.DisplayUitls;

/**
 * Created by mao on 2015/11/6.
 *
 * 下落View配置类,该类采用建造者模式.
 *
 * @author 麦灿标
 */
public class DropViewConfiguration {

    //要绘制的区域
    private Rect mRect;
    //管道数
    private int mPipeCount;
    //管道线颜色
    private int mPipeBorderColor;
    //管道线是否加粗
    private boolean mIsPipeBorderBold;
    //每一个方块高度
    private int mSquareHeight;
    //每一个方块宽度
    private int mSquareWidth;
    //画布颜色
    private int mCanvasColor;
    //每条道宽度
    private int mPipeWidth;
    //每条道分割线宽度
    private float mPipeBorderWidth;

    protected DropViewConfiguration(Builder builder){
        this.mRect = builder.mRect;
        this.mPipeCount = builder.mPipeCount;
        this.mPipeBorderColor = builder.mPipeBorderColor;
        this.mIsPipeBorderBold = builder.mIsPipeBorderBold;
        this.mSquareHeight = builder.mSquareHeight;
        this.mSquareWidth = builder.mSquareWidth;
        this.mCanvasColor = builder.mCanvasColor;
        this.mPipeWidth = builder.mPipeWidth;
        this.mPipeBorderWidth = builder.mPipeBorderWidth;
    }

    public Rect getRect() {
        return mRect;
    }

    public int getPipeCount() {
        return mPipeCount;
    }

    public int getPipeBorderColor() {
        return mPipeBorderColor;
    }

    public boolean isPipeBorderBold() {
        return mIsPipeBorderBold;
    }

    public int getSquareHeight() {
        return mSquareHeight;
    }

    public int getSquareWidth() {
        return mSquareWidth;
    }

    public int getCanvasColor() {
        return  mCanvasColor;
    }

    public int getPipeWidth() {
        return mPipeWidth;
    }

    public float getPipeBorderWidth() {
        return mPipeBorderWidth;
    }

    public static class Builder {

        private Rect mRect;
        private int mPipeCount;
        private int mPipeBorderColor;
        private boolean mIsPipeBorderBold;
        private int mSquareHeight;
        private int mSquareWidth;
        private int mCanvasColor;
        private int mPipeWidth;
        private float mPipeBorderWidth;

        public Builder(Context context) {

            mRect = new Rect();
            int left = 0;
            int top = 0;
            int right = getDefaultRectWidth(context) - left;
            int bottom = getDefaultRectHeight(context) - top;
            mRect.set(left, top, right, bottom);//默认为整个屏幕
            mPipeCount = 4;//默认4条
            mPipeBorderColor = Color.parseColor("#0000ff");//默认蓝色
            mIsPipeBorderBold = false;//默认不加粗
            mSquareHeight = mRect.height() / 4;//默认为指定区域高度的1/4
            mPipeWidth = mRect.width() / mPipeCount;
            mSquareWidth = mRect.width() / mPipeCount;//默认为每条道的宽度(注意每条道的宽度一定是相等的)
            mCanvasColor = Color.TRANSPARENT;
            mPipeBorderWidth = 1.0f;//默认为1px
        }

        /**
         * 设置要绘制的区域
         *
         * @param r 指定的要绘制的区域
         *
         * @return 返回当前对象
         * */
        public Builder setRect(Rect r) {
            mRect = r;
            return this;
        }

        /**
         * 设置管道数
         *
         * @param r 指定的管道数
         *
         * @return 返回当前对象
         * */
        public Builder setPipeCount(int pipeCount) {
            mPipeCount = pipeCount;
            return this;
        }

        /**
         * 设置管道线颜色
         *
         * @param color 指定的管道线颜色
         *
         * @return 返回当前对象
         * */
        public Builder setPipeBorderColor(int color) {
            mPipeBorderColor = color;
            return this;
        }

        /**
         * 设置管道线是否加粗
         *
         * @param isBold 是否加粗
         *
         * @return 返回当前对象
         * */
        public Builder setPipeBorderColor(boolean isBold) {
            mIsPipeBorderBold = isBold;
            return this;
        }

        /**
         * 设置每一个方块高度
         *
         * @param height 指定的高度
         *
         * @return 返回当前对象
         * */
        public Builder setSquareHeight(int height) {
            mSquareHeight = height;
            return this;
        }

        /**
         * 设置每一个方块宽度
         *
         * @param height 指定的高度
         *
         * @return 返回当前对象
         * */
        public Builder setSquareWidth(int width) {
            mSquareWidth = width;
            return this;
        }

        /**
         * 设置画布颜色
         *
         * @param color 指定的画布颜色
         *
         * @return 返回当前对象
         * */
        public Builder setCanvasColor(int color) {
            mCanvasColor = color;
            return this;
        }

        /**
         * 设置分割线宽度
         *
         * @param width 指定的分割线宽度
         *
         * @return 返回当前对象
         * */
        public Builder setBorderWidth(float width) {
            mPipeBorderWidth = width;
            return this;
        }

        public DropViewConfiguration build() {
            return new DropViewConfiguration(this);
        }

        //获取绘制区域默认宽度
        private int getDefaultRectWidth(Context context) {
            if(context == null) {
                return 0;
            }
            if(!(context instanceof Activity)) {
                return 0;
            }
            return DisplayUitls.getScreenWidthPixels((Activity)context);
        }

        //获取绘制区域默认高度
        private int getDefaultRectHeight(Context context) {
            if(context == null) {
                return 0;
            }
            if(!(context instanceof Activity)) {
                return 0;
            }
            return DisplayUitls.getScreenHeightPixels((Activity)context);
        }
    }
}
