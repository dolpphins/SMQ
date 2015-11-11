package com.lym.trample.conf;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by mao on 2015/11/11.
 *
 * 预定义的单词库
 *
 * @author 麦灿标
 */
public class WordsKeeper {

    /** 单词库，键：单词集合名，值：单词集合 */
    private final static Map<String, List<String>> sWordsLibrary = new HashMap<String, List<String>>();

    //测试使用
    static {
        List<String> list = new ArrayList<String>();
        list.add("apple");
        list.add("banana");
        list.add("cat");
        list.add("dog");
        list.add("hello");
        list.add("complete");
        list.add("cautious");
        list.add("expire");
        list.add("transient");
        list.add("spy");
        list.add("minimal");
        list.add("forthcoming");
        list.add("polar");
        list.add("muddy");
        list.add("lamb");
        list.add("ignorance");
        list.add("wretched");
        sWordsLibrary.put("animal", list);
    }

    /**
     * 获取预定义的单词库
     *
     * @return 返回预定义的单词库
     * */
    public static Map<String, List<String>> getWordsLibrary() {
        return sWordsLibrary;
    }

    /**
     * 获取预定义单词库的所有集合名
     *
     * @return 返回预定义单词库的所有集合名
     * */
    public static Set<String> getgetWordsLibraryNames() {
        return sWordsLibrary.keySet();
    }

    /**
     * 获取指定名字的单词集合
     *
     * @param 指定单词集合名
     *
     * @return 获取成功返回相应的单词集合，失败返回null.
     * */
    public static List<String> getWordsList(String name) {
        if(TextUtils.isEmpty(name)) {
            return null;
        }
        if(sWordsLibrary.containsKey(name)) {
            return sWordsLibrary.get(name);
        }
        return null;
    }
}
