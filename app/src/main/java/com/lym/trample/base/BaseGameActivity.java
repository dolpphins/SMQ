package com.lym.trample.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lym.trample.NetworkDataManager;
import com.lym.trample.R;
import com.lym.trample.ScoresManager;
import com.lym.trample.bean.TUser;
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

    private boolean isShowingGameOverDialog;
    private boolean isShowingGamePauseDialog;

    /** 初始速度 */
    private int mInitSpeed;
    /** 速度 */
    private int mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_base_game_activity);

        init();

        //重新获取数据
        NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
        ndm.requestUpdateData();
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

        drop_main_surfaceview.setSpeed(mSpeed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(drop_main_surfaceview != null
                && DropSurfaceView.Status.PAUSE == drop_main_surfaceview.getStatus()) {
            if(!isShowingGamePauseDialog) {
                showGamePauseDialog();
            }
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
        isShowingGamePauseDialog = true;
    }

    //显示游戏结束对话框
    public void showGameOverDialiog() {
        if(mGameOverDialog == null) {
            mGameOverDialog = new GameOverDialog(this);
            mGameOverDialog.setOnCustomDialogListener(new GameOverDialogListener());
        }
        TUser user = new TUser();
        user.setBest_color_score(ScoresManager.bestUserColorScore);
        user.setBest_digit_score(ScoresManager.bestUserDigitScore);
        user.setBest_line_score(ScoresManager.bestUserLineScore);
        user.setGuid(ScoresManager.guid);
        //上传数据
        int temp = user.getScore(createColumnName());
        if(mScores > temp) {
            user.setScore(mScores, createColumnName());
            NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
            ndm.updateOneUserToBmob(user);
        }
        //更新数据
        ScoresManager.updateUserScores(user);
        //传递数据
        setScores(mGameOverDialog);

        mGameOverDialog.show();
        isShowingGameOverDialog = true;

        //更新数据
        ScoresManager.updateBestScores(user);
        //更新缓存
        ScoresManager.updateCacheSp(this);
    }

    /**
     * Bmob列名
     *
     * @return 列名，不能为空
     */
    protected abstract String createColumnName();

    protected void setScores(GameOverDialog dialog) {
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
            isShowingGamePauseDialog = false;
        }
    }

    private void restart() {
        if(drop_main_surfaceview != null && drop_main_parent != null) {
            drop_main_surfaceview.reset();
            drop_main_parent.removeView(drop_main_surfaceview);
            drop_main_parent.addView(drop_main_surfaceview, 0);
            drop_main_surfaceview.setSpeed(mInitSpeed);
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

    /**
     * 设置下落速度
     *
     * @param speed
     */
    public void setSpeed(int speed) {
        this.mSpeed = speed;
        drop_main_surfaceview.setSpeed(mSpeed);
    }

    /**
     * 获取下落速度
     *
     * @return
     */
    public int getSpeed() {
        return mSpeed;
    }

    /**
     * 设置初始速度
     *
     * @param initSpeed
     */
    public void setInitSpeed(int initSpeed) {
        this.mInitSpeed = initSpeed;
    }

    /**
     * 获取初始速度
     *
     * @return
     */
    public int getInitSpeed() {
        return mInitSpeed;
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
