package com.lym.stamp.bean;

import android.graphics.Rect;

import java.io.Serializable;

public class Square implements Serializable{

    private float startX;

    private float startY;

    private float endX;

    private float endY;

    private Object bundle;

    public Square() {

    }

    public Square(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getEndX() {
        return endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public Object getBundle() {
        return bundle;
    }

    public void setBundle(Object bundle) {
        this.bundle = bundle;
    }

    public Rect toRect() {
        Rect r = new Rect();
        r.left = Math.round(startX);
        r.top = Math.round(startY);
        r.right = Math.round(endX);
        r.bottom = Math.round(endY);
        return r;
    }

    public boolean isIntersection(Square square) {
        if(square == null) {
            return false;
        }
        return Rect.intersects(toRect(), square.toRect());
    }

    public boolean isInSquare(float x, float y) {
        if(x >= startX && x <= endX && y >= startY && y <= endY) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("startX:");
        sb.append(startX);
        sb.append(",startY:");
        sb.append(startY);
        sb.append(",endX:");
        sb.append(endX);
        sb.append(",endY:");
        sb.append(endY);
        sb.append("]");
        return sb.toString();
    }
}
