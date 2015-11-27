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

import com.lym.stamp.R;
import com.lym.trample.ScoresManager;
import com.lym.trample.base.BaseDialog;

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
    private int mLeft;
    private int mTop;
    int mDistance;

    private boolean mRunningManFillAfter;

    public GameOverDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void show(ScoresManager.Status status) {
        super.show();
        init(status);
        new RankingTask().execute();
    }

    public void setRunningManPos() {
        setRunningManPos(mDistance);
    }

    public void setRunningManPos(int offsetX) {
        mLeft = ranking_progress.getLeft() + offsetX - running_man_layout.getWidth()/2;
        mTop= running_man_layout.getTop();
        int width = running_man_layout.getWidth();
        int height = running_man_layout.getHeight();
        running_man_layout.clearAnimation();
        running_man_layout.layout(mLeft, mTop, mLeft + width, mTop + height);

    }

    private void init(ScoresManager.Status status) {
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

        if(status == ScoresManager.Status.SUCCESS) {
            game_over_global_highest_score.setText(mGlobalHighestScore + "");
        } else if(status == ScoresManager.Status.FAIL) {
            game_over_global_highest_score.setText(mContext.getResources().getString(R.string.game_dialog_score_fail_tip));
        } else if(status == ScoresManager.Status.GETTING) {
            game_over_global_highest_score.setText(mContext.getResources().getString(R.string.game_dialog_score_getting_tip));
        }

        game_over_my_highest_score.setText(String.valueOf(mMyHighestScore));
        current_score.setText(String.valueOf(mCurrentScore));
        setGameOverTip(status);

        play_again.setOnClickListener(this);
        go_to_main_activity.setOnClickListener(this);

        mRunningManFillAfter = false;
        window.getDecorView().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(){

            @Override
            public boolean onPreDraw() {

                if(mRunningManFillAfter) {
                    setRunningManPos();
                }
                return true;
            }
        });

    }

    public void requestSetRunningManFillAfter() {
        mRunningManFillAfter = true;
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

        int mDuration = 1500;
        int mMax;
        int mCurrentPosition = 0;
        int mDesPosition;
        int mGap;

        @Override
        protected void onPreExecute() {
            mMax = mGlobalHighestScore > mCurrentScore ? mGlobalHighestScore : mCurrentScore;
            ranking_progress.setMax(mMax);
            mDesPosition = mCurrentScore;
            mGap = mDesPosition / (mDuration / 10);
            mGap = mGap == 0 ? 1 : mGap;

            AnimationDrawable mRunningAnimation = (AnimationDrawable)running_man.getBackground();
            mRunningAnimation.setOneShot(false);
            mRunningAnimation.start();

            ViewTreeObserver vto = ranking_progress.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    ranking_progress.getViewTreeObserver().removeOnPreDrawListener(this);
                    mDistance = ranking_progress.getMeasuredWidth();
                    mDistance = (int)(mDesPosition * 1.0 / mMax * mDistance);

                    Animation moveToRight = new TranslateAnimation(0f, mDistance, 0f, 0f);
                    moveToRight.setDuration(mDuration);
                    moveToRight.setInterpolator(new LinearInterpolator());
                    moveToRight.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            game_over_tip.setVisibility(View.VISIBLE);
                            game_over_tip.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.dialog_font_animation));

                            requestSetRunningManFillAfter();
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

    private void setGameOverTip(ScoresManager.Status status) {
        if (status == ScoresManager.Status.SUCCESS && mCurrentScore > mGlobalHighestScore) {
            game_over_tip.setText(mContext.getString(R.string.over_global_score));
        }
//        else if (mCurrentScore >= mGlobalHighestScore - 10) {
//            game_over_tip.setText(mContext.getString(R.string.not_far_to_break_record));
//        }
//        else if (mCurrentScore == mMyHighestScore) {
//            game_over_tip.setText(mContext.getString(R.string.good_job));
//        }
//        else {
//            game_over_tip.setText(mContext.getString(R.string.need_work_hard));
//        }
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
