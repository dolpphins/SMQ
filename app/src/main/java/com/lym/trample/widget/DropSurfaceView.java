package com.lym.trample.widget;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.lym.trample.bean.Square;
import com.lym.trample.generator.BaseSquareGenerator;
import com.lym.trample.generator.SquareGeneratorConfiguration;
import com.lym.trample.generator.impl.DefaultSquareGenerator;

import java.sql.SQLClientInfoException;
import java.util.LinkedList;
import java.util.List;

public class DropSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public enum Status {

        STOPPED,
        READY,
        RUNNING,
        PAUSE
    }

    private Context mContext;

    private SurfaceHolder mSurfaceHolder;

    private DropViewConfiguration mDropViewConfiguration;

    private List<Square> mSquareList;
    private List<Square> mSquareListTemp;
    private volatile Status mStatus;
    private boolean mIsCreated;
    private Thread mDropThread;

    private OnDrawSurfaceViewListener mDrawSurfaceViewListener;
    private int mSpeed;

    private SquareGeneratorConfiguration mSquareGeneratorConfiguration;
    private int mMinY;

    private OnSurfaceViewTouchListener mSurfaceViewTouchListener;

    private OnGameOverListener mGameOverListener;
    private Square mGameOverSquare;
    private boolean mIsGameOver;
    private int mGameOverTwinkleCount;
    private int mGameOverBackDistance;

    private Handler mSurfaceViewHandler;

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
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mDropViewConfiguration = new DropViewConfiguration.Builder(mContext).build();

        mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);

        mSquareList = new LinkedList<Square>();
        mSquareListTemp = new LinkedList<Square>();

        mStatus = Status.STOPPED;
        mIsCreated = false;
        mSpeed = 0;

        mGameOverTwinkleCount = 2;
        mGameOverBackDistance = mDropViewConfiguration.getSquareHeight();

        mSurfaceViewHandler = new SurfaceViewHandler();
    }

    public void setConfiguration(DropViewConfiguration config) {
        mDropViewConfiguration = config;

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

        mGameOverBackDistance = mDropViewConfiguration.getSquareHeight();
    }

    public DropViewConfiguration getDropViewConfiguration() {
        return mDropViewConfiguration;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsCreated = true;
        if (mStatus == Status.STOPPED || mStatus == Status.READY) {
            mSquareList.clear();
            mSquareListTemp.clear();
            drawStartLayout();

        } else if(mStatus == Status.PAUSE) {
            drawOnce();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsCreated = false;
        pause();
    }

    private void startDropThread() {
        if (mStatus == Status.READY) {
            mDropThread = new DropThread();
            mDropThread.start();
        }
    }

    public void reset() {
        if (mSquareList == null) {
            mSquareList = new LinkedList<Square>();
        } else {
            mSquareList.clear();
        }
        if (mSquareListTemp == null) {
            mSquareListTemp = new LinkedList<Square>();
        } else {
            mSquareListTemp.clear();
        }
        mStatus = Status.STOPPED;
        mSpeed = 0;
    }

    public void start() {
        if (mStatus == Status.READY) {
            if (mIsCreated) {
                startDropThread();
            } else {

            }
        }
    }

    public void pause() {
        if (mStatus == Status.RUNNING) {
            mStatus = Status.PAUSE;
        }
    }

    public void resume() {
        if (mStatus == Status.PAUSE) {
            mStatus = Status.RUNNING;
        }
    }

    public Status getStatus() {
        return mStatus;
    }

    public void stop(Square square, int type) {
        if (mStatus == Status.RUNNING) {
            mStatus = Status.STOPPED;
        }
        switch (type) {
            case OnGameOverListener.GAME_OVER_NO_PRESS_TYPE:
                performGameOverEffect(square, true);
                break;
            case OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE:
                performGameOverEffect(square, false);
                break;
            case OnGameOverListener.GAME_OVER_SQUARE_ERROR_TYPE:
                performGameOverEffect(square, true);
                break;
        }

        Message msg = Message.obtain();
        msg.what = SurfaceViewHandler.GAME_OVER_HANDLE_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putSerializable("square", square);
        bundle.putInt("type", type);
        mSurfaceViewHandler.sendMessage(msg);
    }


    public void setOnDrawSurfaceViewListener(OnDrawSurfaceViewListener l) {
        mDrawSurfaceViewListener = l;
    }

    public OnDrawSurfaceViewListener getOnDrawSurfaceViewListener() {
        return mDrawSurfaceViewListener;
    }

    public void setOnSurfaceViewTouchListener(OnSurfaceViewTouchListener l) {
        mSurfaceViewTouchListener = l;
    }

    public OnSurfaceViewTouchListener getOnSurfaceViewTouchListener() {
        return mSurfaceViewTouchListener;
    }

    public void setOnGameOverListener(OnGameOverListener l) {
        mGameOverListener = l;
    }

    public OnGameOverListener getOnGameOverListener() {
        return mGameOverListener;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    public void setGameOverBackDistance(int distance) {
        mGameOverBackDistance = distance;
    }

    public int getGameOverBackDistance() {
        return mGameOverBackDistance;
    }

    private void drawStartLayout() {
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

    private void clearCanvas(Canvas canvas) {
        if(canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        }
    }

    private void drawBaseView(Canvas canvas) {
        if(canvas == null) {
            return;
        }
        if(mSurfaceHolder == null) {
            return;
        }
        if(mDropViewConfiguration == null) {
            return;
        }
        Rect r = mDropViewConfiguration.getRect();
        if(r == null) {
            return;
        }

        canvas.drawColor(mDropViewConfiguration.getCanvasColor());

        int pipeCount = mDropViewConfiguration.getPipeCount();
        if(pipeCount <= 0) {
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
        paint.setStrokeWidth(mDropViewConfiguration.getPipeBorderWidth());
        paint.setColor(mDropViewConfiguration.getPipeBorderColor());
        try {
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
            } else {
                srcList = mSquareList;
                dstList = mSquareListTemp;
            }
            moveOneStep(srcList);
            dstList.clear();
            copyList(srcList, dstList);
            srcList.clear();
            if(canGenerateSquare(dstList)) {
                BaseSquareGenerator generator = new DefaultSquareGenerator();
                List<Square> generatedSquareList = generator.generate(mSquareGeneratorConfiguration);
                if (generatedSquareList != null) {
                    dstList.addAll(generatedSquareList);
                } else {
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

    private void backOneStep(List<Square> squareList) {
        for(Square square : squareList) {
            square.setStartY(square.getStartY() - mSpeed);
            square.setEndY(square.getEndY() - mSpeed);
        }
    }

    private void backOneStep(List<Square> squareList, int distance) {
        for(Square square : squareList) {
            square.setStartY(square.getStartY() - distance);
            square.setEndY(square.getEndY() - distance);
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
                    } else if(mStatus == Status.RUNNING) {
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

    private int getPipeIndexByPoint(float x) {
        int index = (int) (x - mDropViewConfiguration.getRect().left) / mDropViewConfiguration.getPipeWidth();
        return index;
    }

    private boolean isGameOver() {
        List<Square> squareList = getCurrentSquareList();
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
            stop(mGameOverSquare, OnGameOverListener.GAME_OVER_NO_PRESS_TYPE);
            mGameOverSquare = null;
        }
    }

    public void setGameOverRect(Square square) {
        mGameOverSquare = square;
    }

    public void performGameOverEffect(Square square) {
        performGameOverEffect(square, true);
    }

    public void performGameOverEffect(Square square, boolean fillAfter) {
        performGameOverEffect(mSurfaceHolder, square, fillAfter);
    }

    public void performGameOverEffect(SurfaceHolder holder, Square square, boolean fillAfter) {
        if(holder == null || square == null) {
            return;
        }
        Canvas canvas = null;
        int rollbackDistance = 0;
        int stepDistance;
        int backSpeed = mGameOverBackDistance / 10;
        List<Square> squareList = getCurrentSquareList();
        while(rollbackDistance < mGameOverBackDistance) {
            if(rollbackDistance + backSpeed > mGameOverBackDistance) {
                stepDistance = mGameOverBackDistance - rollbackDistance;
            } else {
                stepDistance = backSpeed;
            }
            rollbackDistance += stepDistance;
            try {
                canvas = holder.lockCanvas();
                clearCanvas(canvas);
                drawBaseView(canvas);
                backOneStep(squareList, stepDistance);
                drawData(canvas, false);
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        Paint bgPaint = new Paint();
        bgPaint.setColor(mDropViewConfiguration.getCanvasColor());
        int count = fillAfter ? 2 * mGameOverTwinkleCount : 2 * mGameOverTwinkleCount + 1;
        square.setStartX(square.getStartX() + mDropViewConfiguration.getPipeBorderWidth());
        for(int i = 0; i < count; i++) {
            try {
                Thread.sleep(100);

                canvas = holder.lockCanvas(square.toRect());

                if(i % 2 == 0) {
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

    private void drawOnce() {
        Canvas canvas = null;
        try {

            canvas = mSurfaceHolder.lockCanvas();
            clearCanvas(canvas);
            drawBaseView(canvas);
            updateData();
            drawData(canvas, false);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    //绘制线程
    private class DropThread extends Thread {

        @Override
        public void run() {

            mSquareList.clear();
            mSquareListTemp.clear();
            mStatus = Status.RUNNING;
            mIsGameOver = false;

            while (!Thread.currentThread().isInterrupted()) {
                if(mStatus == Status.STOPPED) {
                    break;
                }
                if (mStatus == Status.PAUSE) {
                    continue;
                }
                drawOnce();
                if(isGameOver()) {
                    mIsGameOver = true;
                    break;
                }
            }

            mStatus = Status.STOPPED;
            if(mIsGameOver) {
                handleGameOver();
            }
        }
    }

    public interface OnDrawSurfaceViewListener {

        void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started);
    }

    public interface OnSurfaceViewTouchListener {

        boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square);

        boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square);
    }

    public interface OnGameOverListener {

        int GAME_OVER_NO_PRESS_TYPE = 0;

        int GAME_OVER_SQUARE_ERROR_TYPE = 1;

        int GAME_OVER_OUT_SQUARE_TYPE = 2;

        boolean onIsGameOver(List<Square> squareList);

        void onHandleGameOver(Square square, int type);
    }

    private class SurfaceViewHandler extends Handler {

        private final static int GAME_OVER_HANDLE_MESSAGE = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GAME_OVER_HANDLE_MESSAGE:
                    if(mGameOverListener != null) {
                        Bundle bundle = msg.getData();
                        Square square = (Square) bundle.get("square");
                        int type = bundle.getInt("type");
                        mGameOverListener.onHandleGameOver(square, type);
                    }
                    break;
            }
        }
    }
}
