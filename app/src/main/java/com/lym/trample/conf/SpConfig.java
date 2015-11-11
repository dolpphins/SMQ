package com.lym.trample.conf;

/**
 * Created by yao on 2015/11/11.
 */
public class SpConfig {

    private static SpConfig mSpConfig = new SpConfig();

    public SpConfig(){

    }

    public static SpConfig getInstance(){
        return mSpConfig;
    }

    /**踩颜色中历史最高分*/
    public String colorScore = "ColorsScore";

    /**踩颜色历史最高排名*/
    public String colorRanking = "ColorsRanking";

    /**踩数字中历史最高分*/
    public String digitScore = "DigitsScore";

    /**踩数字历史最高排名*/
    public String digitRanking = "DigitsRanking";

    /**踩单词中历史最高分*/
    public String wordScore = "WordsScore";

    /**踩单词历史最高排名*/
    public String wordRanking = "WordsRanking";
}


