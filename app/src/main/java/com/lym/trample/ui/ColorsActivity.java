package com.lym.trample.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.bean.Square;
import com.lym.trample.color.generator.IColorGenerator;
import com.lym.trample.color.generator.impl.AverageColorGenerator;
import com.lym.trample.color.generator.impl.RandomColorGenerator;
import com.lym.trample.conf.ColorsKeeper;
import com.lym.trample.dialog.GameOverDialog;
import com.lym.trample.utils.ImageUtil;
import com.lym.trample.utils.TextUtil;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;

import java.util.List;

/**
 * Created by mao on 2015/11/5.
 */
public class ColorsActivity extends BaseActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
                                                    DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private final static String TAG = "ColorsActivity";

    private DropSurfaceView colors_drop_main_surfaceview;
    private DropViewConfiguration config;
    private Paint paint = new Paint();

    private IColorGenerator mColorGenerator;

    private TextView colors_drop_main_scores;
    private int mScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.app_colors_main_activity);

        colors_drop_main_surfaceview = (DropSurfaceView) findViewById(R.id.colors_drop_main_surfaceview);
        colors_drop_main_scores = (TextView) findViewById(R.id.colors_drop_main_scores);

        colors_drop_main_scores.setText(mScores + "");
        config = new DropViewConfiguration.Builder(this)
                                            .setPipeBorderColor(Color.parseColor("#0000ff"))
                                            .setPipeCount(4)
                                            .setCanvasColor(Color.WHITE)
                                            .build();
        colors_drop_main_surfaceview.setConfiguration(config);
        colors_drop_main_surfaceview.setOnDrawSurfaceViewListener(this);
        colors_drop_main_surfaceview.setOnSurfaceViewTouchListener(this);
        colors_drop_main_surfaceview.setOnGameOverListener(this);

        colors_drop_main_surfaceview.setSpeed(18 * config.getRect().height() / 1920);

        //mColorGenerator = new RandomColorGenerator(ColorsKeeper.getColorsMap());
        mColorGenerator = new AverageColorGenerator(ColorsKeeper.getColorsMap());
    }

    @Override
    public void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started) {
        paint.reset();
        if(started) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(square.toRect(), paint);
//            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.start_font_icon);
//            Rect rect = ImageUtil.getFillRectForBitmap(bm, square.toRect());
//            canvas.drawBitmap(bm, null, rect, null);
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
        colors_drop_main_surfaceview.stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        System.out.println("onSurfaceViewTouchSquareDown");
        IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
        //开始
        if(entry == null) {
            colors_drop_main_surfaceview.start();
            mScores = 0;
            colors_drop_main_scores.setText(mScores + "");
        } else {
           if(entry.isSame() && !entry.isAlreadyTouch()) {
               entry.setAlreadyTouch(true);//设置为已点击
               //计算分数
               mScores++;
               colors_drop_main_scores.setText(mScores + "");
           } else {
               colors_drop_main_surfaceview.stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_SQUARE_ERROR_TYPE);
           }
        }
        return false;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if(square.getStartY() > config.getRect().bottom) {
                IColorGenerator.ColorMapEntry entry = castToColorMapEntryFromObject(square.getBundle());
                if(entry != null && entry.isSame() && !entry.isAlreadyTouch()) {
                    colors_drop_main_surfaceview.setGameOverRect(square);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onHandleGameOver(Square square, int type) {
        Log.i(TAG, "onHandleGameOver");

        GameOverDialog gameOverDialog = new GameOverDialog(this, mScores);
        gameOverDialog.show();
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

    
    private static class GameOverDialogListener implements GameOverDialog.OnCustomDialogListener {

        @Override
        public void onDialogButtonClick(int userChoose) {

        }
    }
}


