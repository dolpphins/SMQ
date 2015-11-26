package com.lym.trample.bean;

import android.text.TextUtils;

import cn.bmob.v3.BmobObject;

/**
 * Created by mao on 2015/11/24.
 *
 * @author 麦灿标
 */
public class TUser extends BmobObject {

    private int id;

    private String guid;

    private int best_color_score;

    private int best_digit_score;

    private int best_line_score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getBest_color_score() {
        return best_color_score;
    }

    public void setBest_color_score(int best_color_score) {
        this.best_color_score = best_color_score;
    }

    public int getBest_digit_score() {
        return best_digit_score;
    }

    public void setBest_digit_score(int best_digit_score) {
        this.best_digit_score = best_digit_score;
    }

    /**
     * 设置指定的分数
     *
     * @param score
     * @param column
     * @return 设置成功返回true,失败返回false.
     */
    public boolean updateScore(int score, String column) {
        if(score < 0 || TextUtils.isEmpty(column)) {
            return false;
        }
        if(TUser.getBmobColorColumn().equals(column)) {
            setBest_color_score(score);
            return true;
        }
        if(TUser.getBmobDigitColumn().equals(column)) {
            setBest_line_score(score);
            return true;
        }
        if(TUser.getBmobLineColumn().equals(column)) {
            setBest_line_score(score);
            return true;
        }
        return false;
    }

    /**
     * 获取指定的分数
     *
     * @param column
     * @return 返回指定的分数,column不存在时返回-1.
     */
    public int getScore(String column) {
        if(TextUtils.isEmpty(column)) {
            return -1;
        }
        if(TUser.getBmobColorColumn().equals(column)) {
            return getBest_color_score();
        }
        if(TUser.getBmobDigitColumn().equals(column)) {
            return getBest_digit_score();
        }
        if(TUser.getBmobLineColumn().equals(column)) {
            return getBest_line_score();
        }
        return -1;
    }

    public boolean setScore(int score, String column) {
        if(TextUtils.isEmpty(column)) {
            return false;
        }
        if(TUser.getBmobColorColumn().equals(column)) {
            setBest_color_score(score);
            return true;
        }
        if(TUser.getBmobDigitColumn().equals(column)) {
            setBest_digit_score(score);
            return true;
        }
        if(TUser.getBmobLineColumn().equals(column)) {
            setBest_line_score(score);
            return true;
        }
        return false;
    }

    public int getBest_line_score() {
        return best_line_score;
    }

    public void setBest_line_score(int best_line_score) {
        this.best_line_score = best_line_score;
    }

    public static String getBmobColorColumn() {
        return "best_color_score";
    }

    public static String getBmobDigitColumn() {
        return "best_digit_score";
    }

    public static String getBmobLineColumn() {
        return "best_line_score";
    }
}
