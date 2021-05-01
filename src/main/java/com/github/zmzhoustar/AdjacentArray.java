package com.github.zmzhoustar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 题目：
 * 给定一个 N*N 的矩阵，例如：
1  2  3  4  5
11 12 13 14 15
21 22 23 24 25
31 32 33 34 34
66 63 65 67 62
 给一个长度为 M 的数组，M > N
2 12 13 23 33 65,67
 将数组映射到矩阵上，判断数组是否是相邻数组？
 * @author zmzhou
 * @version 1.0
 * @since 2021/4/26 9:46
 */
public class AdjacentArray {
	/**
	 * 分隔符
	 */
	private static final String REGEX = "[\\s,]+";

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		System.out.println("请输入矩阵的大小 N ：");
		while (!"".equals(line = br.readLine().trim())) {
			int n = Integer.parseInt(line);
			String[][] input = new String[n][n];
			System.out.println("请输入N*N矩阵：");
			for (int i = 0; i < input.length; i++) {
				input[i] = br.readLine().trim().split(REGEX);
			}
			System.out.println("请输入长度为M的数组(M > N)：");
			String mStr;
			while (!"".equals(mStr = br.readLine().trim())) {
				String[] m = mStr.split(REGEX);
				System.out.println("是否为相邻数组判断结果为：" + adjacentArray(input, m));
				System.out.println("请输入长度为M的数组：");
			}
			System.exit(0);
		}
	}

	/**
	 * 将数组映射到矩阵上，判断数组是否为相邻数组
	 *
	 * @param input N*N矩阵
	 * @param m     长度为M的数组 M > N
	 * @return 是否为相邻数组
	 * @author zmzhou
	 * date 2021/4/26 9:42
	 */
	private static boolean adjacentArray(String[][] input, String[] m) {
		Map<String, List<int[]>> map = new LinkedHashMap<>(m.length);
		for (String s : m) {
			map.put(s, new ArrayList<>());
		}
		for (int i = 0; i < input.length; i++) {
			String[] str = input[i];
			for (int j = 0; j < str.length; j++) {
				if (map.containsKey(input[i][j])) {
					int[] point = new int[]{i, j};
					List<int[]> value = map.get(input[i][j]);
					if (!value.isEmpty() && value.contains(point)) {
						continue;
					}
					value.add(point);
				}
			}
		}
		// 指针
		List<int[]> pointer = map.get(m[0]);
		// 存在找不到的数字
		if (pointer.isEmpty()) {
			return false;
		}
		// 路由结果集
		List<int[]> router = new LinkedList<>();
		for (int i = 1; i < m.length; i++) {
			final List<int[]> next = map.get(m[i]);
			for (int[] p : pointer) {
				for (int[] n : next) {
					boolean requirement =
							// 竖着走
							(n[0] - p[0] == 1 && n[1] == p[1]) ||
							// 横着走
							(n[0] == p[0] && n[1] - p[1] == 1) ||
							// 横着往回走
							(n[0] == p[0] && n[1] - p[1] == -1) ||
							// 竖着往回走
							(n[0] - p[0] == -1 && n[1] == p[1]);
					if (requirement && addRouter(router, p, n)) {
						// 符合连续路由条件，移动指针
						pointer = next;
					}
				}
			}
		}
		return router.size() == m.length;
	}

	/**
	 * 添加路由
	 *
	 * @param p 上一个路由坐标
	 * @param n 当前路由坐标
	 * @return 返回添加路由成功结果
	 * @author zmzhou
	 * date 2021/4/26 11:08
	 */
	private static boolean addRouter(List<int[]> router, int[] p, int[] n) {
		// 第一个才添加
		if (router.isEmpty()) {
			router.add(p);
		}
		// 路由集合不存在当前坐标，当前坐标合法，添加
		if (!router.contains(n) && Arrays.equals(router.get(router.size() - 1), p)) {
			router.add(n);
			return true;
		}
		return false;
	}

}
