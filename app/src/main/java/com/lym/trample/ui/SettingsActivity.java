package com.lym.trample.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lym.trample.App;
import com.lym.trample.BuildConfig;
import com.lym.trample.R;
import com.lym.trample.ScoresManager;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.bean.Feedback;

/**
 * Created by mao on 2015/11/5.
 */
public class SettingsActivity extends BaseActivity {

    private final static String TAG = "SettingsActivity";

    private Button app_setting_feedback;

    private Button app_setting_about;

    private AlertDialog mFeedbackDialog;
    private EditText feedback_input;
    private Button feedback_cancel;
    private Button feedback_ok;

    private AlertDialog mAboutDialog;
    private TextView about_name;
    private TextView about_versioncode;
    private TextView about_team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_setting_activity);

        initView();

    }

    private void initView() {
        app_setting_feedback = (Button) findViewById(R.id.app_setting_feedback);
        app_setting_about = (Button) findViewById(R.id.app_setting_about);

        initWriteFeedbackDialog();
        initAboutDialog();
        //意见反馈
        app_setting_feedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFeedbackDialog.show();
            }
        });
        //关于
        app_setting_about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mAboutDialog.show();
            }
        });
    }

    private void initWriteFeedbackDialog() {
        mFeedbackDialog = new AlertDialog.Builder(this).create();
        mFeedbackDialog.setCanceledOnTouchOutside(false);
        View v = LayoutInflater.from(this).inflate(R.layout.write_feedback_layout, null);
        mFeedbackDialog.setView(v);

        feedback_input = (EditText) v.findViewById(R.id.feedback_input);
        feedback_cancel = (Button) v.findViewById(R.id.feedback_cancel);
        feedback_ok = (Button) v.findViewById(R.id.feedback_ok);

        feedback_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFeedbackDialog.cancel();
            }
        });
        feedback_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = feedback_input.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    Feedback feedback = new Feedback();
                    feedback.setGuid(ScoresManager.guid);
                    feedback.setMessage(text);
                    feedback.save(SettingsActivity.this);
                    mFeedbackDialog.cancel();
                }
            }
        });
    }

    private void resetFeedback() {
        if(feedback_input != null) {
            feedback_input.setText("");
        }
    }

    private void initAboutDialog() {
        mAboutDialog = new AlertDialog.Builder(this).create();
        View v = LayoutInflater.from(this).inflate(R.layout.about_dialog_layout, null);
        mAboutDialog.setView(v);

        about_name = (TextView) v.findViewById(R.id.about_name);
        about_versioncode = (TextView) v.findViewById(R.id.about_versioncode);
        about_team = (TextView) v.findViewById(R.id.about_team);

        about_name.setText(getResources().getString(R.string.app_name));
        about_versioncode.setText(BuildConfig.VERSION_NAME);
        about_team.setText(getResources().getString(R.string.app_team));
    }
}
