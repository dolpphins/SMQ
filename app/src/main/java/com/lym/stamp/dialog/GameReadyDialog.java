package com.lym.stamp.dialog;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lym.stamp.R;
import com.lym.stamp.ScoresManager;
import com.lym.stamp.base.BaseDialog;

public class GameReadyDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;

    private TextView global_highest_score = null;
    private TextView my_highest_score = null;
    private Button start_game = null;
    private Button go_back = null;

    private int mGlobalHighestScore = 0;
    private int mMyHighestScore = 0;

    public GameReadyDialog(Context context) {
        super(context);
        mContext = context;
    }

    public void show(ScoresManager.Status status) {
        super.show();
        init(status);
    }

    private void init(ScoresManager.Status status) {
        mDialog.setContentView(R.layout.dlg_game_ready);
        Window window = mDialog.getWindow();

        global_highest_score = (TextView) window.findViewById(R.id.global_highest_score);
        my_highest_score = (TextView) window.findViewById(R.id.my_highest_score);
        start_game = (Button) window.findViewById(R.id.start_game);
        go_back = (Button) window.findViewById(R.id.go_back);

        if(status == ScoresManager.Status.SUCCESS) {
            global_highest_score.setText(mGlobalHighestScore + "");
        } else if(status == ScoresManager.Status.FAIL) {
            global_highest_score.setText(mContext.getResources().getString(R.string.game_dialog_score_fail_tip));
        } else if(status == ScoresManager.Status.GETTING) {
            global_highest_score.setText(mContext.getResources().getString(R.string.game_dialog_score_getting_tip));
        }

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
