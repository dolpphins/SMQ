package com.lym.stamp.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.lym.stamp.ScoresManager;
import com.lym.stamp.base.BaseGameActivity;
import com.lym.stamp.bean.Square;
import com.lym.stamp.calculator.ColorCalculator;
import com.lym.stamp.calculator.ICalculate;
import com.lym.stamp.color.generator.IColorGenerator;
import com.lym.stamp.color.generator.impl.AverageColorGenerator;
import com.lym.stamp.conf.ColorsKeeper;
import com.lym.stamp.dialog.GameOverDialog;
import com.lym.stamp.screen.DisplayUitls;
import com.lym.stamp.utils.TextUtil;
import com.lym.stamp.widget.DropSurfaceView;

import java.util.List;

public class ColorsActivity extends BaseGameActivity {

    private Paint paint = new Paint();

    private IColorGenerator mColorGenerator;

    private ICalculate mCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mColorGenerator = new AverageColorGenerator(ColorsKeeper.getColorsMap(getApplicationContext()));
        try {
            mCalculator = new ColorCalculator(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
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
            IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = mColorGenerator.generate();
                square.setBundle(entry);
            }

            paint.setColor(entry.getValue());

            if(entry.isAlreadyTouch()) {
                paint.setAlpha(128);
            }
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();

            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            String text = entry.getText();
            if(!TextUtils.isEmpty(text)) {
                canvas.drawText(entry.getText(), (rect.left + rect.right) / 2, baseline, paint);
            }
        }
    }


    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {

        IColorGenerator.ColorMapEntry entry = new IColorGenerator.ColorMapEntry();
        entry.setValue(Color.RED);
        square.setBundle(entry);
        getDropSurfaceview().setGameOverBackDistance(0);
        getDropSurfaceview().stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());

        if(entry == null) {
            getDropSurfaceview().start();
            updateScores(0);
        } else {
           if(entry.isSame() && !entry.isAlreadyTouch()) {
               entry.setAlreadyTouch(true);

               setSpeed(mCalculator.calculateSpeed());

               updateScores(mCalculator.calculateScore());


           } else {
               getDropSurfaceview().setGameOverBackDistance(0);
               getDropSurfaceview().stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_SQUARE_ERROR_TYPE);
           }
        }
        return false;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if(square.getStartY() > getConfig().getRect().bottom) {
                IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
                if(entry != null && entry.isSame() && !entry.isAlreadyTouch()) {
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

    private IColorGenerator.ColorMapEntry castToColorMapEntryFromObject(Object obj) {
        if(obj == null) {
            return null;
        }
        try {
            return (IColorGenerator.ColorMapEntry) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected String createColumnName() {
        return "best_color_score";
    }

    @Override
    protected void setScores(GameOverDialog dialog) {
        dialog.setGlobalHighestScoe(ScoresManager.bestColorScore);
        dialog.setMyHighestScore(ScoresManager.bestUserColorScore);
        dialog.setCurrentScore(getScores());
    }

    @Override
    protected ScoresManager.Status getBestScoreStatus() {
        return ScoresManager.bestColorScoreSuccess;
    }

    @Override
    protected void reset() {
        super.reset();
        if(mCalculator != null) {
            mCalculator.reset();
        }
    }
}


