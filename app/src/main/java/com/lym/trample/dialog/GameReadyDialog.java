package com.lym.trample.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseDialog;

/**
 * 游戏开始前，给用户提供一些游戏相关信息的对话框。
 * 用户可以选择开始游戏或者返回主菜单。
 * 实现OnCustomDialogListener接口，即可监听用户的选择。
 * onDialogButtonClick事件的参数有以下两个取值：
 * BaseDialog.OnCustomDialogListener.START_GAME表示用户选择开始游戏。
 * BaseDialog.OnCustomDialogListener.GO_BACK表示用户选择返回。
 * Created by 卢沛东 on 2015/11/7.
 */
public class GameReadyDialog extends BaseDialog implements View.OnClickListener {

    private TextView global_highest_score = null;
    private TextView my_highest_score = null;
    private Button start_game = null;
    private Button go_back = null;

    private int mGlobalHighestScore = 0;
    private int mMyHighestScore = 0;

    public GameReadyDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        init();
    }

    private void init() {
        mDialog.setContentView(R.layout.dlg_game_ready);
        Window window = mDialog.getWindow();

        global_highest_score = (TextView) window.findViewById(R.id.global_highest_score);
        my_highest_score = (TextView) window.findViewById(R.id.my_highest_score);
        start_game = (Button) window.findViewById(R.id.start_game);
        go_back = (Button) window.findViewById(R.id.go_back);

        global_highest_score.setText(mGlobalHighestScore + "");
        my_highest_score.setText(mMyHighestScore + "");
        start_game.setOnClickListener(this);
        go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mListener == null) return;
        switch (view.getId()) {
            case R.id.start_game:
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_READY_DIALOG_START_GAME);
                break;
            case R.id.go_back:
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_READY_DIALOG_GO_BACK);
                break;
        }
    }

    public int getGlobalHighestScore() {
        return mGlobalHighestScore;
    }

    public void setGlobalHighestScore(int mGlobalHighestScore) {
        this.mGlobalHighestScore = mGlobalHighestScore;
    }

    public int getMyHighestScore() {
        return mMyHighestScore;
    }

    public void setMyHighestScore(int mMyHighestScore) {
        this.mMyHighestScore = mMyHighestScore;
    }
}
