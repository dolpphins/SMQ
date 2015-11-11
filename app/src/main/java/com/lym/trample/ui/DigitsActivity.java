package com.lym.trample.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.bean.Square;
import com.lym.trample.digit.generator.BaseDigitGenerator;
import com.lym.trample.digit.generator.IDigitGenerator;
import com.lym.trample.digit.generator.impl.RandomDigitGenerator;
import com.lym.trample.utils.TextUtil;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;

import java.util.List;

/**
 * Created by mao on 2015/11/5.
 */
public class DigitsActivity extends BaseActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
                                                    DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private final static String TAG = "DigitsActivity";

    private DropSurfaceView digits_drop_main_surfaceview;
    private DropViewConfiguration config;
    private Paint paint = new Paint();

    private BaseDigitGenerator mDigitGenerator;

    private TextView digits_drop_main_scores;
    private int mScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_digits_main_activity);

        digits_drop_main_surfaceview = (DropSurfaceView) findViewById(R.id.digits_drop_main_surfaceview);
        digits_drop_main_scores = (TextView) findViewById(R.id.digits_drop_main_scores);

        digits_drop_main_scores.setText(mScores + "");
        config = new DropViewConfiguration.Builder(this)
                .setPipeBorderColor(Color.parseColor("#0000ff"))
                .setPipeCount(4)
                .setCanvasColor(Color.WHITE)
                .build();
        digits_drop_main_surfaceview.setConfiguration(config);
        digits_drop_main_surfaceview.setOnDrawSurfaceViewListener(this);
        digits_drop_main_surfaceview.setOnSurfaceViewTouchListener(this);
        digits_drop_main_surfaceview.setOnGameOverListener(this);

        digits_drop_main_surfaceview.setSpeed(18 * config.getRect().height() / 1920);

        mDigitGenerator = new RandomDigitGenerator();
    }

    @Override
    public void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started) {
        Log.i(TAG, "onDrawSurfaceViewSquareItem");
        paint.reset();
        if(started) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();
            //注意metrics的所有值都是以基线(0)为参照
            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            canvas.drawText("GO", (rect.left + rect.right) / 2, baseline, paint);
        } else {
            IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = mDigitGenerator.generate();
                square.setBundle(entry);
            }

            int num = entry.getNum();
                //Game Over
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
                //注意metrics的所有值都是以基线(0)为参照
                float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
                canvas.drawText(num + "", (rect.left + rect.right) / 2, baseline, paint);
            }
        }
    }

    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {

        IDigitGenerator.DigitMapEntry entry = new IDigitGenerator.DigitMapEntry();
        entry.setNum(1);//大于0就行
        entry.setDrawDigitFlag(false);
        square.setBundle(entry);
        digits_drop_main_surfaceview.stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
        if(entry == null) {
            digits_drop_main_surfaceview.start();
            mScores = 0;
            digits_drop_main_scores.setText(mScores + "");
        } else {
            int num = entry.getNum();
            num--;
            if(num < 0) {
                //游戏结束
                entry.setNum(1);
                entry.setDrawDigitFlag(false);
                digits_drop_main_surfaceview.stop(square, GAME_OVER_OUT_SQUARE_TYPE);
            } else {
                entry.setNum(num);
                mScores++;
                digits_drop_main_scores.setText(mScores + "");
            }
        }
        return true;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if(square.getStartY() > config.getRect().bottom) {
                IDigitGenerator.DigitMapEntry entry = castToDigitMapEntryFromObject(square.getBundle());
                if(entry != null && entry.getNum() > 0) {
                    digits_drop_main_surfaceview.setGameOverRect(square);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onHandleGameOver(Square square, int type) {

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

}
