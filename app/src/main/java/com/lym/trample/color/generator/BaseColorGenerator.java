package com.lym.trample.color.generator;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by mao on 2015/11/9.
 *
 * 基本的颜色生成器
 *
 * @author 麦灿标
 *
 */
public abstract  class BaseColorGenerator implements IColorGenerator {

    private final static String TAG = "BaseColorGenerator";

    protected Map<String, String> mColorsMap;

    protected List<String> mColorsTextList;

    protected List<String> mColorsValueList;

    /** 记录每种颜色(背景色)出现的次数map，键：背景颜色值，值：出现的次数 */
    protected Map<String, Integer> mValueCountMap;

    /** 记录所有匹配颜色map，键：背景颜色值，值：匹配的次数 */
    protected Map<String, Integer> mMatchCountMap;

    /** 生成的颜色块总数 */
    protected int mTotalCount;

    /** 匹配的颜色块总数 */
    protected int mMatchCount;

    public BaseColorGenerator(Map<String, String> colorsMap) {
        mColorsMap = colorsMap;
        init();
    }

    private void init() {
        mColorsTextList = new ArrayList<String>();
        mColorsValueList = new ArrayList<String>();
        mValueCountMap = new HashMap<String, Integer>();
        mMatchCountMap = new HashMap<String, Integer>();
        if(mColorsMap != null) {
            Set<String> keys = mColorsMap.keySet();
            Collection<String> values = mColorsMap.values();
            mColorsTextList.addAll(keys);
            mColorsValueList.addAll(values);
            for(String s : values) {
                mValueCountMap.put(s, 0);
                mMatchCountMap.put(s, 0);
            }
        }
        mTotalCount = 0;
        mMatchCount = 0;
    }

    /**
     * 随机生成颜色entry
     *
     * @return
     * */
    @Override
    public ColorMapEntry generate() {
        if(mColorsTextList.size() <= 0 || mColorsValueList.size() <= 0) {
            return null;
        }
        Random random = new Random();
        ColorMapEntry entry = new ColorMapEntry();
        String text = getColorText(random.nextInt(Integer.MAX_VALUE));
        String value = getColorValue(random.nextInt(Integer.MAX_VALUE));
        entry.setText(text);
        entry.setValue(value);

        if (isColorSame(entry.getText(), entry.getValue())) {
            entry.setSame(true);
        } else {
            entry.setSame(false);
        }

        return entry;
    }

    /**
     * 获取指定索引的颜色文本,注意该方法会自动对index进行取余操作，以保证
     * 可以总能获取到某一颜色文本.
     *
     * @param index 指定的索引
     *
     * @return 返回相应的颜色文本
     * */
    public String getColorText(int index) {
        if(mColorsTextList.size() <= 0) {
            return null;
        }
        return mColorsTextList.get(index % mColorsTextList.size());
    }

    /**
     * 获取指定索引的颜色值(十六进制表示),注意该方法会自动对index进行取余操作，以保证
     * 可以总能获取到某一颜色值.
     *
     * @param index 指定的索引
     *
     * @return 返回相应的颜色值
     * */
    public String getColorValue(int index) {
        if(mColorsValueList.size() <= 0) {
            return null;
        }
        return mColorsValueList.get(index % mColorsValueList.size());
    }

    /**
     * 判断颜色文本与颜色值是否对应，注意该方法只对该类预定的颜色有效
     *
     * @param text 颜色文本
     * @param value 颜色值,十六进制形式表示
     *
     * @return 相同返回true，否则返回false.
     * */
    public boolean isColorSame(String text, String value) {
        if(TextUtils.isEmpty(text) || TextUtils.isEmpty(value)) {
            return false;
        }
        if(mColorsMap.containsKey(text)) {
            String v = mColorsMap.get(text);
            if(value.equals(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 调整entry让其颜色文本与背景颜色一致，注意该调整是以颜色文本为参考，
     * 即颜色文本不变，改变背景颜色值.
     *
     * @return entry 要调整的颜色entry
     *
     * @return 调整成功返回调整后的颜色entry，失败返回null.
     * */
    public ColorMapEntry trimToSame(ColorMapEntry entry) {
        if(entry == null) {
            return entry;
        }
        String text = entry.getText();
        if(mColorsMap.containsKey(text)) {
            entry.setValue(mColorsMap.get(text));
            entry.setSame(true);
            return entry;
        }
        return null;
    }
}
