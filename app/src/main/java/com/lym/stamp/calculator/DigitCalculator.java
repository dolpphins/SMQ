package com.lym.stamp.calculator;

import android.content.Context;

import com.lym.stamp.screen.DisplayUitls;

public class DigitCalculator implements ICalculate{

    private int mInitSpeedDp;
    private int mMaxSpeedDp;
    private int mInitSpeed;
    private int mMaxSpeed;
    private int speedDelta;
    private int deltaCount;

    private int speed;
    private int score;

    public DigitCalculator(Context context) throws Exception{
        if(context == null) {
            throw new Exception("ColorCalculator constructor context can't be null");
        }

        init();

        speedDelta = DisplayUitls.dp2px(context, 1) / 2;
        if(speedDelta < 1) {
            speedDelta = 1;
        }
        mInitSpeed = DisplayUitls.dp2px(context.getApplicationContext(), mInitSpeedDp);
        mMaxSpeed = DisplayUitls.dp2px(context.getApplicationContext(), mMaxSpeedDp);
    }

    @Override
    public int generateInitSpeed() {
        return mInitSpeed;
    }

    @Override
    public int generateMaxSpeed() {
        return mMaxSpeed;
    }

    @Override
    public int calculateScore() {
        score = score + mInitSpeedDp + deltaCount;
        return score;
    }

    @Override
    public int calculateSpeed() {
        if(score < 100) {
            speed = mInitSpeed;
            return mInitSpeed;
        }
        deltaCount = score / 100;
        speed = mInitSpeed + deltaCount * speedDelta;
        if(speed > mMaxSpeed) {
            speed = mMaxSpeed;
        }
        return speed;
    }

    @Override
    public void reset() {
        init();
    }

    private void init() {
        score = 0;
        speed = mInitSpeed;
        deltaCount = 0;
        mInitSpeedDp = 6;
        mMaxSpeedDp = 10;
    }
}
