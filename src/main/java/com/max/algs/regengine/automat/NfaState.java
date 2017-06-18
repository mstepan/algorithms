package com.max.algs.regengine.automat;

import com.max.algs.regengine.char_class.CharClass;
import com.max.algs.regengine.char_class.UnconditionalCharClass;

import java.util.*;

/**
 * Nondeterministic finite automata (NFA) single state.
 */
public class NfaState {

    private final String name;

    private final Map<CharClass, NfaState> transition = new HashMap<>();

    public NfaState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<CharClass, NfaState> getTransition() {
        return Collections.unmodifiableMap(transition);
    }

    public void addTransition(CharClass charClass, NfaState nextState) {
        transition.put(charClass, nextState);
    }

    public void setNullTransitions(NfaState nextState) {

        for (Map.Entry<CharClass, NfaState> entry : transition.entrySet()) {

            if (entry.getValue() == null) {
                entry.setValue(nextState);
            }
        }
    }

    public Set<NfaState> getUnconditionalRecursive() {

        Set<NfaState> epsilonTransitions = new HashSet<>();

        NfaState cur = transition.get(UnconditionalCharClass.INST);

        while (cur != null) {

            if (epsilonTransitions.contains(cur)) {
                break;
            }

            epsilonTransitions.add(cur);
            cur = cur.transition.get(UnconditionalCharClass.INST);
        }

        return epsilonTransitions;

    }

    public Set<NfaState> nextUnconditional() {

        Set<NfaState> unconditionalStates = new HashSet<>();

        NfaState unconditional = transition.get(UnconditionalCharClass.INST);

        if (unconditional != null) {
            unconditionalStates.add(unconditional);
        }

        return unconditionalStates;
    }

    public Set<NfaState> nextStatesWithoutUnconditional(char ch) {

        Set<NfaState> nextStates = new HashSet<>();

        for (Map.Entry<CharClass, NfaState> entry : transition.entrySet()) {

            CharClass chClass = entry.getKey();

            // boolean res = chClass.matched(ch);

            if (chClass != UnconditionalCharClass.INST && chClass.matched(ch)) {
                nextStates.add(entry.getValue());
            }

        }

        return nextStates;
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        NfaState other = (NfaState) obj;

        return com.google.common.base.Objects.equal(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
