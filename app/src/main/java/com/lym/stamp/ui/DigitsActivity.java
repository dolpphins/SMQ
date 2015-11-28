package com.lym.stamp.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

import com.lym.stamp.ScoresManager;
import com.lym.stamp.base.BaseGameActivity;
import com.lym.stamp.bean.Square;
import com.lym.stamp.calculator.DigitCalculator;
import com.lym.stamp.calculator.ICalculate;
import com.lym.stamp.dialog.GameOverDialog;
import com.lym.stamp.digit.generator.BaseDigitGenerator;
import com.lym.stamp.digit.generator.IDigitGenerator;
import com.lym.stamp.digit.generator.impl.RandomDigitGenerator;
import com.lym.stamp.screen.DisplayUitls;
import com.lym.stamp.utils.TextUtil;
import com.lym.stamp.widget.DropSurfaceView;

import java.util.List;

public class DigitsActivity extends BaseGameActivity{

    private Paint paint = new Paint();

    private BaseDigitGenerator mDigitGenerator;

    private ICalculate mCalculator;

    private int mMaxValue = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDigitGenerator = new RandomDigitGenerator();
        try {
            mCalculator = new DigitCalculator(getApplicationContext());
        } catch (Exception e) {

        }
        setInitSpeed(mCalculator.generateInitSpeed());
        setSpeed(mCalculator.generateInitSpeed());
    }

    @Override
    public void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started) {
        if(square == null) {
            return;
        }
        paint.reset();
        if(started) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();
            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            canvas.drawText("GO", (rect.left + rect.right) / 2, baseline, paint);
        } else {
            IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = mDigitGenerator.generate(mMaxValue);
                square.setBundle(entry);
            }

            int num = entry.getNum();
            if(num > 0) {
                paint.setColor(Color.parseColor("#000000"));
                canvas.drawRect(square.toRect(), paint);

                Rect rect = TextUtil.getFillRectForText(square.toRect());
                if(entry.isDrawDigitFlag()) {
                    paint.setColor(Color.parseColor("#ffffff"));
                } else {
                    paint.setColor(Color.parseColor("#000000"));
                }
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(rect.width());
                Paint.FontMetrics metrics = paint.getFontMetrics();
                float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
                canvas.drawText(num + "", (rect.left + rect.right) / 2, baseline, paint);
            }
        }
    }

    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {

        IDigitGenerator.DigitMapEntry entry = new IDigitGenerator.DigitMapEntry();
        entry.setNum(1);
        entry.setDrawDigitFlag(false);
        square.setBundle(entry);
        getDropSurfaceview().setGameOverBackDistance(0);
        getDropSurfaceview().stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
        if(entry == null) {
            getDropSurfaceview().start();
            updateScores(0);
        } else {
            int num = entry.getNum();
            num--;
            if(num < 0) {
                entry.setNum(1);
                entry.setDrawDigitFlag(false);
                getDropSurfaceview().setGameOverBackDistance(0);
                getDropSurfaceview().stop(square, GAME_OVER_OUT_SQUARE_TYPE);
            } else {
                entry.setNum(num);
                updateScores(mCalculator.calculateScore());
                setSpeed(mCalculator.calculateSpeed());
                mMaxValue = calculateMaxValue(getScores());
            }
        }
        return true;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if(square.getStartY() > getConfig().getRect().bottom) {
                IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
                if(entry != null && entry.getNum() > 0) {
                    getDropSurfaceview().setGameOverBackDistance(getConfig().getSquareHeight());
                    getDropSurfaceview().setGameOverRect(square);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onHandleGameOver(Square square, int type) {
        showGameOverDialiog();
    }

    private IDigitGenerator.DigitMapEntry castToDigitMapEntryFromObject(Object obj) {
        if(obj == null) {
            return null;
        }
        try {
            return (IDigitGenerator.DigitMapEntry) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int calculateMaxValue(int scores) {
        if(scores < 300) {
            return 4;
        }
        int temp =  (scores - 300) / 300 + 4;
        if(temp > 6) {
            temp = 6;
        }
        return temp;
    }

    @Override
    protected String createColumnName() {
        return "best_digit_score";
    }

    @Override
    protected void setScores(GameOverDialog dialog) {
        dialog.setGlobalHighestScoe(ScoresManager.bestDigitScore);
        dialog.setMyHighestScore(ScoresManager.bestUserDigitScore);
        dialog.setCurrentScore(getScores());
    }

    @Override
    protected ScoresManager.Status getBestScoreStatus() {
        return ScoresManager.bestDigitScoreSuccess;
    }

    @Override
    protected void reset() {
        super.reset();
        mMaxValue = 4;
        if(mCalculator != null) {
            mCalculator.reset();
        }
    }
}
