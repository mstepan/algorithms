package com.max.algs.fulltextsearch;

public enum SnowballStemmer {

    INST;

    private final org.tartarus.snowball.SnowballStemmer stemmer;

    private SnowballStemmer() {
        try {
            Class<?> stemClass = Class
                    .forName("org.tartarus.snowball.ext.englishStemmer");
            stemmer = (org.tartarus.snowball.SnowballStemmer) stemClass.newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            throw new IllegalStateException("Can't instantiate stemmer class");
        }
    }

    public String stemm(String word) {
        stemmer.setCurrent(word);
        stemmer.stem();
        return stemmer.getCurrent();
    }
}
