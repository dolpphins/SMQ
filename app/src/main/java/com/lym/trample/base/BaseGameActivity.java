package com.lym.trample.base;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.dialog.GameOverDialog;
import com.lym.trample.dialog.GamePauseDialog;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;

/**
 * Created by mao on 2015/11/12.
 *
 * 基本的游戏Activity
 *
 * @author 麦灿标
 */
public abstract class BaseGameActivity extends BaseActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
        DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private final static String TAG = "BaseGameActivity";

    private DropSurfaceView drop_main_surfaceview;
    private DropViewConfiguration config;

    private TextView drop_main_scores;
    private int mScores;

    private FrameLayout drop_main_parent;

    private GameOverDialog mGameOverDialog;
    private GamePauseDialog mGamePauseDialog;

    /** 速度 */
    private int mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_base_game_activity);

        init();
    }

    private void init() {
        drop_main_surfaceview = (DropSurfaceView) findViewById(R.id.drop_main_surfaceview);
        drop_main_scores = (TextView) findViewById(R.id.drop_main_scores);
        drop_main_parent = (FrameLayout) findViewById(R.id.drop_main_parent);

        drop_main_scores.setText(mScores + "");
        config = new DropViewConfiguration.Builder(this)
                .setPipeBorderColor(Color.parseColor("#0000ff"))
                .setPipeCount(4)
                .setCanvasColor(Color.WHITE)
                .build();
        drop_main_surfaceview.setConfiguration(config);
        drop_main_surfaceview.setOnDrawSurfaceViewListener(this);
        drop_main_surfaceview.setOnSurfaceViewTouchListener(this);
        drop_main_surfaceview.setOnGameOverListener(this);

        mSpeed = 18 * config.getRect().height() / 1920;
        drop_main_surfaceview.setSpeed(mSpeed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drop_main_surfaceview != null
                && DropSurfaceView.Status.PAUSE == drop_main_surfaceview.getStatus()) {
            showGamePauseDialog();
        }
    }

    //显示暂停对话框
    public void showGamePauseDialog() {
        if(mGamePauseDialog == null) {
            mGamePauseDialog = new GamePauseDialog(this);
            mGamePauseDialog.setOnCustomDialogListener(new PauseDialogListener());
        }
        drop_main_surfaceview.pause();
        mGamePauseDialog.show();
    }

    //显示游戏结束对话框
    public void showGameOverDialiog() {
        if(mGameOverDialog == null) {
            mGameOverDialog = new GameOverDialog(this, mScores);
            mGameOverDialog.setOnCustomDialogListener(new GameOverDialogListener());
        }
        mGameOverDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(drop_main_surfaceview != null
                    && DropSurfaceView.Status.RUNNING == drop_main_surfaceview.getStatus()) {
                showGamePauseDialog();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void resume() {
        if(drop_main_surfaceview != null) {
            drop_main_surfaceview.resume();
        }
        if(mGamePauseDialog != null) {
            mGamePauseDialog.dismiss();
        }
    }

    private void restart() {
        if(drop_main_surfaceview != null && drop_main_parent != null) {
            drop_main_surfaceview.reset();
            drop_main_parent.removeView(drop_main_surfaceview);
            drop_main_parent.addView(drop_main_surfaceview, 0);
            drop_main_surfaceview.setSpeed(mSpeed);
            mGameOverDialog.dismiss();
        }
        mScores = 0;
        if(drop_main_scores != null) {
            drop_main_scores.setText(mScores + "");
        }
    }

    /**
     * 更新分数
     *
     * @param score 分数
     * */
    public void updateScores(int score) {
        mScores = score;
        if(drop_main_scores != null) {
            drop_main_scores.setText(mScores + "");
        }
    }

    public DropSurfaceView getDropSurfaceview() {
        return drop_main_surfaceview;
    }

    public void setDropSurfaceview(DropSurfaceView surfaceview) {
        this.drop_main_surfaceview = surfaceview;
    }

    public DropViewConfiguration getConfig() {
        return config;
    }

    public void setConfig(DropViewConfiguration config) {
        this.config = config;
    }

    public TextView getDropScores() {
        return drop_main_scores;
    }

    public void setDropScores(TextView scores) {
        this.drop_main_scores = scores;
    }

    public int getScores() {
        return mScores;
    }

    public void setScores(int scores) {
        this.mScores = scores;
    }

    private class GameOverDialogListener implements GameOverDialog.OnCustomDialogListener {

        @Override
        public void onDialogButtonClick(int userChoose) {
            switch (userChoose) {
                //返回主菜单
                case GameOverDialog.OnCustomDialogListener.GAME_OVER_DIALOG_GO_TO_MAIN_ACTIVITY:
                    finish();
                    break;
                //再玩一次
                case GameOverDialog.OnCustomDialogListener.GAME_OVER_DIALOG_PLAY_AGAIN:
                    restart();
                    break;
            }
        }
    }

    private class PauseDialogListener implements GamePauseDialog.OnCustomDialogListener {

        @Override
        public void onDialogButtonClick(int userChoose) {
            switch (userChoose) {
                case GamePauseDialog.OnCustomDialogListener.GAME_PAUSE_DIALOG_RESUME:
                    resume();
                    break;
                case GamePauseDialog.OnCustomDialogListener.GAME_PAUSE_DIALOG_TERMINATE:
                    finish();
                    break;
            }
        }
    }
}
