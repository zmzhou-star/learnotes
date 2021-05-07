package com.github.zmzhoustar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 求最大面积
 * @title MaxArea
 * @author zmzhou
 * @version 1.0
 * @since 2021/5/7 10:19
 */
public class MaxArea {
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while (null != (line = bf.readLine())) {
			String[] arr = line.split(Constants.REGEX);
			int[] array = new int[arr.length];
			for (int i = 0; i < arr.length; i++) {
				array[i] = Integer.parseInt(arr[i]);
			}
			int left = 0;
			int right = array.length - 1;
			int maxArea = maxArea(array, 0, left, right);
			System.out.println(maxArea);
		}
	}

	private static int maxArea(int[] arr, int maxArea, int left, int right) {
		maxArea = Math.max(maxArea, min(arr, left, right) * (right - left));
		if (arr[left] < arr[right]) {
			left++;
		} else {
			right--;
		}
		if (left >= right) {
			return maxArea;
		} else {
			return maxArea(arr, maxArea, left, right);
		}
	}

	private static int min(int[] arr, int left, int right) {
		return Arrays.stream(Arrays.copyOfRange(arr, left, right + 1)).min().getAsInt();
	}
}
