package com.lym.trample.base;

import android.app.AlertDialog;
import android.content.Context;

import com.lym.trample.R;

/**
 * Created by 卢沛东 on 2015/11/10.
 */
public class BaseDialog {

    protected AlertDialog mDialog = null;
    protected OnCustomDialogListener mListener = null;

    public BaseDialog(Context context) {
        mDialog = new AlertDialog.Builder(context, R.style.customDialog).create();
    }

    /**
     * 显示对话框
     */
    public void show() {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        mDialog.dismiss();
    }

    /**
     * 设置OnCustomDialogListener监听器
     * @param l 实现OnCustomDialogListener接口的监听器
     */
     public void setOnCustomDialogListener(OnCustomDialogListener l) {
            mListener = l;
     }

    public interface OnCustomDialogListener {
        void onDialogButtonClick(int userChoose);
        /**
         * 在GameReadyDialog中，用户选择了开始游戏
         */
        int GAME_READY_DIALOG_START_GAME = 0;
        /**
         * 在GameReadyDialog中，用户选择返回
         */
        int GAME_READY_DIALOG_GO_BACK = 1;
        /**
         * 在GameOverDialog中，用户选择再玩一局
         */
        int GAME_OVER_DIALOG_PLAY_AGAIN = 2;
        /**
         * 在GameOverDialog中，用户选择返回主界面
         */
        int GAME_OVER_DIALOG_GO_TO_MAIN_ACTIVITY = 3;
        /**
         * 在GamePauseDialog中，用户选择终止游戏
         */
        int GAME_PAUSE_DIALOG_TERMINATE = 4;
        /**
         * 在GamePauseDialog中，用户选择继续游戏
         */
        int GAME_PAUSE_DIALOG_RESUME = 5;
    }
}
