package com.lym.trample.word.generator;

import java.util.List;
import java.util.Random;

/**
 * Created by mao on 2015/11/11.
 *
 * 基本的单词生成器,该类默认为随机生成
 *
 * @author 麦灿标
 */
public abstract class BaseWordGenerator implements IWordGenerator {

    /** 某一单词库 */
    protected List<String> mWordsList;

    private Random random = new Random();

    /**
     * 构造函数
     *
     * @param wordsList 与该生成器相关联的单词集合
     * */
    public BaseWordGenerator(List<String> wordsList) {
        mWordsList = wordsList;
    }

    @Override
    public String generate() {
        return generate(mWordsList);
    }

    @Override
    public String generate(List<String> primaryList) {
        if(primaryList == null || primaryList.size() <= 0) {
            return null;
        }
        int size = primaryList.size();
        int index = random.nextInt(size);
        return primaryList.get(index);
    }

    /**
     * 设置单词集合
     *
     * @param 指定的单词集合
     * */
    public void setWordsList(List<String> wordsList) {
        mWordsList = wordsList;
    }

    /**
     * 获取单词集合
     *
     * @return 返回单词集合
     * */
    public List<String> getWordsList() {
        return mWordsList;
    }
}
