package com.max.algs.sorting;

import java.util.Arrays;

import com.max.algs.util.ArrayUtils;

public final class SortUtils {

	private static final int INSERTION_SORT_TRESHOLD = 47;

	/**
	 * 
	 * Merge sorted subarray back to array.
	 * 
	 * 
	 * @param arr
	 * @param from1
	 * @param to1
	 * @param from2
	 * @param to2
	 */
	public static void merge(int[] arr, int from1, int to1, int from2, int to2) {

		int[] left = new int[to1 - from1 + 1];
		System.arraycopy(arr, from1, left, 0, left.length);

		int[] right = new int[to2 - from2 + 1];
		System.arraycopy(arr, from2, right, 0, right.length);

		int baseIndex = from1;
		int leftIndex = 0;
		int rightIndex = 0;

		while (leftIndex < left.length || rightIndex < right.length) {

			// 'left' exosted
			if (leftIndex >= left.length) {
				arr[baseIndex] = right[rightIndex];
				++rightIndex;
			}
			// 'right' exosted
			else
				if (rightIndex >= right.length) {
					arr[baseIndex] = left[leftIndex];
					++leftIndex;
				}
				else {
					if (left[leftIndex] < right[rightIndex]) {
						arr[baseIndex] = left[leftIndex];
						++leftIndex;
					}
					else {
						arr[baseIndex] = right[rightIndex];
						++rightIndex;
					}
				}

			++baseIndex;
		}

	}

	/**
	 * time: O(to - from) space: O(1)
	 * 
	 */
	public static int qPartition(int[] arr, int from, int to) {

		if (arr == null) {
			throw new IllegalArgumentException("Array is NULL");
		}

		if (from > to) {
			throw new IllegalArgumentException("'from' > 'to': " + from + " > "
					+ to);
		}

		if (from == to) {
			return from;
		}

		int pivot = arr[to];

		int boundary = from;

		for (int i = from; i < to; i++) {

			if (arr[i] < pivot) {
				ArrayUtils.swap(arr, boundary, i);
				++boundary;
			}
		}

		ArrayUtils.swap(arr, arr[boundary], to);

		return boundary;
	}

	/**
	 * 
	 * time: O(N) space: O(1)
	 * 
	 */
	public static void threeWayPartition(int[] arr, int pivot) {

		if (arr == null) {
			throw new IllegalArgumentException("Array is NULL");
		}

		int equalStartIndex = 0;
		int greaterStartIndex = 0;

		for (int i = 0; i < arr.length; i++) {
			// equal case
			if (arr[i] == pivot) {
				swap(arr, greaterStartIndex, i);
				greaterStartIndex++;
			}
			// less case
			else
				if (arr[i] < pivot) {
					swap(arr, greaterStartIndex, i);
					swap(arr, equalStartIndex, greaterStartIndex);
					greaterStartIndex++;
					equalStartIndex++;
				}
		}
	}

	public static void qsort(int[] arr) {
		qsort(arr, 0, arr.length - 1);
	}

	private static void qsort(int[] arr, int from, int to) {

		if (from < 0 || to >= arr.length) {
			throw new IllegalArgumentException(
					"from/to index is incorrect: from = " + from + ", to = "
							+ to);
		}

		if (to - from <= 0) {
			return;
		}

		if (to - from < INSERTION_SORT_TRESHOLD) {
			insertionSort(arr, from, to);
		}
		else {

			int smallPivot = arr[from];
			int bigPivot = arr[to];

			if (smallPivot > bigPivot) {
				swap(arr, from, to);
				bigPivot = arr[to];
			}

			// do dual pivot quicksort

			int less = from;
			int middle = from;
			int greater = to;

			int index = from + 1;

			while (index < greater) {

				int temp = arr[index];

				// put to smaller part
				if (arr[index] < smallPivot) {
					arr[index] = arr[less + 1];
					++middle;

					arr[less + 1] = temp;
					++less;

					++index;
				}
				// put to middle part
				else
					if (arr[index] >= smallPivot && arr[index] <= bigPivot) {
						++middle;
						++index;
					}
					// put to greater part
					else {
						arr[index] = arr[greater - 1];
						arr[greater - 1] = temp;
						--greater;
					}

			}

			if (less != from) {
				swap(arr, from, less);
				--less;
			}

			if (greater != to) {
				swap(arr, to, greater + 1);
				++greater;
			}

			if (less != from) {
				qsort(arr, from, less);
			}

			if (middle != less) {
				qsort(arr, less + 2, middle);
			}

			if (middle + 1 != to) {
				qsort(arr, middle + 2, to);
			}
		}
	}

	private static void swap(int[] arr, int from, int to) {
		int temp = arr[from];
		arr[from] = arr[to];
		arr[to] = temp;
	}

	public static void insertionSort(int[] arr) {
		insertionSort(arr, 0, arr.length - 1);
	}

	/**
	 * Binary insertion sort.
	 * 
	 * time: O(N^2) space: O(1)
	 * 
	 */
	public static void insertionSort(int[] arr, int from, int to) {

		if (arr == null) {
			throw new IllegalArgumentException("NULL array passed");
		}

		if (from > to) {
			throw new IllegalArgumentException("from > to: " + from + " > "
					+ to);
		}

		if (from < 0) {
			throw new IllegalArgumentException(
					"'from' index can't be negative: " + from);
		}

		if (to > arr.length) {
			throw new IllegalArgumentException("'to' index too big: " + to
					+ ", should be less then " + arr.length);
		}

		// don't sort 1 element array
		if (from == to) {
			return;
		}

		int temp;
		int index;

		for (int i = from + 1; i <= to; i++) {

			if (arr[i - 1] > arr[i]) {

				temp = arr[i];
				index = Arrays.binarySearch(arr, 0, i - 1, temp);

				if (index < 0) {
					index = (-index) - 1;
				}
				else {
					index += 1;
				}

				System.arraycopy(arr, index, arr, index + 1, i - index);

				arr[index] = temp;
			}
		}

	}

	public static void insertionSortSimple(int[] arr) {

		if (arr == null) {
			throw new IllegalArgumentException("NULL array passed");
		}

		int temp;
		int j;
		for (int i = 1; i < arr.length; i++) {

			temp = arr[i];
			j = i - 1;

			while (j >= 0 && arr[j] > temp) {
				arr[j + 1] = arr[j];
				--j;
			}

			arr[j + 1] = temp;
		}
	}

	private SortUtils() {
		super();
	}

}
