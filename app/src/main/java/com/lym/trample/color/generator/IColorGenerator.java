package com.lym.trample.color.generator;

public interface IColorGenerator {

    ColorMapEntry generate();


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
