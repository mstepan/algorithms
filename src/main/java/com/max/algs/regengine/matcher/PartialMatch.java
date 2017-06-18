package com.max.algs.regengine.matcher;

import com.max.algs.regengine.automat.NfaState;

class PartialMatch {

    final int index;
    final NfaState state;

    PartialMatch(int index, NfaState state) {
        super();
        this.index = index;
        this.state = state;
    }

    @Override
    public String toString() {

        if (state == null) {
            return "null";
        }

        return state.getName() + ", " + state.getTransition();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PartialMatch other = (PartialMatch) obj;
        if (index != other.index)
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        }
        else if (!state.equals(other.state))
            return false;
        return true;
    }

}
