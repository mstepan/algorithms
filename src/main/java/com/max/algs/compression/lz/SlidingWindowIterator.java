package com.max.algs.compression.lz;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;


class SlidingWindowIterator implements Iterator<Triple> {


    private final SearchBuffer searchBuf;

    private final SelfFetchBuffer lookAheadBuf;

    private Optional<Triple> triple;

    public SlidingWindowIterator(BufferedReader in, int searchBufSize, int lookAheadBufSize) {

        this.searchBuf = new SearchBuffer(searchBufSize);
        this.lookAheadBuf = new SelfFetchBuffer(in, lookAheadBufSize);

        this.triple = nextTriple();
    }

    @Override
    public boolean hasNext() {
        return triple.isPresent();
    }

    @Override
    public Triple next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Triple res = triple.get();

        triple = nextTriple();

        return res;
    }


    private Optional<Triple> nextTriple() {

        if (lookAheadBuf.isEmpty()) {
            return Optional.empty();
        }

        Triple longestTriple;

        if (searchBuf.isEmpty()) {

            char singleCh = lookAheadBuf.arr[0];

            searchBuf.add(singleCh);
            lookAheadBuf.fetch(1);

            longestTriple = new Triple(0, 0, singleCh);
        }
        else {

            longestTriple = new Triple(0, 0, lookAheadBuf.arr[0]);

            for (int searchPos = searchBuf.length() - 1; searchPos >= 0; searchPos--) {

                int from = searchPos;
                int to = 0;

                int seqLength = 0; // take into account

                while (to < lookAheadBuf.effectiveLength() - 1 && from < searchBuf.length()
                        && searchBuf.get(from) == lookAheadBuf.get(to)) {

                    ++seqLength;
                    ++to;
                    ++from;
                }

                if (seqLength != 0 && seqLength >= longestTriple.length) {
                    int offsetFromSearchEnd = searchBuf.length() - searchPos;
                    longestTriple = new Triple(offsetFromSearchEnd, seqLength, lookAheadBuf.arr[to]);
                }
            }

            int elemsToMove = longestTriple.length + 1;

            // copy from 'look ahead buffer' to 'search buffer'
            searchBuf.add(lookAheadBuf.arr, 0, elemsToMove);

            // fetch new data for 'look ahead buffer'
            lookAheadBuf.fetch(elemsToMove);
        }

        return Optional.of(longestTriple);
    }


}
