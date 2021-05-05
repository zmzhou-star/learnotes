package com.github.zmzhoustar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 题目描述
 * 请根据输入两个字符串，求它们的最长公共字串，输出最长公共字串的长度。
 * 找不到字串输出：0
 * 例如：
 * 输入：ab1abc2ddd ab1abc 最长公共字串为ab1abc，输出结果6
 * 输入：abcdef abc1def 最长公共字串为abc，输出结果3
 *
 * @author zmzhou
 * @version 1.0
 * date 2021/4/29 17:23
 */
public class LongestCommonStr {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		System.out.println("输入两个字符串：");
		while (!"".equals(line = br.readLine())) {
			// 排序
			Object[] arr = Arrays.stream(line.split("\\s+"))
				.sorted((Comparator.comparingInt(String::length))).toArray();
			// 长的放第一个
			String first = arr[1].toString();
			String second = arr[0].toString();
			int result = maxStr(first, second);
			System.out.println(result);
		}
	}

	private static int maxStr(String max, String min) {
		String target;
		//最外层：min子串的长度，从最大长度开始
		for (int i = min.length(); i >= 1; i--) {
			//遍历长度为i的min子串，从0开始
			for (int j = 0; j <= min.length() - i; j++) {
				target = min.substring(j, j + i);
				// max串含有字串，则输出最大长度
				if (max.contains(target)){
					return target.length();
				}
			}
		}
		return 0;
	}
}
