package com.lym.trample.digit.generator;

import java.util.Random;

public abstract class BaseDigitGenerator implements IDigitGenerator{

    private int defaultMaxValue = 4;

    private Random random;

    public BaseDigitGenerator() {
        random = new Random();
    }

    public DigitMapEntry generate() {
        return generate(defaultMaxValue);
    }

    @Override
    public DigitMapEntry generate(int maxValue) {
        if(maxValue <= 0) {
            return null;
        }
        int digit = random.nextInt(maxValue) + 1;
        DigitMapEntry entry = new DigitMapEntry();
        entry.setNum(digit);
        return entry;
    }

    public void setDefaultMaxValue(int maxValue) {
        if(maxValue > 0) {
            defaultMaxValue = maxValue;
        }
    }

    public int getDefaultMaxValue() {
        return defaultMaxValue;
    }
}
