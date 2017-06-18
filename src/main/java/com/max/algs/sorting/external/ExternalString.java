package com.max.algs.sorting.external;

public class ExternalString implements Comparable<ExternalString> {

    private final String part;

    public ExternalString(String str, int length) {
        part = str.substring(length);
    }

    @Override
    public String toString() {
        return part;
    }

    @Override
    public int compareTo(ExternalString other) {
        return part.compareTo(other.part);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((part == null) ? 0 : part.hashCode());
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
        ExternalString other = (ExternalString) obj;
        if (part == null) {
            if (other.part != null)
                return false;
        }
        else if (!part.equals(other.part))
            return false;
        return true;
    }

}
