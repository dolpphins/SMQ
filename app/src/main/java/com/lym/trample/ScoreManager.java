package com.lym.trample;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yao on 2015/11/11.
 */
public class ScoreManager {

    /**初始化后保存踩颜色的历史最高分数和排名的map的key，
     * 通过这个key可以拿到踩颜色的ScoreEntry*/
    public static String defaultColorKey = "color";

    /**初始化后保存踩水果的历史最高分数和排名的map的key，
     * 通过这个key可以拿到踩水果的ScoreEntry*/
    public static String defaultDigitKey = "fruit";

    /**初始化后保存踩单词的历史最高分数和排名的map的key，
     * 通过这个key可以拿到踩单词的ScoreEntry*/
    public static String defaultWordKey = "word";

    private static ScoreManager mScoresManeger = new ScoreManager();

    private static Map<String,ScoreEntry> colors = new HashMap<String, ScoreEntry>();
    private static Map<String,ScoreEntry> digits = new HashMap<String, ScoreEntry>();
    private static Map<String,ScoreEntry> words = new HashMap<String, ScoreEntry>();


    public ScoreManager(){}

    /**
     * <p>
     * 	获取ScoresManeger唯一实例对象
     * </p>
     *
     * @return 返回ScoresManeger唯一实例对象
     * */
    public static ScoreManager getInstance()
    {
        return mScoresManeger;
    }

    /**
     * 拿到保存踩颜色历史最高分数和排名的键值对
     * 通过ScoreManager.defaultColorKey拿到值
     * @return
     */
    public Map<String, ScoreEntry> getColors()
    {
        return colors;
    }

    /**
     * 设置color模式的数据
     * @param score 踩颜色模式的历史最高分
     * @param ranking 踩颜色模式的历史最高排名
     * @auther yao
     */
    public void setColors(int score,int ranking) {
        Map<String, ScoreEntry> color = new HashMap<String, ScoreEntry>();
        ScoreEntry se = new ScoreEntry();
        se.setRanking(ranking);
        se.setScore(score);
        color.put(defaultColorKey,se);
        ScoreManager.colors = color;
    }
    /**
     * 拿到保存踩数字历史最高分数和排名的键值对
     * 通过ScoreManager.defaultFruitKey拿到值
     * @return
     */
    public Map<String, ScoreEntry> getDigits()
    {
        return digits;
    }
    /**
     * 设置color模式的数据
     * @param score 踩颜色模式的历史最高分
     * @param ranking 踩颜色模式的历史最高排名
     * @auther yao
     */
    public void setDigits(int score,int ranking) {
        Map<String, ScoreEntry> fruit = new HashMap<String, ScoreEntry>();
        ScoreEntry se = new ScoreEntry();
        se.setRanking(ranking);
        se.setScore(score);
        fruit.put(defaultDigitKey,se);
        ScoreManager.digits = fruit;
    }
    /**
     * 拿到保存踩单词历史最高分数和排名的键值对
     * 通过ScoreManager.defaultWordKey拿到值
     * @return
     */
    public Map<String, ScoreEntry> getWords()
    {
        return words;
    }
    /**
     * 设置color模式的数据
     * @param score 踩颜色模式的历史最高分
     * @param ranking 踩颜色模式的历史最高排名
     * @auther yao
     */
    public void setWords(int score,int ranking) {
        Map<String, ScoreEntry> word = new HashMap<String, ScoreEntry>();
        ScoreEntry se = new ScoreEntry();
        se.setRanking(ranking);
        se.setScore(score);
        word.put(defaultWordKey, se);
        ScoreManager.words = word;
    }

    /**
     * 内部类，用来放置历史最高分数和历史最高排名
     */
    public class ScoreEntry
    {
        /**历史最高分数*/
        int score;
        /**历史最高排名*/
        int ranking;

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }
    }
}
