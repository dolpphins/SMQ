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

/**
 * Created by mao on 2015/11/24.
 *
 * 网络数据管理类
 */
public class NetworkDataManager {

    private final static String TAG = "NetworkDataManager";

    private Context mContext;

    public NetworkDataManager(Context context) {
        mContext = context;
    }

    /**
     * 初始化总体网络数据，注意该方法使异步的.
     *
     */
    public void requestUpdateData() {
        //当context为null或者网络不可用都会返回false
        if(!NetworkHelper.isAvailable(mContext)) {
            return;
        }
        queryInitDataFromNetwork(mContext);
    }

    private void queryInitDataFromNetwork(Context context) {
        BmobQuery<TUser> query = new BmobQuery<TUser>();
        //总人数
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
        query.max(new String[]{"best_color_score"});
        query.groupby(new String[]{"createdAt"});
        query.findStatistics(context, TUser.class, new FindStatisticsListener() {

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
        query.max(new String[]{"best_digit_score"});
        query.findStatistics(context, TUser.class, new FindStatisticsListener() {

            @Override
            public void onSuccess(Object result) {
                JSONArray ary = (JSONArray) result;
                if (ary != null) {
                    try {
                        JSONObject obj = ary.getJSONObject(0);
                        Log.i(TAG, obj.toString());
                        int maxscore = obj.getInt("_maxBest_digit_score");
                        if (maxscore >= 0) {
                            ScoresManager.bestDigitScore = maxscore;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
            }
        });
        query.max(new String[]{"best_line_score"});
        query.findStatistics(context, TUser.class, new FindStatisticsListener() {

            @Override
            public void onSuccess(Object result) {
                JSONArray ary = (JSONArray) result;
                if (ary != null) {
                    try {
                        JSONObject obj = ary.getJSONObject(0);
                        Log.i(TAG, obj.toString());
                        int maxscore = obj.getInt("_maxBest_line_score");
                        if (maxscore >= 0) {
                            ScoresManager.bestLineScore = maxscore;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
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
//        //历史排名
//        //颜色
//        BmobQuery<TUser> personalColorRankQuery = new BmobQuery<TUser>();
//        personalColorRankQuery.addWhereGreaterThan("best_color_score", ScoresManager.bestUserColorScore);
//        personalColorRankQuery.count(mContext, TUser.class, new CountListener() {
//
//            @Override
//            public void onSuccess(int i) {
//                if (i > 0) {
//                    ScoresManager.bestUserColorRank = i;
//                }
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//            }
//        });
//        //数字
//        BmobQuery<TUser> personalDigitRankQuery = new BmobQuery<TUser>();
//        personalDigitRankQuery.addWhereGreaterThan("best_digit_score", ScoresManager.bestUserDigitScore);
//        personalDigitRankQuery.count(mContext, TUser.class, new CountListener() {
//
//            @Override
//            public void onSuccess(int i) {
//                if(i > 0) {
//                    ScoresManager.bestUserDigitRank = i;
//                }
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//            }
//        });
//        //边线
//        BmobQuery<TUser> personalLineRankQuery = new BmobQuery<TUser>();
//        personalLineRankQuery.addWhereGreaterThan("best_line_score", ScoresManager.bestUserLineScore);
//        personalLineRankQuery.count(mContext, TUser.class, new CountListener() {
//
//            @Override
//            public void onSuccess(int i) {
//                if(i > 0) {
//                    ScoresManager.bestUserLineRank = i;
//                }
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//            }
//        });
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
