package com.lym.trample.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lym.trample.R;

/**
 * 游戏结束后，给用户提供本局游戏相关信息的对话框。
 * 用户可以选择再玩一局或者返回主菜单。
 * 实现OnCustomDialogListener接口，即可监听用户的选择。
 * onDialogButtonClick事件的参数有以下两个取值：
 * GameOverDialog.PLAY_AGAIN表示用户选择开始游戏。
 * GameOverDialog.GO_TO_MAIN_ACTIVITY表示用户选择返回主菜单。
 * Created by 卢沛东 on 2015/11/7.
 */
public class GameOverDialog extends BaseDialog implements View.OnClickListener {

    /**
     * 用户选择再玩一局
     */
    public static final int PLAY_AGAIN = 4;
    /**
     * 用户选择返回主界面
     */
    public static final int GO_TO_MAIN_ACTIVITY = 5;

    private TextView history_highest_score = null;
    private TextView current_score = null;
    private TextView score_change_tip = null;
    private TextView history_rank = null;
    private TextView current_rank = null;
    private TextView rank_change_tip = null;
    private TextView game_over_tip = null;
    private Button play_again = null;
    private Button go_to_main_activity = null;

    private Context mContext;

    public GameOverDialog(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 显示对话框
     */
    public void show() {
        super.show();
        init();
    }

    private void init() {
        mDialog.setContentView(R.layout.dlg_game_over);
        Window window = mDialog.getWindow();

        history_highest_score = (TextView)window.findViewById(R.id.history_highest_score);
        current_score = (TextView)window.findViewById(R.id.current_score);
        score_change_tip = (TextView)window.findViewById(R.id.score_change_tip);
        history_rank = (TextView)window.findViewById(R.id.history_rank);
        current_rank = (TextView)window.findViewById(R.id.current_rank);
        rank_change_tip = (TextView)window.findViewById(R.id.rank_change_tip);
        game_over_tip = (TextView)window.findViewById(R.id.game_over_tip);
        play_again = (Button)window.findViewById(R.id.play_again);
        go_to_main_activity = (Button)window.findViewById(R.id.go_to_main_activity);

        history_highest_score.setText("50");
        current_score.setText("100");
        score_change_tip.setText(mContext.getString(R.string.break_record));
        history_rank.setText("600");
        current_rank.setText("100");
        rank_change_tip.setText(mContext.getString(R.string.ranking_up));
        game_over_tip.setText(mContext.getString(R.string.good_job));
        play_again.setOnClickListener(this);
        go_to_main_activity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_again:
                mListener.onDialogButtonClick(PLAY_AGAIN);
                break;
            case R.id.go_to_main_activity:
                mListener.onDialogButtonClick(GO_TO_MAIN_ACTIVITY);
                break;
        }
    }
}
