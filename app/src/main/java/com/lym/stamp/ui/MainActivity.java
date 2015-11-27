package com.lym.stamp.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lym.stamp.App;
import com.lym.stamp.R;
import com.lym.stamp.ScoresManager;
import com.lym.stamp.base.BaseActivity;
import com.lym.stamp.base.BaseDialog;
import com.lym.stamp.crash.CrashHandler;
import com.lym.stamp.dialog.GameReadyDialog;


public class MainActivity extends BaseActivity implements OnClickListener,BaseDialog.OnCustomDialogListener {

    private ImageView how_to_play;
    private ImageView setting;
    private ImageView exit;

    private Button gameWord;
    private Button gameColor;
    private Button gameDigit;

    private Boolean isClickColor;
    private Boolean isClickWord;
    private Boolean isClickDigit;


    private GameReadyDialog mGameReadyDialog;

    private AlertDialog mUpdateDialog;
    private TextView app_update_ignore;
    private TextView app_update_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_menu_activity);

        isClickColor = false;
        isClickDigit = false;
        isClickWord = false;

        initView();

        CrashHandler.getInstance().registerUncaughtExceptionHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(App.sCanUpdate && App.sUpdateItem != null) {
            showUpdateDialog();
        }
    }

    public void initView() {

        how_to_play = (ImageView) findViewById(R.id.menu_iv_how_to_play);
        setting = (ImageView) findViewById(R.id.menu_iv_setting);
        exit = (ImageView) findViewById(R.id.menu_iv_exit);

        how_to_play.setOnClickListener(this);
        setting.setOnClickListener(this);
        exit.setOnClickListener(this);

        gameWord = (Button) findViewById(R.id.menu_bt_words);
        gameDigit = (Button) findViewById(R.id.menu_bt_digits);
        gameColor = (Button) findViewById(R.id.menu_bt_colors);

        gameColor.setOnClickListener(this);
        gameDigit.setOnClickListener(this);
        gameWord.setOnClickListener(this);

        mGameReadyDialog = new GameReadyDialog(this);
        mGameReadyDialog.setOnCustomDialogListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_iv_how_to_play:
                Intent intentHelp = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intentHelp);
                break;
            case R.id.menu_iv_setting:
                Intent intentSetting = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.menu_iv_exit:
                confirmExit();
                break;
            case R.id.menu_bt_colors:
                mGameReadyDialog.setMyHighestScore(ScoresManager.bestUserColorScore);
                mGameReadyDialog.setGlobalHighestScore(ScoresManager.bestColorScore);
                mGameReadyDialog.show(ScoresManager.bestColorScoreSuccess);
                isClickColor = true;
                break;
            case R.id.menu_bt_digits:
                mGameReadyDialog.setMyHighestScore(ScoresManager.bestUserDigitScore);
                mGameReadyDialog.setGlobalHighestScore(ScoresManager.bestDigitScore);
                mGameReadyDialog.show(ScoresManager.bestDigitScoreSuccess);
                isClickDigit = true;
                break;
            case R.id.menu_bt_words:
                mGameReadyDialog.setMyHighestScore(ScoresManager.bestUserLineScore);
                mGameReadyDialog.setGlobalHighestScore(ScoresManager.bestLineScore);
                mGameReadyDialog.show(ScoresManager.bestLineScoreSuccess);
                isClickWord = true;
                break;


        }
    }

    @Override
    public void onDialogButtonClick(int userChoose) {
        switch (userChoose){
            case BaseDialog.OnCustomDialogListener.GAME_READY_DIALOG_GO_BACK:
                mGameReadyDialog.dismiss();
                isClickColor = false;
                isClickDigit = false;
                isClickWord = false;
                break;
            case BaseDialog.OnCustomDialogListener.GAME_READY_DIALOG_START_GAME:
                if(isClickColor)
                {
                    isClickColor = false;
                    Intent gameColorsIntent = new Intent(MainActivity.this, ColorsActivity.class);
                    startActivity(gameColorsIntent);
                }
                if(isClickDigit)
                {
                    isClickDigit = false;
                    Intent gameDigitsIntent = new Intent(MainActivity.this, DigitsActivity.class);
                    startActivity(gameDigitsIntent);
                }
                if(isClickWord){
                    isClickWord = false;
                    Intent gameWordsIntent = new Intent(MainActivity.this, SideActivity.class);
                    startActivity(gameWordsIntent);
                }
                mGameReadyDialog.dismiss();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            confirmExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_exit_tip));
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.ok), new AlertDialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        builder.show();
    }

    private void showUpdateDialog() {
        if(mUpdateDialog == null) {
            mUpdateDialog = new AlertDialog.Builder(this).create();
            View v = LayoutInflater.from(this).inflate(R.layout.update_dialog_layout, null);
            mUpdateDialog.setView(v);

            app_update_ignore = (TextView) v.findViewById(R.id.app_update_ignore);
            app_update_ok = (TextView) v.findViewById(R.id.app_update_ok);
            app_update_ignore.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mUpdateDialog.cancel();
                    App.sCanUpdate = false;
                    App.sUpdateItem = null;
                }
            });
            app_update_ok.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(App.sUpdateItem.getUpdateLink()));
                    startActivity(intent);
                }
            });
        }

        if(App.sCanUpdate && App.sUpdateItem != null) {
            mUpdateDialog.show();
        }
    }
}
