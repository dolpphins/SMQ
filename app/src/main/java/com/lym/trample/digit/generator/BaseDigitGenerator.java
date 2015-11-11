package com.lym.trample.digit.generator;

import java.util.Random;

/**
 * Created by mao on 2015/11/10.
 *
 * 基本数字生成器，该类默认为随机生成
 *
 * @author 麦灿标
 */
public abstract class BaseDigitGenerator implements IDigitGenerator{

    /** 默认生成的数字最大值 */
    private int defaultMaxValue = 4;

    private Random random;

    public BaseDigitGenerator() {
        random = new Random();
    }

    /**
     * 生成一个DigitMapEntry，其最大值你可以通过{@link #getDefaultMaxValue()}
     * 查看，也可以通过{@link #setDefaultMaxValue(int)}设置
     *
     * @return 生成成功返回相应的DigitMapEntry对象，失败返回null.
     * */
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

    /**
     * 设置默认生成的数字最大值
     *
     * @param maxValue 指定的最大值，注意必须大于0
     * */
    public void setDefaultMaxValue(int maxValue) {
        if(maxValue > 0) {
            defaultMaxValue = maxValue;
        }
    }

    /**
     * 获取默认生成的数字最大值
     *
     * @return 生成的数字最大值
     * */
    public int getDefaultMaxValue() {
        return defaultMaxValue;
    }
}
