package com.lym.trample.color.generator;

/**
 * Created by mao on 2015/11/9.
 *
 * 颜色生成器接口
 *
 * @author 麦灿标
 */
public interface IColorGenerator {

    /**
     * 生成一个ColorMapEntry
     *
     * @return 生成成功返回一个ColorMapEntry对象，失败返回null.
     * */
    ColorMapEntry generate();



    /**
     * 颜色map entry
     * */
    class ColorMapEntry {

        private String text;

        private String value;

        private boolean same;

        private boolean alreadyTouch;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isSame() {
            return same;
        }

        public void setSame(boolean same) {
            this.same = same;
        }

        public boolean isAlreadyTouch() {
            return alreadyTouch;
        }

        public void setAlreadyTouch(boolean alreadyTouch) {
            this.alreadyTouch = alreadyTouch;
        }
    }
}
