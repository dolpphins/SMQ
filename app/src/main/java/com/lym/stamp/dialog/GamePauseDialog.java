package com.lym.stamp.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.lym.stamp.R;
import com.lym.stamp.base.BaseDialog;

public class GamePauseDialog extends BaseDialog implements View.OnClickListener {

    private Button terminal = null;
    private Button resume = null;

    public GamePauseDialog(Context context) {
        super(context);
    }

    @Override
    public void show() {
        super.show();
        init();
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
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_PAUSE_DIALOG_TERMINATE);
                break;
            case R.id.resume:
                mListener.onDialogButtonClick(OnCustomDialogListener.GAME_PAUSE_DIALOG_RESUME);
                break;
        }
    }
}
