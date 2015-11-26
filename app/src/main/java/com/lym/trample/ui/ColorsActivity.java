package com.lym.trample.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.base.BaseDialog;
import com.lym.trample.base.BaseGameActivity;
import com.lym.trample.bean.Square;
import com.lym.trample.color.generator.IColorGenerator;
import com.lym.trample.color.generator.impl.AverageColorGenerator;
import com.lym.trample.color.generator.impl.RandomColorGenerator;
import com.lym.trample.conf.ColorsKeeper;
import com.lym.trample.dialog.GameOverDialog;
import com.lym.trample.dialog.GamePauseDialog;
import com.lym.trample.dialog.GameReadyDialog;
import com.lym.trample.utils.ImageUtil;
import com.lym.trample.utils.TextUtil;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;

import java.util.List;

/**
 * Created by mao on 2015/11/5.
 *
 * 踩颜色游戏界面
 *
 * @author 麦灿标
 */
public class ColorsActivity extends BaseGameActivity {

    private final static String TAG = "ColorsActivity";

    private Paint paint = new Paint();

    private IColorGenerator mColorGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int speed = 18 * getConfig().getRect().height() / 1920;
        setInitSpeed(speed);
        setSpeed(speed);

        mColorGenerator = new AverageColorGenerator(ColorsKeeper.getColorsMap());
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
            IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = mColorGenerator.generate();
                square.setBundle(entry);
            }

            paint.setColor(Color.parseColor(entry.getValue()));
            //已经被点击
            if(entry.isAlreadyTouch()) {
                paint.setAlpha(128);
            }
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();
            //注意metrics的所有值都是以基线(0)为参照
            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            canvas.drawText(entry.getText(), (rect.left + rect.right) / 2, baseline, paint);

        }
    }


    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {
        System.out.println("onSurfaceViewTouchOutsideDown");

        System.out.println(square);

        IColorGenerator.ColorMapEntry entry = new IColorGenerator.ColorMapEntry();
        entry.setValue("#ff0000");
        square.setBundle(entry);
        getDropSurfaceview().stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        System.out.println("onSurfaceViewTouchSquareDown");
        IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
        //开始
        if(entry == null) {
            getDropSurfaceview().start();
            updateScores(0);
        } else {
           if(entry.isSame() && !entry.isAlreadyTouch()) {
               entry.setAlreadyTouch(true);//设置为已点击
               //计算分数
               updateScores(getScores() + getSpeed());
               //计算速度
               int temp = calculateSpeed(getScores());
               setSpeed(temp);
           } else {
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
                    getDropSurfaceview().setGameOverRect(square);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onHandleGameOver(Square square, int type) {
        Log.i(TAG, "onHandleGameOver");

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

    private int calculateSpeed(int scores) {
        if(scores < 400) {
            return 18;
        }
        return (scores - 400) / 100 + 18;
    }

    @Override
    protected String createColumnName() {
        return "best_color_score";
    }
}


