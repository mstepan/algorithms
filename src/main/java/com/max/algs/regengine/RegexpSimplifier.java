package com.max.algs.regengine;


public final class RegexpSimplifier {

    public RegexpSimplifier() {
        super();
    }

    public static String simplify(String regexp) {

        StringBuilder simpleRegexp = new StringBuilder();

        char[] regArr = regexp.toCharArray();

        for (int i = 0; i < regArr.length; i++) {
            if (regArr[i] == '+') {

                if (i - 2 >= 0 && regArr[i - 2] == '\\') {
                    simpleRegexp.append('\\').append(regArr[i - 1]).append('*');
                }
                else {
                    simpleRegexp.append(regArr[i - 1]).append("*");
                }
            }
            else {
                simpleRegexp.append(regArr[i]);
            }

        }


        return simpleRegexp.toString();
    }


}
