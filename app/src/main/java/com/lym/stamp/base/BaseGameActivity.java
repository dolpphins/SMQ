package com.lym.stamp.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lym.stamp.NetworkDataManager;
import com.lym.stamp.R;
import com.lym.stamp.ScoresManager;
import com.lym.stamp.bean.TUser;
import com.lym.stamp.dialog.GameOverDialog;
import com.lym.stamp.dialog.GamePauseDialog;
import com.lym.stamp.widget.DropSurfaceView;
import com.lym.stamp.widget.DropViewConfiguration;

public abstract class BaseGameActivity extends BaseActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
        DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private DropSurfaceView drop_main_surfaceview;
    private DropViewConfiguration config;

    private TextView drop_main_scores;
    private int mScores;

    private FrameLayout drop_main_parent;

    private GameOverDialog mGameOverDialog;
    private GamePauseDialog mGamePauseDialog;

    private boolean isShowingGameOverDialog;
    private boolean isShowingGamePauseDialog;

    private int mInitSpeed;

    private int mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_base_game_activity);

        init();

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

    public void showGamePauseDialog() {
        if(mGamePauseDialog == null) {
            mGamePauseDialog = new GamePauseDialog(this);
            mGamePauseDialog.setOnCustomDialogListener(new PauseDialogListener());
        }
        drop_main_surfaceview.pause();
        mGamePauseDialog.show();
        isShowingGamePauseDialog = true;
    }

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

        int temp = user.getScore(createColumnName());
        if(mScores > temp) {
            user.setScore(mScores, createColumnName());
            NetworkDataManager ndm = new NetworkDataManager(getApplicationContext());
            ndm.updateOneUserToBmob(user);
        }

        ScoresManager.updateUserScores(user);

        setScores(mGameOverDialog);

        mGameOverDialog.show(getBestScoreStatus());
        isShowingGameOverDialog = true;

        ScoresManager.updateBestScores(user);

        ScoresManager.updateCacheSp(this);
    }

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

    public void updateScores(int score) {
        mScores = score;
        if(drop_main_scores != null) {
            drop_main_scores.setText(mScores + "");
        }
    }

    public void setSpeed(int speed) {
        this.mSpeed = speed;
        drop_main_surfaceview.setSpeed(mSpeed);
    }

    public int getSpeed() {
        return mSpeed;
    }

    public void setInitSpeed(int initSpeed) {
        this.mInitSpeed = initSpeed;
    }

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
                case GameOverDialog.OnCustomDialogListener.GAME_OVER_DIALOG_GO_TO_MAIN_ACTIVITY:
                    finish();
                    break;
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

    protected abstract ScoresManager.Status getBestScoreStatus();
}
