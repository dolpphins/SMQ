package com.lym.trample.idiom;

/**
 * Created by mao on 2015/11/22.
 *
 * @author 麦灿标
 */
public interface SideGenerator {


    class SideMapEntry {

        /**
         * 标记是否被点击过了
         */
        private boolean alreadyTouch;

        public boolean isAlreadyTouch() {
            return alreadyTouch;
        }

        public void setAlreadyTouch(boolean alreadyTouch) {
            this.alreadyTouch = alreadyTouch;
        }
    }
}
