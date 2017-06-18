package com.max.algs.regengine.automat;

import com.max.algs.regengine.char_class.CharClass;
import com.max.algs.regengine.char_class.DotCharClass;

import java.util.*;

/**
 * Deterministic finite automata(DFA) single state .
 */
public final class DfaState {

    private final String name;

    private Map<CharClass, DfaState> transitions = new HashMap<>();

    private Set<NfaState> nfaStates = new HashSet<>();

    public DfaState(Set<NfaState> states) {
        this.name = combineNames(states);
        this.nfaStates.clear();
        this.nfaStates.addAll(states);
    }

    public DfaState(String name) {
        this.name = name;
    }

    public Set<NfaState> getDotTransitions() {

        Set<NfaState> dotTransitions = new HashSet<>();

        for (NfaState nfaState : nfaStates) {

            NfaState nextDotTransition = nfaState.getTransition().get(
                    DotCharClass.INST);

            if (nextDotTransition != null) {
                dotTransitions.add(nextDotTransition);
            }
        }

        return dotTransitions;

    }

    public DfaState getTransition(char ch) {
        for (Map.Entry<CharClass, DfaState> entry : transitions.entrySet()) {
            if (entry.getKey().matched(ch)) {
                return entry.getValue();
            }
        }

        return null;
    }

    public Set<NfaState> getNfaStates() {
        return Collections.unmodifiableSet(nfaStates);
    }

    public String getName() {
        return name;
    }

    public void addTransition(CharClass charCl, DfaState nextState) {
        transitions.put(charCl, nextState);
    }

    @Override
    public String toString() {
        return name;
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

        DfaState other = (DfaState) obj;

        return com.google.common.base.Objects.equal(name, other.name);
    }

    private String combineNames(Set<NfaState> states) {

        List<String> names = new ArrayList<>();

        for (NfaState state : states) {
            names.add(state.getName());
        }

        Collections.sort(names);

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < names.size(); i++) {
            if (i > 0) {
                buf.append(",");
            }
            buf.append(names.get(i));
        }

        return buf.toString();
    }

}
