package com.max.algs.regengine.char_class;

public class SingleChar implements CharClass {


    private final char originalCh;


    public SingleChar(char originalCh) {
        this.originalCh = originalCh;
    }


    @Override
    public boolean matched(char ch) {
        return ch == originalCh;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + originalCh;
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
        SingleChar other = (SingleChar) obj;
        if (originalCh != other.originalCh)
            return false;
        return true;
    }


    public String toString() {
        return String.valueOf(originalCh);
    }

}
