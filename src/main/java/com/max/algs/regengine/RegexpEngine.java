package com.max.algs.regengine;

import com.max.algs.regengine.automat.DfaState;
import com.max.algs.regengine.automat.NfaState;
import com.max.algs.regengine.automat.NfaToDfaConvertorUtils;
import com.max.algs.regengine.char_class.*;
import com.max.algs.regengine.matcher.RegexpMatcher;

import java.util.ArrayDeque;
import java.util.Deque;

public class RegexpEngine {


    private final String originalRegexp;
    private final String simplifiedRegexp;

    private final NfaState END = new NfaState("END");

    private final RegexpMatcher matcher;

    public RegexpEngine(String regexp) {

        if (!RegexpValidator.isValid(regexp)) {
            throw new IllegalArgumentException("Invalid regular expression pattern: '" + regexp + "'");
        }

        this.originalRegexp = regexp;
        this.simplifiedRegexp = RegexpSimplifier.simplify(regexp);
        this.matcher = new RegexpMatcher(compileAutomat(simplifiedRegexp.toCharArray()), END);
    }

    public boolean match(String str) {
        return matcher.match(str);
    }


    private DfaState compileAutomat(char[] regexp) {

        Deque<NfaState> statesStack = new ArrayDeque<>();

        int statesCount = 0;

        for (int i = 0; i < regexp.length; i++) {

            char ch = regexp[i];

            NfaState newState = new NfaState(String.valueOf(statesCount));

            if (ch == '\\') {

                char nextCh = regexp[i + 1];

                if (nextCh == RegexpMetachar.W || nextCh == RegexpMetachar.D) {
                    newState.addTransition((nextCh == RegexpMetachar.W ? WChar.INST : DChar.INST), null);
                    statesStack.push(newState);
                    ++statesCount;
                    ++i;
                }
            }

            else if (ch == RegexpMetachar.QUESTION) {
                NfaState prevState = statesStack.peekFirst();
                prevState.addTransition(UnconditionalCharClass.INST, null);
            }

            else if (ch == RegexpMetachar.ASTERIX) {
                NfaState prevState = statesStack.peekFirst();
                prevState.setNullTransitions(prevState);
                prevState.addTransition(UnconditionalCharClass.INST, null);
            }

            else if (ch == RegexpMetachar.OR) {
                char orAlternativeCh = regexp[i + 1];
                NfaState prevState = statesStack.peekFirst();
                prevState.addTransition(new SingleChar(orAlternativeCh), null);
                i += 2;
            }

            else if (ch == RegexpMetachar.DOT) {
                newState.addTransition(DotCharClass.INST, null);
                statesStack.push(newState);
                ++statesCount;
            }
            // create simple state
            else if (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '@' || ch == '_') {
                newState.addTransition(new SingleChar(ch), null);
                ++statesCount;
                statesStack.push(newState);
            }
        }

        statesStack.push(END);

        NfaState rootNfaState = combineStates(statesStack);

        DfaState dfa = NfaToDfaConvertorUtils.convert(rootNfaState);

        return dfa;
    }


    private NfaState combineStates(Deque<NfaState> states) {

        NfaState prev = null;

        while (!states.isEmpty()) {
            NfaState cur = states.pop();
            cur.setNullTransitions(prev);
            prev = cur;
        }


        return prev;
    }

    @Override
    public String toString() {
        return "original: " + originalRegexp + ", simplified: " + simplifiedRegexp;
    }

}
