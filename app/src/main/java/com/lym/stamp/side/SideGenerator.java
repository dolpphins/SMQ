package com.lym.stamp.side;

public interface SideGenerator {


    class SideMapEntry {


        private boolean alreadyTouch;

        public boolean isAlreadyTouch() {
            return alreadyTouch;
        }

        public void setAlreadyTouch(boolean alreadyTouch) {
            this.alreadyTouch = alreadyTouch;
        }
    }
}
