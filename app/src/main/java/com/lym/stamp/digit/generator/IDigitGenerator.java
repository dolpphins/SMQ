package com.lym.stamp.digit.generator;

public interface IDigitGenerator {

    DigitMapEntry generate(int maxValue);

    class DigitMapEntry {

        private int num;

        private String value;

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
