package com.lym.trample;

/**
 * Created by mao on 2015/11/24.
 */
public class ScoresManager {

    /** 总用户数 */
    public static int totalUserCount;

    /** 踩颜色最高分 */
    public static int bestColorScore;

    /** 踩数字最高分 */
    public static int bestDigitScore;

    /** 踩边线最高分 */
    public static int bestLineScore;


    /***
     * 个人相关
     */

    public static String guid;

    /** 我的踩颜色历史最高分 */
    public static int bestUserColorScore;

//    /** 我的踩数字历史最高排名 */
//    public static int bestUserColorRank;

    /** 我的踩数字历史最高分 */
    public static int bestUserDigitScore;

//    /** 我的踩颜色历史最高排名 */
//    public static int bestUserDigitRank;

    /** 我的踩边线历史最高分 */
    public static int bestUserLineScore;

//    /** 我的踩边线历史最高排名 */
//    public static int bestUserLineRank;
}
