package com.lym.trample.generator;

import android.graphics.Rect;

public class SquareGeneratorConfiguration {

    private Rect rect;

    private int num;

    private int width;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(rect);
        sb.append(",");
        sb.append("width:");
        sb.append(width);
        sb.append(",");
        sb.append("height:");
        sb.append(height);
        sb.append(",");
        sb.append("num:");
        sb.append(num);

        return sb.toString();

    }
}
