package com.lym.trample.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lym.trample.R;

/**
 * 游戏暂停对话框。
 * 用户可以选择终止游戏或者继续游戏。
 * 实现OnCustomDialogListener接口，即可监听用户的选择。
 * onDialogButtonClick事件的参数有以下两个取值：
 * GamePauseDialog.TERMINATE表示用户选择终止游戏。
 * GamePauseDialog.RESUME表示用户选择继续游戏。
 * Created by 卢沛东 on 2015/11/9.
 */
public class GamePauseDialog extends BaseDialog implements View.OnClickListener {

    private Button terminal = null;
    private Button resume = null;

    public GamePauseDialog(Context context) {
        super(context);
    }

    /**
     * 显示对话框。
     */
    public void show() {
        super.show();
        init();
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        mDialog.dismiss();
    }

    private void init() {
        mDialog.setContentView(R.layout.dlg_game_pause);
        Window window = mDialog.getWindow();

        terminal = (Button)window.findViewById(R.id.terminate);
        resume = (Button)window.findViewById(R.id.resume);

        terminal.setOnClickListener(this);
        resume.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) return;
        switch (v.getId()) {
            case R.id.terminate:
                mListener.onDialogButtonClick(OnGamePauseDialogInterface.TERMINATE);
                break;
            case R.id.resume:
                mListener.onDialogButtonClick(OnGamePauseDialogInterface.RESUME);
                break;
        }
    }

    interface OnGamePauseDialogInterface {
        /**
         * 用户选择终止游戏
         */
        int TERMINATE = 2;
        /**
         * 用户选择继续游戏
         */
        int RESUME = 3;

        void onDialogButtonClick(int userChoose);
    }
}
