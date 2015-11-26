package com.lym.trample.conf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 2015/11/22.
 *
 * @author 麦灿标
 */
public class IdiomsKeeper {

    private final static List<String> sIdiomsList = new ArrayList<String>();

    static {

        sIdiomsList.add("坐井观天");
        sIdiomsList.add("水滴石穿");
        sIdiomsList.add("差强人意");
        sIdiomsList.add("过犹不及");
        sIdiomsList.add("龙马精神");
        sIdiomsList.add("草长莺飞");
        sIdiomsList.add("光阴荏苒");
        sIdiomsList.add("刻舟求剑");
        sIdiomsList.add("天壤之别");
        sIdiomsList.add("不毛之地");
        sIdiomsList.add("开天辟地");
        sIdiomsList.add("出生入死");
        sIdiomsList.add("人山人海");
        sIdiomsList.add("一望无际");

    }

    public static List<String> getIdiomsList() {
        return sIdiomsList;
    }
}
