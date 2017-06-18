package com.max.algs.permutation;

import java.util.*;

/**
 * Read only permutation generator.
 */
public class PermutationIterator<T extends Comparable<T>> implements Iterator<List<T>> {

    private final ElementWithDirection<T>[] arr;

    private List<T> curPermutation;

    public PermutationIterator(List<T> elements) {
        arr = new ElementWithDirection[elements.size()];

        for (int i = 0; i < elements.size(); i++) {
            arr[i] = new ElementWithDirection<>(elements.get(i), Direction.LEFT);
        }

        Arrays.sort(arr);

        curPermutation = constructPermutation();
    }

    public PermutationIterator(T[] elements) {
        arr = new ElementWithDirection[elements.length];

        for (int i = 0; i < elements.length; i++) {
            arr[i] = new ElementWithDirection<>(elements[i], Direction.LEFT);
        }

        Arrays.sort(arr);

        curPermutation = constructPermutation();
    }

    @Override
    public boolean hasNext() {
        return curPermutation != null;
    }

    @Override
    public List<T> next() {

        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        List<T> retValue = new ArrayList<>(curPermutation);

        curPermutation = nextPermutation();

        return retValue;
    }

    private List<T> nextPermutation() {

        int maxMobileIndex = -1;

        for (int i = 0; i < arr.length; i++) {
            ElementWithDirection<T> elem = arr[i];

            if (elem.direction == Direction.LEFT && i > 0 && arr[i].compareTo(arr[i - 1]) > 0) {
                if (maxMobileIndex == -1 || elem.compareTo(arr[maxMobileIndex]) > 0) {
                    maxMobileIndex = i;
                }
            }
            else if (elem.direction == Direction.RIGHT && i < arr.length - 1 && arr[i].compareTo(arr[i + 1]) > 0) {
                if (maxMobileIndex == -1 || elem.compareTo(arr[maxMobileIndex]) > 0) {
                    maxMobileIndex = i;
                }
            }
        }

        // has mobile element
        if (maxMobileIndex != -1) {


            ElementWithDirection<T> mobileElement = arr[maxMobileIndex];

            int otherIndex = maxMobileIndex - 1;

            if (mobileElement.direction == Direction.RIGHT) {
                otherIndex = maxMobileIndex + 1;
            }

            // swap mobile element
            swap(maxMobileIndex, otherIndex);

            // change direction for all bigger elements
            for (ElementWithDirection<T> element : arr) {

                if (element.compareTo(mobileElement) > 0) {
                    element.changeDirection();
                }
            }

            return constructPermutation();
        }


        return null;
    }

    private void swap(int from, int to) {
        ElementWithDirection<T> temp = arr[from];

        arr[from] = arr[to];
        arr[to] = temp;
    }

    private List<T> constructPermutation() {
        List<T> singlePermutation = new ArrayList<>();

        for (ElementWithDirection<T> element : arr) {
            singlePermutation.add(element.value);
        }

        return singlePermutation;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();

    }

    private static enum Direction {
        LEFT("<-"),
        RIGHT("->");

        private final String symbol;

        Direction(String symbol) {
            this.symbol = symbol;
        }

    }

    private static final class ElementWithDirection<U extends Comparable<U>>
            implements Comparable<ElementWithDirection<U>> {

        final U value;
        Direction direction;

        public ElementWithDirection(U value, Direction direction) {
            this.value = value;
            this.direction = direction;
        }

        private void changeDirection() {
            direction = (direction == Direction.LEFT) ? Direction.RIGHT : Direction.LEFT;
        }

        @Override
        public int compareTo(ElementWithDirection<U> other) {
            return value.compareTo(other.value);
        }

        @Override
        public String toString() {
            assert direction != null && value != null;
            return String.valueOf(direction.symbol) + ":" + String.valueOf(value);
        }
    }

}
