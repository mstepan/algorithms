package com.max.algs.string.matching;

import com.max.algs.string.ZboxUtils;

import java.util.ArrayList;
import java.util.List;


public enum ZboxStringMatcher implements IStringMatcher {

    INST;

    @Override
    public List<Integer> validShifts(String text, String pattern) {

        List<Integer> indexes = new ArrayList<>();

        String str = pattern + "$" + text;

        int[] zBoxes = ZboxUtils.calculateZboxes(str);

        int offset = pattern.length() + 1;

        for (int i = 0; i < zBoxes.length; i++) {
            if (zBoxes[i] == pattern.length()) {
                indexes.add(i - offset);
            }
        }

        return indexes;
    }


}
