package com.lym.trample.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.lym.trample.bean.Square;
import com.lym.trample.screen.DisplayUitls;

public class DropViewConfiguration {

    private Rect mRect;
    private int mPipeCount;
    private int mPipeBorderColor;
    private boolean mIsPipeBorderBold;
    private int mSquareHeight;
    private int mSquareWidth;
    private int mCanvasColor;
    private int mPipeWidth;
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
            mRect.set(left, top, right, bottom);
            mPipeCount = 4;
            mPipeBorderColor = Color.parseColor("#0000ff");
            mIsPipeBorderBold = false;
            mSquareHeight = mRect.height() / 4;
            mPipeWidth = mRect.width() / mPipeCount;
            mSquareWidth = mRect.width() / mPipeCount;
            mCanvasColor = Color.TRANSPARENT;
            mPipeBorderWidth = 1.0f;
        }

        public Builder setRect(Rect r) {
            mRect = r;
            return this;
        }

        public Builder setPipeCount(int pipeCount) {
            mPipeCount = pipeCount;
            return this;
        }

        public Builder setPipeBorderColor(int color) {
            mPipeBorderColor = color;
            return this;
        }

        public Builder setPipeBorderColor(boolean isBold) {
            mIsPipeBorderBold = isBold;
            return this;
        }

        public Builder setSquareHeight(int height) {
            mSquareHeight = height;
            return this;
        }

        public Builder setSquareWidth(int width) {
            mSquareWidth = width;
            return this;
        }

        public Builder setCanvasColor(int color) {
            mCanvasColor = color;
            return this;
        }

        public Builder setBorderWidth(float width) {
            mPipeBorderWidth = width;
            return this;
        }

        public DropViewConfiguration build() {
            return new DropViewConfiguration(this);
        }

        private int getDefaultRectWidth(Context context) {
            if(context == null) {
                return 0;
            }
            if(!(context instanceof Activity)) {
                return 0;
            }
            return DisplayUitls.getScreenWidthPixels((Activity)context);
        }

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
