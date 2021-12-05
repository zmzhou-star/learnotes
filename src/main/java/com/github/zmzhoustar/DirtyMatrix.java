/*
 * Copyright © 2020-present zmzhou-star. All Rights Reserved.
 */

package com.github.zmzhoustar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 求最小脏矩阵的序列
 * 矩阵:matrix matrix[i][0],matrix[i][1]表示矩形左上角X坐标，Y坐标 matrix[i][2],matrix[i][3]表示宽度和高度
 * X坐标，Y坐标 <= 1000
 * dirtySequences数组表示dirty矩形的数组下标
 * 求：dirty矩阵的下标（含重叠矩形）
 *
 * 输入示例1：
9
1 5 1 1
9 7 2 1
5 6 1 2
2 2 4 4
6 4 1 1
7 7 1 1
4 5 1 1
7 3 1 2
8 4 1 1

2 5 7

 参考示意图：<a href='https://gitee.com/zmzhou-star/learnotes/blob/master/%E5%8D%8E%E4%B8%BA%E9%9D%A2%E8%AF%95%E9%A2%98/%E6%9C%80%E5%B0%8F%E8%84%8F%E7%9F%A9%E9%98%B5/dirtyMatrix.png'></a>

 * @author zmzhou
 * @version 1.0
 * @since 2021/12/4 20:43
 */
public class DirtyMatrix {
	/**
	 * 分隔符
	 */
	private static final String REGEX = "[\\s]+";

	private static List<String> deMatrix(int[][] matrix, int[] dirtySequences) {
		List<String> result = new ArrayList<>();
		if (dirtySequences.length > 0) {
			int minX = 1000;
			int maxX = 0;
			int minY = 1000;
			int maxY = 0;
			for (int dirtySequence : dirtySequences) {
				minX = Math.min(minX, matrix[dirtySequence][0]);
				maxX = Math.max(maxX, matrix[dirtySequence][0] + matrix[dirtySequence][2]);
				minY = Math.min(minY, matrix[dirtySequence][1] - matrix[dirtySequence][3]);
				maxY = Math.max(maxY, matrix[dirtySequence][1]);
			}
			for (int i = 0; i < matrix.length; i++) {
				if (!(matrix[i][0] >= maxX || matrix[i][0] + matrix[i][2] <= minX
					|| matrix[i][1] <= minY || matrix[i][1] - matrix[i][3] >= maxY)) {
					result.add(String.valueOf(i));
				}
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		System.out.println("请输入矩阵的大小 N ：");
		while (!"".equals(line = br.readLine().trim())) {
			int n = Integer.parseInt(line);
			int[][] matrix = new int[n][4];
			System.out.println("请输入N*4矩阵：");
			for (int i = 0; i < matrix.length; i++) {
				matrix[i] = Arrays.stream(br.readLine().trim().split(REGEX)).mapToInt(Integer::parseInt).toArray();
			}
			System.out.println("请输入长度为M的数组(N > M > 0 && N > M[i] > 0)：");
			String mStr;
			while (!"".equals(mStr = br.readLine().trim())) {
				int[] dirtySequences = Arrays.stream(mStr.split(REGEX)).mapToInt(Integer::parseInt).toArray();
				List<String> result = deMatrix(matrix, dirtySequences);
				System.out.println("[" + String.join(" ", result) + "]");
				System.out.println("请输入长度为M的数组(N > M > 0 && N > M[i] > 0)：");
			}
			System.exit(0);
		}
	}
}
