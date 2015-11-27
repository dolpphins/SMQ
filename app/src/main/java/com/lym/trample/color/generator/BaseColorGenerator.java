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

public abstract  class BaseColorGenerator implements IColorGenerator {

    private final static String TAG = "BaseColorGenerator";

    protected Map<String, String> mColorsMap;

    protected List<String> mColorsTextList;

    protected List<String> mColorsValueList;

    protected Map<String, Integer> mValueCountMap;

    protected Map<String, Integer> mMatchCountMap;

    protected int mTotalCount;

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

    public String getColorText(int index) {
        if(mColorsTextList.size() <= 0) {
            return null;
        }
        return mColorsTextList.get(index % mColorsTextList.size());
    }

    public String getColorValue(int index) {
        if(mColorsValueList.size() <= 0) {
            return null;
        }
        return mColorsValueList.get(index % mColorsValueList.size());
    }

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
