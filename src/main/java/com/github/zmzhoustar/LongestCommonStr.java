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
			Object[] arr = Arrays.stream(line.split("\\s+"))
					.sorted((Comparator.comparingInt(String::length))).toArray();
			String first = arr[1].toString();
			String second = arr[0].toString();
			int result = 0;
			result = maxStr(first, second, 0, result);
			System.out.println(result);
		}
	}

	private static int maxStr(String first, String second, int n, int result) {
		if (first.equals(second)) {
			return second.length();
		}
		if (n >= first.length()) {
			return result;
		}
		int p = Math.max(0, second.indexOf(first.charAt(n)));
		for (int i = n; i < first.length(); i++) {
			if (p < second.length() && first.charAt(i) == second.charAt(p)) {
				p += 1;
			} else {
				result = Math.max(result, p);
				n += 1;
				result = Math.max(result, maxStr(first, second, n, result));
			}
		}
		return result;
	}
}
