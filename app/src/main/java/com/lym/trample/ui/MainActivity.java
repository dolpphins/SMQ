package com.lym.trample.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lym.trample.App;
import com.lym.trample.R;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.base.BaseDialog;
import com.lym.trample.bean.UpdateBean;
import com.lym.trample.crash.CrashHandler;
import com.lym.trample.dialog.GameReadyDialog;


public class MainActivity extends BaseActivity implements OnClickListener,BaseDialog.OnCustomDialogListener {

    private final static String TAG = "MainActivity";

    /**
     * 怎么玩图标
     */
    private ImageView how_to_play;
    /**
     * 设置图标
     */
    private ImageView setting;
    /**
     * 退出游戏图标
     */
    private ImageView exit;

    private Button gameWord;
    private Button gameColor;
    private Button gameDigit;


    private int mCurrentLayoutState = 0;

    private static final int FLING_MIN_DISTANCE = 80;
    private static final int FLING_MIN_VELOCITY = 100;

    private Boolean isClickColor;
    private Boolean isClickWord;
    private Boolean isClickDigit;


    private GameReadyDialog mGameReadyDialog;

    //更新相关
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

        //异常处理器
        CrashHandler.getInstance().registerUncaughtExceptionHandler(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //判断是否有更新
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
                mGameReadyDialog.show();
                isClickColor = true;
                break;
            case R.id.menu_bt_digits:
                mGameReadyDialog.show();
                isClickDigit = true;
                break;
            case R.id.menu_bt_words:
                mGameReadyDialog.show();
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
        builder.setTitle("你真的要退出吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {

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

        //再次判断
        if(App.sCanUpdate && App.sUpdateItem != null) {
            mUpdateDialog.show();
        }
    }
}
