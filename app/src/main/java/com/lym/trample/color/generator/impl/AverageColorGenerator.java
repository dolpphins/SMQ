package com.lym.trample.color.generator.impl;

import com.lym.trample.color.generator.BaseColorGenerator;

import java.sql.ParameterMetaData;
import java.util.Map;
import java.util.Random;

/**
 * Created by mao on 2015/11/9.
 *
 * 均匀的颜色生成器,该生成器会自动控制匹配率
 *
 * @author 麦灿标
 */
public class AverageColorGenerator extends BaseColorGenerator {

    /** 颜色块总数/匹配数 因子 */
    private float factor = 2;

    private int mCriticalValue = 4;

    public AverageColorGenerator(Map<String, String> colorsMap) {
        super(colorsMap);
    }

    @Override
    public ColorMapEntry generate() {
        ColorMapEntry entry = super.generate();
        if(entry == null) {
            return null;
        }

        //进行纠正
        if(!entry.isSame()) {
            if(mMatchCount <= 0 && mTotalCount > getRandomInt(mCriticalValue)
                    || mMatchCount > 0 && mTotalCount > factor * mMatchCount) {
                entry = trimToSame(entry);
            }
        }
        if(entry == null) {
            return null;
        }
        String value = entry.getValue();
        mTotalCount++;
        int newValueCount = mValueCountMap.get(value) + 1;
        mValueCountMap.put(value, newValueCount);
        if(entry.isSame()) {
            mMatchCount++;
            int newMatchCount = mMatchCountMap.get(entry.getValue());
            mMatchCountMap.put(value, newMatchCount);
        }
        return entry;
    }

    /**
     * 设置颜色块匹配率，0表示在开始一小段时间后所有颜色块都匹配，该值越高表示匹配率越低.
     *
     * @param factor 匹配率，必须为非负数.
     * */
    public void setFactor(float factor) {
        if(factor >= 0) {
            this.factor = factor;
        }
    }

    /**
     * 获取颜色块匹配率
     *
     * @return 返回颜色快匹配率
     * */
    public float getFactor() {
        return factor;
    }

    //产生0~maxValue(包括maxValue)的int类型随机数
    private int getRandomInt(int maxValue) {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }
}
