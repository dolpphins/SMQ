package com.lym.trample.color.generator;

public interface IColorGenerator {

    ColorMapEntry generate();


    class ColorMapEntry {

        private String text;

        private Integer value;

        private boolean same;

        private boolean alreadyTouch;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
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
