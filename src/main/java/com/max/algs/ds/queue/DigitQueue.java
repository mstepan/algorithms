package com.max.algs.ds.queue;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Store decimal digits in array based queue. To store digits from 0 till 9, we
 * only need 4 bits per digit.
 * 
 * 
 * One integer value (32 bits) can store up to 8 digits (each 4 bits long).
 * 
 * @author Maksym Stepanenko.
 *
 */
public class DigitQueue {

	private StorageStrategy storage = new WordStorage();

	private int size;

	public void add(int value) {
		checkArgument(value >= 0 && value < 10,
				"Value should be in range [0;9]");

		if (size == storage.capacity()) {
			storage = new ArrayStorage(storage);
		}

		storage.store(value);

		size += 1;
	}

	public int poll() {
		checkArgument(!isEmpty(), "Queue is empty");

		int value = storage.extract();

		--size;

		return value;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private static interface StorageStrategy {
		void store(int value);

		int extract();

		int capacity();
	}

	private class WordStorage implements StorageStrategy {

		private int data;

		@Override
		public void store(int value) {
			data |= (value & 0x0F) << (size << 2);
		}

		@Override
		public int extract() {
			int value = data & 0x0F;
			data >>>= 4;
			return value;
		}

		@Override
		public int capacity() {
			return 8;
		}

	}

	/**
	 * head stored in 'arr[extractIndex]'
	 * 
	 * tail stored in 'arr[storeIndex]'
	 * 
	 * @author Maksym Stepanenko
	 *
	 */
	private class ArrayStorage implements StorageStrategy {

		private final int[] arr;

		public ArrayStorage(StorageStrategy prevStorage) {
			arr = new int[8];
			arr[0] = ((WordStorage) prevStorage).data;
		}

		@Override
		public void store(int value) {

		}

		@Override
		public int extract() {
			return 0;
		}

		@Override
		public int capacity() {
			return arr.length << 3;
		}

	}

}
