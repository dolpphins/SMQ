package com.lym.trample;

import android.app.DownloadManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.lym.trample.bean.TUser;
import com.lym.trample.utils.NetworkHelper;
import com.lym.trample.utils.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.UserDataHandler;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.SaveListener;

public class NetworkDataManager {

    private Context mContext;

    public NetworkDataManager(Context context) {
        mContext = context;
    }

    public void requestUpdateData() {
        if(!NetworkHelper.isAvailable(mContext)) {
            return;
        }
        queryInitDataFromNetwork(mContext);
    }

    private void queryInitDataFromNetwork(Context context) {
        BmobQuery<TUser> query = new BmobQuery<TUser>();
        query.count(context, TUser.class, new CountListener() {

            @Override
            public void onSuccess(int i) {
                if (i >= 0) {
                    ScoresManager.totalUserCount = i;
                }
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
        //
        BmobQuery<TUser> colorQuery = new BmobQuery<TUser>();
        colorQuery.max(new String[]{"best_color_score"});
        ScoresManager.bestColorScoreSuccess = ScoresManager.Status.GETTING;
        colorQuery.findStatistics(context, TUser.class, new FindStatisticsListener() {

            @Override
            public void onSuccess(Object result) {
                JSONArray ary = (JSONArray) result;
                if (ary != null) {
                    try {
                        JSONObject obj = ary.getJSONObject(0);
                        int maxscore = obj.getInt("_maxBest_color_score");
                        if (maxscore >= 0) {
                            ScoresManager.bestColorScore = maxscore;
                        }
                        ScoresManager.bestColorScoreSuccess = ScoresManager.Status.SUCCESS;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ScoresManager.bestColorScoreSuccess = ScoresManager.Status.FAIL;
            }
        });
        BmobQuery<TUser> digitQuery = new BmobQuery<TUser>();
        digitQuery.max(new String[]{"best_digit_score"});
        ScoresManager.bestDigitScoreSuccess = ScoresManager.Status.GETTING;
        digitQuery.findStatistics(context, TUser.class, new FindStatisticsListener() {

            @Override
            public void onSuccess(Object result) {
                JSONArray ary = (JSONArray) result;
                if (ary != null) {
                    try {
                        JSONObject obj = ary.getJSONObject(0);
                        int maxscore = obj.getInt("_maxBest_digit_score");
                        if (maxscore >= 0) {
                            ScoresManager.bestDigitScore = maxscore;
                        }
                        ScoresManager.bestDigitScoreSuccess = ScoresManager.Status.SUCCESS;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ScoresManager.bestDigitScoreSuccess = ScoresManager.Status.FAIL;
            }
        });
        BmobQuery<TUser> lineQuery = new BmobQuery<TUser>();
        lineQuery.max(new String[]{"best_line_score"});
        ScoresManager.bestLineScoreSuccess = ScoresManager.Status.GETTING;
        lineQuery.findStatistics(context, TUser.class, new FindStatisticsListener() {

            @Override
            public void onSuccess(Object result) {
                JSONArray ary = (JSONArray) result;
                if (ary != null) {
                    try {
                        JSONObject obj = ary.getJSONObject(0);
                        int maxscore = obj.getInt("_maxBest_line_score");
                        if (maxscore >= 0) {
                            ScoresManager.bestLineScore = maxscore;
                        }
                        ScoresManager.bestLineScoreSuccess = ScoresManager.Status.SUCCESS;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ScoresManager.bestLineScoreSuccess = ScoresManager.Status.FAIL;
            }
        });
        BmobQuery<TUser> personalScoreQuery = new BmobQuery<TUser>();
        personalScoreQuery.addWhereEqualTo("guid", ScoresManager.guid);
        personalScoreQuery.findObjects(mContext, new FindListener<TUser>() {

            @Override
            public void onSuccess(List<TUser> list) {
                if(list != null && list.size() > 0) {
                    TUser user = list.get(0);
                    ScoresManager.bestUserColorScore = user.getBest_color_score();
                    ScoresManager.bestUserDigitScore = user.getBest_digit_score();
                    ScoresManager.bestUserLineScore = user.getBest_line_score();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }


    public void insertOneUserToBmob(final TUser user) {
        if(user == null) {
            return;
        }
        BmobQuery<TUser> query = new BmobQuery<TUser>();
        query.addWhereEqualTo("guid", user.getGuid());
        query.findObjects(mContext, new FindListener<TUser>() {

            @Override
            public void onSuccess(List<TUser> list) {
                if(list != null && list.size() == 0) {
                    user.save(mContext);
                }
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    public void updateOneUserToBmob(final TUser user) {
        if(user == null) {
            return;
        }
        BmobQuery<TUser> query = new BmobQuery<TUser>();
        query.addWhereEqualTo("guid", user.getGuid());
        query.findObjects(mContext, new FindListener<TUser>() {

            @Override
            public void onSuccess(List<TUser> list) {
                if (list != null && list.size() == 1) {
                    TUser temp = list.get(0);
                    user.setObjectId(list.get(0).getObjectId());
                    user.update(mContext);
                }
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }
}
