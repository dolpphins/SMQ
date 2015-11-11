package com.lym.trample.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.lym.trample.R;
import com.lym.trample.ScoreManager;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.base.BaseDialog;
import com.lym.trample.conf.SpConfig;
import com.lym.trample.dialog.GameReadyDialog;
import com.lym.trample.utils.SharePreferencesManager;


public class MainActivity extends BaseActivity implements OnClickListener,BaseDialog.OnCustomDialogListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main_menu_activity);
        initData();
        initView();
    }

    public void initData() {
        int cScore = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().colorScore, -1);
        int cRanking = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().colorRanking,-1);
        if(cScore!=-1&&cRanking!=-1)
            ScoreManager.getInstance().setColors(cScore,cRanking);
        else
            ScoreManager.getInstance().setColors(0,0);
        int fScore = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().digitScore,-1);
        int fRanking = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().digitRanking,-1);
        if(fScore!=-1&&fRanking!=-1)
            ScoreManager.getInstance().setColors(fScore,fRanking);
        else
            ScoreManager.getInstance().setColors(0,0);
        int wScore = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().wordScore,-1);
        int wRanking = SharePreferencesManager.getInstance().getInt(this, SpConfig.getInstance().wordRanking,-1);
        if(wScore!=-1&&wRanking!=-1)
            ScoreManager.getInstance().setColors(wScore,wRanking);
        else
            ScoreManager.getInstance().setColors(0,0);


        isClickColor = false;
        isClickDigit = false;
        isClickWord = false;


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
                this.finish();
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
                    Intent gameWordsIntent = new Intent(MainActivity.this, WordsActivity.class);
                    startActivity(gameWordsIntent);
                }
                mGameReadyDialog.dismiss();
                break;
        }
    }
}
