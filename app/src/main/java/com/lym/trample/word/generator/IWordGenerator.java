package com.lym.trample.word.generator;

import java.util.List;

/**
 * Created by mao on 2015/11/11.
 *
 * 单词生成器
 *
 * @author 麦灿标
 */
public interface IWordGenerator {

    /**
     * 按一定规则生成一个单词，该单词位于默认单词库中
     *
     * @return 生成成功返回一个单词，失败返回null.
     * */
    String generate();

    /**
     * 按一定规则生成一个单词，该单词位于指定的单词库中
     *
     * @param primaryList 指定的单词库
     *
     * @return 生成成功返回一个单词，失败返回null.
     * */
    String generate(List<String> primaryList);


    class WordMapEntry {

        /** 相关联单词 */
        private String word;

        /** 当前字母 */
        private String letter;

        private boolean isPressed;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getLetter() {
            return letter;
        }

        public void setLetter(String letter) {
            this.letter = letter;
        }

        public boolean isPressed() {
            return isPressed;
        }

        public void setIsPressed(boolean isPressed) {
            this.isPressed = isPressed;
        }
    }
}
