package com.max.algs.regengine.char_class;

public enum DChar implements CharClass {

    INST;

    @Override
    public boolean matched(char ch) {
        return Character.isDigit(ch);
    }

    @Override
    public String toString() {
        return "\\d";
    }

}
