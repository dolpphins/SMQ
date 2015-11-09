package com.lym.trample.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lym.trample.bean.Square;
import com.lym.trample.generator.BaseSquareGenerator;
import com.lym.trample.generator.SquareGeneratorConfiguration;
import com.lym.trample.generator.impl.DefaultSquareGenerator;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by mao on 2015/11/5.
 *
 * 自定义下落SurfaceView
 *
 * @author 麦灿标
 */
public class DropSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    //DropSurfaceView状态枚举类
    private enum Status{

        /** 未开始 */
        STOPPED,

        /** 准备开始 */
        READY,

        /** 正在执行 */
        RUNNING,

        /** 暂停 */
        PAUSE
    }


    private final static String TAG = "DropSurfaceView";

    private Context mContext;

    private SurfaceHolder mSurfaceHolder;

    /** DropSurfaceView配置 */
    private DropViewConfiguration mDropViewConfiguration;

    //要绘制的数据
    private List<Square> mSquareList;
    private List<Square> mSquareListTemp;
    //当前状态
    private volatile Status mStatus;
    //SurfaceView是否已被创建
    private boolean mIsCreated;
//    //记录是否有请求等待执行
//    private boolean mPendingThread;
    //当前绘制进程
    private Thread mDropThread;

    //绘制SurfaceView监听接口
    private OnDrawSurfaceViewListener mDrawSurfaceViewListener;
    //下落速度
    private int mSpeed;

    //方块生成器配置
    private SquareGeneratorConfiguration mSquareGeneratorConfiguration;
    private int mMinY;

    //SurfaceView触摸监听器
    private OnSurfaceViewTouchListener mSurfaceViewTouchListener;

    //游戏结束监听器
    private OnGameOverListener mGameOverListener;
    /** 保存游戏结束点区域 */
    private Square mGameOverSquare;
    //标记游戏是否结束
    private boolean mIsGameOver;
    //游戏结束方块闪烁次数
    private int mGameOverTwinkleCount;

    public DropSurfaceView(Context context) {
        this(context, null);
    }

    public DropSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        Log.i(TAG, "DropSurfaceView init");
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        //默认配置
        mDropViewConfiguration = new DropViewConfiguration.Builder(mContext).build();

        //设置背景透明
        setZOrderOnTop(true);
        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        mSquareList = new LinkedList<Square>();
        mSquareListTemp = new LinkedList<Square>();

        mStatus = Status.STOPPED;
        mIsCreated = false;
        mSpeed = 0;

        mGameOverTwinkleCount = 2;

    }

    public void setConfiguration(DropViewConfiguration config) {
        mDropViewConfiguration = config;

        //相应地初始化方块生成器配置
        mSquareGeneratorConfiguration = new SquareGeneratorConfiguration();
        Rect rect = new Rect();
        rect.left = config.getRect().left;
        rect.right = config.getRect().right;
        rect.top = config.getRect().top - 3 * config.getSquareHeight();
        rect.bottom = config.getRect().top - 3 * config.getSquareHeight() / 2;
        mMinY = config.getRect().top - config.getSquareHeight() / 2;

        mSquareGeneratorConfiguration.setRect(rect);
        mSquareGeneratorConfiguration.setNum(1);
        mSquareGeneratorConfiguration.setWidth(config.getSquareWidth());
        mSquareGeneratorConfiguration.setHeight(config.getSquareHeight());
    }

    public DropViewConfiguration getDropViewConfiguration() {
        return mDropViewConfiguration;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        mIsCreated = true;
        //如果是未开始状态，那么绘制游戏初始布局
        if(mStatus == Status.STOPPED) {
            drawStartLayout();

        //如果是暂停状态
        } else if(mStatus == Status.PAUSE) {
            resume();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        Log.i(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        mIsCreated = false;
        pause();
    }

    private void startDropThread() {
        if(mStatus == Status.READY) {
            Log.i(TAG, "start to execute a drop thread");
            mDropThread = new DropThread();
            mDropThread.start();
        }
    }

    /**
     * 开始执行绘制线程,只有在当前处于停止状态时才会执行绘制线程
     * */
    public void start() {
        if(mStatus == Status.READY) {
            if(mIsCreated) {
                startDropThread();
            } else {

            }
        }
    }

    /**
     * 暂停绘制线程
     * */
    public void pause() {
        if(mStatus == Status.RUNNING) {
            mStatus = Status.PAUSE;
        }
    }

    /**
     * 恢复暂停绘制线程
     * */
    public void resume() {
        if(mStatus == Status.PAUSE) {
            mStatus = Status.RUNNING;
        }
    }

    /**
     * 停止绘制线程
     * */
    public void stop() {
        if(mStatus == Status.RUNNING) {
            mStatus = Status.STOPPED;
        }
    }

    /**
     * 设置SurfaceView绘制监听接口
     *
     * @param l 指定的要设置的SurfaceView绘制监听接口
     * */
    public void setOnDrawSurfaceViewListener(OnDrawSurfaceViewListener l) {
        mDrawSurfaceViewListener = l;
    }

    /**
     * 获取SurfaceView绘制监听接口
     *
     * @return 返回SurfaceView绘制监听接口
     * */
    public OnDrawSurfaceViewListener getOnDrawSurfaceViewListener() {
        return mDrawSurfaceViewListener;
    }

    /**
     * 设置SurfaceView触摸监听器
     *
     * @param l 指定的要设置的SurfaceView触摸监听器
     * */
    public void setOnSurfaceViewTouchListener(OnSurfaceViewTouchListener l) {
        mSurfaceViewTouchListener = l;
    }

    /**
     * 获取SurfaceView触摸监听器
     *
     * @return 返回SurfaceView触摸监听器
     * */
    public OnSurfaceViewTouchListener getOnSurfaceViewTouchListener() {
        return mSurfaceViewTouchListener;
    }

    /**
     * 设置游戏结束监听器
     *
     * @param l 指定的监听器
     * */
    public void setOnGameOverListener(OnGameOverListener l) {
        mGameOverListener = l;
    }

    /**
     * 获取游戏结束监听器
     *
     * @return 返回游戏结束监听器
     * */
    public OnGameOverListener getOnGameOverListener() {
        return mGameOverListener;
    }

    /**
     * 设置速度
     *
     * @param  speed 要设置的下落速度
     * */
    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    private void drawStartLayout() {
        //开始产生1个
        int squareWidth = mSquareGeneratorConfiguration.getWidth();
        int squareHeight = mSquareGeneratorConfiguration.getHeight();
        int top = (mDropViewConfiguration.getRect().top + mDropViewConfiguration.getRect().bottom) / 2;
        mSquareList.add(new Square(squareWidth, top, 2 * squareWidth, top + squareHeight));
        Canvas canvas = null;
        try {
            canvas = mSurfaceHolder.lockCanvas();
            drawBaseView(canvas);
            drawData(canvas, true);
            mStatus = Status.READY;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(canvas != null && mSurfaceHolder != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    //清除画布
    private void clearCanvas(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //canvas.drawColor(mDropViewConfiguration.getCanvasColor(), PorterDuff.Mode.CLEAR);
        }
    }

    //绘制基本的东西，比如背景等
    private void drawBaseView(Canvas canvas) {
        if(canvas == null) {
            Log.w(TAG, "canvas is null, drawBaseView fail!");
            return;
        }
        if(mSurfaceHolder == null) {
            Log.w(TAG, "mSurfaceHolder is null, drawBaseView fail!");
            return;
        }
        if(mDropViewConfiguration == null) {
            Log.w(TAG, "mDropViewConfiguration is null, drawBaseView fail!");
            return;
        }
        Rect r = mDropViewConfiguration.getRect();
        if(r == null) {
            Log.w(TAG, "mDropViewConfiguration's rect is null, drawBaseView fail!");
            return;
        }
        int pipeCount = mDropViewConfiguration.getPipeCount();
        if(pipeCount <= 0) {
            Log.w(TAG, "mDropViewConfiguration's pipeCount is " + pipeCount + ", drawBaseView fail!");
            return;
        }
        int pipeWidth = r.width() / pipeCount;
        int pipeHeight = r.height();
        float[] startX = new float[pipeCount - 1];
        float[] startY = new float[pipeCount - 1];
        float[] endX = new float[pipeCount - 1];
        float[] endY = new float[pipeCount - 1];
        for(int i = 0; i < pipeCount - 1; i++) {
            startX[i] = r.left + (i + 1) * pipeWidth;
            startY[i] = r.top;
            endX[i] = r.left + (i + 1) * pipeWidth;
            endY[i] = r.top + pipeHeight;
        }
        Paint paint = new Paint();
        paint.setColor(mDropViewConfiguration.getPipeBorderColor());
        try {
            canvas.drawLine(startX[0], startY[0], endX[0], endY[0], paint);
            for(int i = 0; i < pipeCount - 1; i++) {
                canvas.drawLine(startX[i], startY[i], endX[i], endY[i], paint);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private List<Square> getCurrentSquareList() {
        List<Square> currentSquareList;
        if(mSquareList.size() > 0) {
            currentSquareList = mSquareList;
        } else if(mSquareListTemp.size() > 0) {
            currentSquareList = mSquareListTemp;
        } else {
            currentSquareList = mSquareList;
        }
        return currentSquareList;
    }

    //更新要绘制的数据集
    private void updateData() {
        if(mSquareList != null && mSquareListTemp != null) {
            List<Square> srcList = null;
            List<Square> dstList = null;
            if(mSquareList.size() > 0) {
                srcList = mSquareList;
                dstList = mSquareListTemp;
            } else if(mSquareListTemp.size() > 0) {
                srcList = mSquareListTemp;
                dstList = mSquareList;
                //都为空集
            } else {
                srcList = mSquareList;
                dstList = mSquareListTemp;
            }
            moveOneStep(srcList);
            dstList.clear();
            copyList(srcList, dstList);
            srcList.clear();

            //判断是否符合产生新方块的条件
            if(canGenerateSquare(dstList)) {
                //产生新的方块
                BaseSquareGenerator generator = new DefaultSquareGenerator();
                List<Square> generatedSquareList = generator.generate(mSquareGeneratorConfiguration);
                if (generatedSquareList != null) {
//                    System.out.println("generatedSquareList is not null,size:" + generatedSquareList.size());
                    dstList.addAll(generatedSquareList);
                } else {
 //                   System.out.println("generatedSquareList is null");
                }
            }
        }
    }

    private void moveOneStep(List<Square> squareList) {
        for(Square square : squareList) {
            square.setStartY(square.getStartY() + mSpeed);
            square.setEndY(square.getEndY() + mSpeed);
        }
    }

    private void copyList(List<Square> srcList, List<Square> dstList) {
        for(Square square : srcList) {
            if(!isGoOutOfRect(square)) {
               dstList.add(square);
            }
        }
    }

    private boolean isGoOutOfRect(Square square) {
        Rect rect = mDropViewConfiguration.getRect();
        if(square.getStartY() > rect.bottom + mDropViewConfiguration.getSquareHeight() / 4) {
            return true;
        } else {
            return false;
        }
    }

    //绘制数据集的所有内容
    private void drawData(Canvas canvas, boolean started) {
        if(mDrawSurfaceViewListener != null) {
            List<Square> drawList = getCurrentSquareList();
            if(drawList == null) {
                return;
            }
            for (Square square : drawList) {
                mDrawSurfaceViewListener.onDrawSurfaceViewSquareItem(canvas, square, started);
            }
        }
    }

    private boolean canGenerateSquare(List<Square> squareList) {
        int minYTemp = Integer.MAX_VALUE;
        for(Square square : squareList) {
            if(square.getStartY() < minYTemp) {
                minYTemp = (int) square.getStartY();
            }
        }
        if(minYTemp >= mMinY) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mStatus != Status.RUNNING && mStatus != Status.READY) {
            return false;
        }
        boolean handled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mSurfaceViewTouchListener != null) {
                    Square square = isTouchSquare(event);
                    if(square != null) {
                        handled = mSurfaceViewTouchListener.onSurfaceViewTouchSquareDown(event, square);
                    } else {
                        square = generateOutsideSquare(event);
                        handled = mSurfaceViewTouchListener.onSurfaceViewTouchOutsideDown(event, square);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return handled || super.onTouchEvent(event);
    }

    //判断触摸点是否在某一方块中，是的话返回该方块，否则返回null
    private Square isTouchSquare(MotionEvent event) {
        List<Square> squareList = null;
        squareList = getCurrentSquareList();
        if(squareList != null) {
            float x = event.getX();
            float y = event.getY();
            for(Square square : squareList) {
                if(square.isInSquare(x, y)) {
                    return square;
                }
            }
        }
        return null;
    }

    private Square generateOutsideSquare(MotionEvent event) {
        if(event == null) {
            return null;
        }
        Square square = new Square();
        float x = event.getX();
        float y = event.getY();
        int index = getPipeIndexByPoint(x);
        square.setStartX(index * mDropViewConfiguration.getPipeWidth());
        square.setEndX(square.getStartX() + mDropViewConfiguration.getPipeWidth());
        float maxY = mDropViewConfiguration.getRect().bottom;
        float minY = mDropViewConfiguration.getRect().top;
        List<Square> squareList = getCurrentSquareList();
        for(Square item : squareList) {
            //在同一条道上
            if(index == getPipeIndexByPoint((item.getStartX() + item.getEndX()) / 2)) {
                if(item.getEndY() < y) {
                    if(item.getEndY() > minY) {
                        minY = item.getEndY();
                    }
                } else if(item.getStartY() > y) {
                    if(item.getStartY() < maxY) {
                        maxY = item.getStartY();
                    }
                }
            }
        }
        square.setStartY(minY);
        square.setEndY(maxY);

        return square;
    }

    //通过x坐标获取当前点在哪一条道上
    private int getPipeIndexByPoint(float x) {
        int index = (int) (x - mDropViewConfiguration.getRect().left) / mDropViewConfiguration.getPipeWidth();
        return index;
    }

    private boolean isGameOver() {
        List<Square> squareList = getCurrentSquareList();
        //未知异常
        if(squareList == null) {
            return true;
        } else {
            if(mGameOverListener != null) {
                return mGameOverListener.onIsGameOver(squareList);
            }
        }
        return false;
    }

    public void handleGameOver() {
        if(mGameOverListener != null && mSurfaceHolder != null && mGameOverSquare != null) {
            mGameOverListener.onHandleGameOver(mSurfaceHolder, mGameOverSquare);
            mGameOverSquare = null;
        }
    }

    /**
     * 设置游戏结束区域
     *
     * @param square 指定的游戏结束区域
     * */
    public void setGameOverRect(Square square) {
        mGameOverSquare = square;
    }

    /**
     * 执行游戏结束动画效果
     *
     * @param square
     *
     * @see {@link #performGameOverEffect(SurfaceHolder, Square)}
     * */
    public void performGameOverEffect(Square square) {
        performGameOverEffect(square, true);
    }

    /**
     * 执行游戏结束动画效果
     *
     * @param square
     * @param fillAfter 是否保持之前绘制的内容
     *
     * @see {@link #performGameOverEffect(SurfaceHolder, Square)}
     * */
    public void performGameOverEffect(Square square, boolean fillAfter) {
        performGameOverEffect(mSurfaceHolder, square, fillAfter);
    }

    /**
     * 执行游戏结束动画效果
     *
     * @param holder
     * @param square
     * @param fillAfter
     * */
    public void performGameOverEffect(SurfaceHolder holder, Square square, boolean fillAfter) {
        if(holder == null || square == null) {
            return;
        }
        Canvas canvas = null;
        Paint bgPaint = new Paint();
        int count = fillAfter ? 2 * mGameOverTwinkleCount : 2 * mGameOverTwinkleCount + 1;
        for(int i = 0; i < count; i++) {
            try {
                Thread.sleep(100);

                canvas = holder.lockCanvas(square.toRect());

                if(i % 2 == 0) {
                    bgPaint.setColor(mDropViewConfiguration.getCanvasColor());
                    canvas.drawRect(square.toRect(), bgPaint);
                } else {
                    if(mDrawSurfaceViewListener != null) {
                        mDrawSurfaceViewListener.onDrawSurfaceViewSquareItem(canvas, square, false);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    //绘制线程
    private class DropThread extends Thread {

        private final static String TAG = "DropThread";

        @Override
        public void run() {
            Log.i(TAG, "execute run method");

            //先清除所有数据
            mSquareList.clear();
            mSquareListTemp.clear();
            mStatus = Status.RUNNING;
            mIsGameOver = false;

            while (!Thread.currentThread().isInterrupted()) {
                //停止
                if(mStatus == Status.STOPPED) {
                    break;
                }
                //暂停
                if (mStatus == Status.PAUSE) {
                    continue;
                }

                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    clearCanvas(canvas);
                    drawBaseView(canvas);
                    updateData();
                    drawData(canvas, false);
                    //判断是否游戏结束
                    if(isGameOver()) {
                        mIsGameOver = true;
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }

            mStatus = Status.STOPPED;
            if(mIsGameOver) {
                handleGameOver();
            }
        }
    }

    public interface OnDrawSurfaceViewListener {

        /**
         * 绘制方块回调方法，你可以实现该方法以自己绘制方块
         *
         * @param canvas 画布
         * @param square 要绘制的方块位置信息
         * @param started 表示当前绘制的方块是否为第一个方块(未开始时显示的第一个方块)，
         *                true表示是，false表示不是，你应该在started为true时设置相应的
         *                事件，然后出发该事件时开始游戏
         * */
        void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started);
    }

    public interface OnSurfaceViewTouchListener {

        /**
         * 当点击到方块时回调该方法
         *
         * @param event 事件对象
         * @param square 被点击的方块
         *
         * @return 已处理完事件返回true，否则返回false.
         * */
        boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square);

        /**
         * 当点击到方块外时回调该方法
         *
         * @param event 事件对象
         * @param square
         *
         * @return 已处理完事件返回true，否则返回false.
         * */
        boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square);
    }

    public interface OnGameOverListener {

        /**
         * 判断游戏是否结束
         *
         * @param squareList 当前的所有方块信息集合
         *
         * @return 返回true表示游戏结束
         * */
        boolean onIsGameOver(List<Square> squareList);

        /**
         * 游戏结束处理方法，当游戏结束时会回调该方法
         *
         * @param surfaceHolder
         * @param rect 游戏结束点所在区域
         * */
        void onHandleGameOver(SurfaceHolder surfaceHolder, Square square);
    }
}
