package com.max.algs.regengine.char_class;

public enum UnconditionalCharClass implements CharClass {


    INST;

    @Override
    public boolean matched(char ch) {
        return true;
    }

    @Override
    public String toString() {
        return "?";
    }


}
