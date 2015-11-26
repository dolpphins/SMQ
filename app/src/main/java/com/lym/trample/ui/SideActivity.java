package com.lym.trample.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;

import com.lym.trample.ScoresManager;
import com.lym.trample.base.BaseGameActivity;
import com.lym.trample.bean.Square;
import com.lym.trample.dialog.GameOverDialog;
import com.lym.trample.idiom.SideGenerator;
import com.lym.trample.utils.TextUtil;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;

import java.util.List;

/**
 * Created by mao on 2015/11/5.
 *
 * 踩边线游戏界面
 *
 * @author 麦灿标
 */
public class SideActivity extends BaseGameActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
                                                    DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private final static String TAG = "SideActivity";

    private Paint paint = new Paint();

    private int mScores;

    private DropViewConfiguration config;

    private int mBaseline;

    private int mInitSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DropSurfaceView surfaceView = getDropSurfaceview();
        surfaceView.setGameOverBackDistance(0);
        config = surfaceView.getDropViewConfiguration();
        mBaseline = config.getRect().bottom - config.getSquareHeight() / 5;

        mInitSpeed = 18 * config.getRect().height() / 1920;
        setInitSpeed(mInitSpeed);
        setSpeed(mInitSpeed);
    }

    @Override
    public void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started) {
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
            SideGenerator.SideMapEntry entry = castToIdiomMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = new SideGenerator.SideMapEntry();
                square.setBundle(entry);
            }

            paint.setColor(Color.BLACK);
            //已经被点击
            if(entry.isAlreadyTouch()) {
                paint.setAlpha(0);
            }
            canvas.drawRect(square.toRect(), paint);

            //baseline
            float startX = config.getRect().left;
            float startY = mBaseline;
            float endX = config.getRect().right;
            float endY = mBaseline;
            paint.setColor(Color.BLUE);
            paint.setAlpha(255);
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {
        SideGenerator.SideMapEntry entry = new SideGenerator.SideMapEntry();
        square.setBundle(entry);
        getDropSurfaceview().stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);
        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        SideGenerator.SideMapEntry entry = castToIdiomMapEntryFromObject(square.getBundle());
        if(entry == null) {
            getDropSurfaceview().start();
            mScores = 0;
            updateScores(mScores);
        } else {
            //计算分数
            float distance = mBaseline - square.getEndY();
            int scores = calculateScore(distance);
            mScores += scores;
            updateScores(mScores);
            entry.setAlreadyTouch(true);
            //计算速度
            int temp = calculateSpeed(mScores);
            setSpeed(temp);
        }
        return true;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if(square.getEndY() > mBaseline) {
                SideGenerator.SideMapEntry entry = castToIdiomMapEntryFromObject(square.getBundle());
                if(entry != null && !entry.isAlreadyTouch()) {
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

    private SideGenerator.SideMapEntry castToIdiomMapEntryFromObject(Object obj) {
        if(obj == null) {
            return null;
        }
        try {
            return (SideGenerator.SideMapEntry) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int calculateScore(float distance) {
        if(distance < 0) {
            return 0;
        }
        return (int) ((mBaseline - config.getRect().top) / distance);
    }

    private int calculateSpeed(int scores) {
        if(scores < 400) {
            return mInitSpeed;
        }
        return (scores - 400) / 100 + mInitSpeed;
    }

    @Override
    protected String createColumnName() {
        return "best_line_score";
    }

    @Override
    protected void setScores(GameOverDialog dialog) {
        dialog.setGlobalHighestScoe(ScoresManager.bestLineScore);
        dialog.setMyHighestScore(ScoresManager.bestUserLineScore);
        dialog.setCurrentScore(getScores());
    }
}
