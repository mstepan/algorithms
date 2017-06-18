package com.max.algs.regengine.matcher;

import com.max.algs.regengine.automat.DfaState;
import com.max.algs.regengine.automat.NfaState;

public final class RegexpMatcher {

    private final DfaState start;
    private final NfaState end;

    public RegexpMatcher(DfaState start, NfaState end) {
        this.start = start;
        this.end = end;
    }

    public boolean match(String str) {

        DfaState cur = start;

        for (int i = 0; i < str.length() && cur != null; i++) {
            char ch = str.charAt(i);
            cur = cur.getTransition(ch);
        }

        if (cur == null) {
            return false;
        }

        for (NfaState nfaState : cur.getNfaStates()) {
            if (nfaState == end) {
                return true;
            }
        }

        return false;

        /** redesign to support DFA automat */
    }

}
