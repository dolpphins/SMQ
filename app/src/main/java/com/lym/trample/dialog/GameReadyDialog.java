package com.lym.trample.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lym.trample.R;

/**
 * 游戏开始前，给用户提供一些游戏相关信息的对话框。
 * 用户可以选择开始游戏或者返回主菜单。
 * 实现OnCustomDialogListener接口，即可监听用户的选择。
 * onDialogButtonClick事件的参数有以下两个取值：
 * GameReadyDialog.START_GAME表示用户选择开始游戏。
 * GameReadyDialog.GO_BACK表示用户选择返回。
 * Created by 卢沛东 on 2015/11/7.
 */
public class GameReadyDialog extends BaseDialog implements View.OnClickListener {

    /**
     * 用户选择开始游戏
     */
    public static final int START_GAME = 0;
    /**
     * 用户选择返回
     */
    public static final int GO_BACK = 1;

    private TextView number_of_participants = null;
    private TextView global_highest_score = null;
    private TextView my_highest_score = null;
    private TextView my_rank = null;
    private Button start_game = null;
    private Button go_back = null;

    public GameReadyDialog(Context context) {
        super(context);
    }

    /**
     * 显示对话框
     */
    public void show() {
        super.show();
        init();
    }

    private void init() {
        mDialog.setContentView(R.layout.dlg_game_ready);
        Window window = mDialog.getWindow();

        number_of_participants = (TextView) window.findViewById(R.id.number_of_participants);
        global_highest_score = (TextView) window.findViewById(R.id.global_highest_score);
        my_highest_score = (TextView) window.findViewById(R.id.my_highest_score);
        my_rank = (TextView) window.findViewById(R.id.my_rank);
        start_game = (Button) window.findViewById(R.id.start_game);
        go_back = (Button) window.findViewById(R.id.go_back);

        number_of_participants.setText("1000");
        global_highest_score.setText("100");
        my_highest_score.setText("50");
        my_rank.setText("600");
        start_game.setOnClickListener(this);
        go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_game:
                mListener.onDialogButtonClick(START_GAME);
                break;
            case R.id.go_back:
                mListener.onDialogButtonClick(GO_BACK);
                break;
        }
    }
}
