package com.max.algs.fulltextsearch;

import java.util.HashSet;
import java.util.Set;

public enum StopWords {

    INST;


    private final Set<String> dictionary = new HashSet<>();

    private StopWords() {
        dictionary.add("the");
        dictionary.add("of");
        dictionary.add("to");
        dictionary.add("and");
        dictionary.add("in");
        dictionary.add("a");
        dictionary.add("for");
        dictionary.add("it");
        dictionary.add("on");
        dictionary.add("reuter");
        dictionary.add("is");
        dictionary.add("that");
        dictionary.add("from");
        dictionary.add("its");
        dictionary.add("by");
        dictionary.add("will");
        dictionary.add("at");
        dictionary.add("with");
        dictionary.add("was");
        dictionary.add("be");
        dictionary.add("he");
        dictionary.add("has");
        dictionary.add("an");
        dictionary.add("as");
        dictionary.add("would");
        dictionary.add("not");
        dictionary.add("which");
        dictionary.add("are");
        dictionary.add("but");
        dictionary.add("have");
        dictionary.add("or");
        dictionary.add("also");
        dictionary.add("vs");
        dictionary.add("dlrs");
        dictionary.add("mln");
        dictionary.add("pct");
        dictionary.add("cts");
        dictionary.add("this");
    }

    public boolean isStopWord(String word) {
        return dictionary.contains(word);
    }

}
