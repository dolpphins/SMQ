package com.lym.stamp.base;

import android.app.AlertDialog;
import android.content.Context;

import com.lym.stamp.R;

public class BaseDialog {

    protected AlertDialog mDialog = null;
    protected OnCustomDialogListener mListener = null;

    public BaseDialog(Context context) {
        mDialog = new AlertDialog.Builder(context, R.style.customDialog).create();
    }

    public void show() {
        mDialog.getWindow().setWindowAnimations(R.style.dialogWindowAnim);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public void setOnCustomDialogListener(OnCustomDialogListener l) {
            mListener = l;
     }

    public interface OnCustomDialogListener {
        void onDialogButtonClick(int userChoose);

        int GAME_READY_DIALOG_START_GAME = 0;

        int GAME_READY_DIALOG_GO_BACK = 1;

        int GAME_OVER_DIALOG_PLAY_AGAIN = 2;

        int GAME_OVER_DIALOG_GO_TO_MAIN_ACTIVITY = 3;

        int GAME_PAUSE_DIALOG_TERMINATE = 4;

        int GAME_PAUSE_DIALOG_RESUME = 5;
    }
}
