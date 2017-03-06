package com.max.data_retrieval;


import java.util.HashSet;
import java.util.Set;

public enum LineNormaliser {

    INST;

    private final Set<Character> skipSet = new HashSet<>();

    {
        skipSet.add(',');
        skipSet.add(';');
        skipSet.add(':');
        skipSet.add('!');
        skipSet.add('?');
        skipSet.add('.');
        skipSet.add('\'');
        skipSet.add('\"');
        skipSet.add('(');
        skipSet.add(')');
        skipSet.add('[');
        skipSet.add(']');
        skipSet.add('-');
        skipSet.add('_');
        skipSet.add('/');
        skipSet.add('\\');
        skipSet.add('#');
        skipSet.add('$');
        skipSet.add('%');
        skipSet.add('&');
        skipSet.add('*');
        skipSet.add('”');
        skipSet.add('“');
        skipSet.add('’');
        skipSet.add('‘');
    }

    public String normalizeLine(String line) {

        char[] original = line.toCharArray();

        char[] res = new char[original.length];
        int length = 0;

        for (char ch : original) {

            if (skipSet.contains(ch)) {
                res[length] = ' ';
            }
            else if (Character.isUpperCase(ch)) {
                res[length] = Character.toLowerCase(ch);
            }
            else {
                res[length] = ch;
            }

            ++length;
        }

        return new String(res, 0, length);
    }

}
