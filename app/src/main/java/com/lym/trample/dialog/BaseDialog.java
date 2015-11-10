package com.lym.trample.dialog;

import android.app.AlertDialog;
import android.content.Context;

import com.lym.trample.R;

/**
 * Created by 卢沛东 on 2015/11/10.
 */
public class BaseDialog {

    protected OnCustomDialogListener mListener = null;
    protected AlertDialog mDialog = null;

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
}
