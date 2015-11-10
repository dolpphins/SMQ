package com.lym.trample.digit.generator;

/**
 * Created by mao on 2015/11/10.
 *
 * 数字生成器接口
 *
 * @author 麦灿标
 */
public interface IDigitGenerator {

    /**
     * 生成一个DigitMapEntry，其中数字范围为0~maxValue.
     *
     * @param maxValue 生成数字的最大值，注意必须大于0.
     *
     * @return 生成成功返回相应的DigitMapEntry对象，失败(包括maxValue小于等于0)返回null.
     * */
    DigitMapEntry generate(int maxValue);

    class DigitMapEntry {

        /** 当前数字 */
        private int num;

        /** 背景颜色，十六进制表示形式 */
        private String value;

        /** 表示是否绘制数字,默认为true */
        private boolean drawDigitFlag = true;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isDrawDigitFlag() {
            return drawDigitFlag;
        }

        public void setDrawDigitFlag(boolean drawDigitFlag) {
            this.drawDigitFlag = drawDigitFlag;
        }
    }
}
