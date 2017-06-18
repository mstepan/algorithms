package com.max.algs.regengine.char_class;

/**
 * match '\\w' regex character class , [0-9A-Za-z_]
 */
public enum WChar implements CharClass {

    INST;

    @Override
    public boolean matched(char ch) {
        return ch == '_' || Character.isAlphabetic(ch) || Character.isDigit(ch);
    }

    @Override
    public String toString() {
        return "\\w";
    }

}
