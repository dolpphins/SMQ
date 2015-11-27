package com.lym.stamp.color.generator.impl;

import com.lym.stamp.color.generator.BaseColorGenerator;

import java.util.Map;
import java.util.Random;

public class AverageColorGenerator extends BaseColorGenerator {

    private float factor = 2;

    private int mCriticalValue = 4;

    public AverageColorGenerator(Map<String, Integer> colorsMap) {
        super(colorsMap);
    }

    @Override
    public ColorMapEntry generate() {
        ColorMapEntry entry = super.generate();
        if(entry == null) {
            return null;
        }

        if(!entry.isSame()) {
            if(mMatchCount <= 0 && mTotalCount > getRandomInt(mCriticalValue)
                    || mMatchCount > 0 && mTotalCount > factor * mMatchCount) {
                entry = trimToSame(entry);
            }
        }
        if(entry == null) {
            return null;
        }
        Integer value = entry.getValue();
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

    public void setFactor(float factor) {
        if(factor >= 0) {
            this.factor = factor;
        }
    }

    public float getFactor() {
        return factor;
    }

    private int getRandomInt(int maxValue) {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }
}
