package com.lym.trample.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseDialog;

/**
 * 游戏结束后，给用户提供本局游戏相关信息的对话框。
 * 用户可以选择再玩一局或者返回主菜单。
 * 实现OnCustomDialogListener接口，即可监听用户的选择。
 * onDialogButtonClick事件的参数有以下两个取值：
 * BaseDialog.OnCustomDialogListener.OnCustomDialogListener.PLAY_AGAIN表示用户选择开始游戏。
 * BaseDialog.OnCustomDialogListener.GO_TO_MAIN_ACTIVITY表示用户选择返回主菜单。
 * Created by 卢沛东 on 2015/11/7.
 */
public class GameOverDialog extends BaseDialog implements View.OnClickListener {

    private TextView game_over_global_highest_score = null;
    private TextView game_over_my_highest_score = null;
    private TextView current_score = null;
    private TextView game_over_tip = null;
    private Button play_again = null;
    private Button go_to_main_activity = null;
    private ProgressBar ranking_progress = null;
    private ImageView running_man = null;
    private LinearLayout running_man_layout = null;

    private Context mContext = null;
    private int mGlobalHighestScore = 0;
    private int mMyHighestScore = 0;
    private int mCurrentScore = 0;
    /** 保存小人最后停留的位置信息 */
    private int mLeft;
    private int mTop;
    /** 小人需要奔跑的距离 */
    int mDistance;

    public GameOverDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void show() {
        super.show();
        init();
        new RankingTask().execute();
    }

    /**
     * 设置小人停留在动画结束时的位置
     */
    public void setRunningManPos() {
        mLeft = ranking_progress.getLeft() + mDistance - running_man_layout.getWidth()/2;
        mTop= running_man_layout.getTop();
        int width = running_man_layout.getWidth();
        int height = running_man_layout.getHeight();
        running_man_layout.clearAnimation();
        running_man_layout.layout(mLeft, mTop, mLeft + width, mTop + height);
    }

    private void init() {
        mDialog.setContentView(R.layout.dlg_game_over);
        Window window = mDialog.getWindow();

        game_over_global_highest_score = (TextView)window.findViewById(R.id.game_over_global_highest_score);
        game_over_my_highest_score = (TextView)window.findViewById(R.id.game_over_my_highest_score);
        current_score = (TextView)window.findViewById(R.id.current_score);
        game_over_tip = (TextView)window.findViewById(R.id.game_over_tip);
        play_again = (Button)window.findViewById(R.id.play_again);
        go_to_main_activity = (Button)window.findViewById(R.id.go_to_main_activity);
        ranking_progress = (ProgressBar)window.findViewById(R.id.ranking_progressbar);
        running_man = (ImageView)window.findViewById(R.id.running_man);
        running_man_layout = (LinearLayout)window.findViewById(R.id.running_man_layout);

        game_over_global_highest_score.setText(String.valueOf(mGlobalHighestScore));
        game_over_my_highest_score.setText(String.valueOf(mMyHighestScore));
        current_score.setText(String.valueOf(mCurrentScore));
        setGameOverTip();

        play_again.setOnClickListener(this);
        go_to_main_activity.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (mListener == null) return;
        switch (view.getId()) {
            case R.id.play_again:
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_OVER_DIALOG_PLAY_AGAIN);
                break;
            case R.id.go_to_main_activity:
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_OVER_DIALOG_GO_TO_MAIN_ACTIVITY);
                break;
        }
    }

    class RankingTask extends AsyncTask<Void, Integer, Void> {

        /** 进度条与小人前进的时间 */
        int mDuration = 1500;
        /** 进度条最大值 */
        int mMax;
        /** 进度条当前位置 */
        int mCurrentPosition = 0;
        /** 进度条目标位置 */
        int mDesPosition;
        /** 进度条位移增量 */
        int mGap;

        @Override
        protected void onPreExecute() {
            mMax = mGlobalHighestScore > mCurrentScore ? mGlobalHighestScore : mCurrentScore;
            //设置进度条属性
            ranking_progress.setMax(mMax);
            //计算进度条该到达的位置
            mDesPosition = mCurrentScore;
            //计算其增量
            mGap = mDesPosition / (mDuration / 10);
            mGap = mGap == 0 ? 1 : mGap;

            //设置小人奔跑动画
            AnimationDrawable mRunningAnimation = (AnimationDrawable)running_man.getBackground();
            mRunningAnimation.setOneShot(false);
            mRunningAnimation.start();

            ViewTreeObserver vto = ranking_progress.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    ranking_progress.getViewTreeObserver().removeOnPreDrawListener(this);
                    //获取进度条宽度
                    mDistance = ranking_progress.getMeasuredWidth();
                    //计算小人应奔跑的距离
                    mDistance = (int)(mDesPosition * 1.0 / mMax * mDistance);

                    //小人平移动画
                    Animation moveToRight = new TranslateAnimation(0f, mDistance, 0f, 0f);
                    moveToRight.setDuration(mDuration);
                    //平移速度为匀速
                    moveToRight.setInterpolator(new LinearInterpolator());
                    //监听动画事件，当动画结束时，使小人停在最终位置
                    moveToRight.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            setRunningManPos();
                            //开始提示文字动画
                            game_over_tip.setVisibility(View.VISIBLE);
                            game_over_tip.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_font_animation));
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    running_man_layout.startAnimation(moveToRight);
                    return true;
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                while (mCurrentPosition < mDesPosition) {
                    Thread.sleep(10);
                    if (mCurrentPosition + mGap > mDesPosition) {
                        mCurrentPosition = mDesPosition;
                    }
                    else {
                        mCurrentPosition += mGap;
                    }
                    publishProgress(mCurrentPosition);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            ranking_progress.setProgress(values[0]);
        }

    }

    private void setGameOverTip() {
        if (mCurrentScore > mGlobalHighestScore) {
            game_over_tip.setText(mContext.getString(R.string.over_global_score));
        }
        else if (mCurrentScore >= mGlobalHighestScore - 10) {
            game_over_tip.setText(mContext.getString(R.string.not_far_to_break_record));
        }
        else if (mCurrentScore == mMyHighestScore) {
            game_over_tip.setText(mContext.getString(R.string.good_job));
        }
        else {
            game_over_tip.setText(mContext.getString(R.string.need_work_hard));
        }
    }

    public int getMyHighestScore() {
        return mMyHighestScore;
    }

    public void setMyHighestScore(int mHistoryHighestScore) {
        this.mMyHighestScore = mHistoryHighestScore;
    }

    public int getCurrentScore() {
        return mCurrentScore;
    }

    public void setCurrentScore(int mCurrentScore) {
        this.mCurrentScore = mCurrentScore;
    }

    public int getGlobalHighestScoe() {
        return mGlobalHighestScore;
    }

    public void setGlobalHighestScoe(int mGlobalHighestScoe) {
        this.mGlobalHighestScore = mGlobalHighestScoe;
    }
}
