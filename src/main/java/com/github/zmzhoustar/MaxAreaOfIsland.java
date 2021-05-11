package com.github.zmzhoustar;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Leetcode算法题：695. 岛屿的最大面积
 * 给定一个包含了一些 0 和 1 的非空二维数组 grid 。
 * 一个 岛屿 是由一些相邻的 1 (代表土地) 构成的组合，这里的「相邻」要求两个 1 必须在水平或者竖直方向上相邻。你可以假设 grid 的四个边缘都被 0（代表水）包围着。
 * 找到给定的二维数组中最大的岛屿面积。(如果没有岛屿，则返回面积为 0 。)
 * 算法：
 * BFS(广度优先遍历) / DFS(深度优先遍历)
 * @author zmzhou
 * @version 1.0
 * @since 2021/5/11 11:53
 */
public class MaxAreaOfIsland {

	/** 上下左右四个移动方向 */
	private static final int[][] MOVE_ARRAY = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

	public static void main(String[] args) {
		int[][] grid =
			   {{0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0},
				{0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0},
				{0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
				{0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
				{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
		int[][] cloneGrid = cloneGrid(grid);
		// 递归实现深度优先遍历
		System.out.println(maxAreaOfIsland1(cloneGrid));
		cloneGrid = cloneGrid(grid);
		// 栈 + 深度优先遍历
		System.out.println(maxAreaOfIsland2(cloneGrid));
		cloneGrid = cloneGrid(grid);
		// 队列方法实现广度优先遍历
		System.out.println(maxAreaOfIsland3(cloneGrid));
	}

	/**
	 * 克隆测试数据
	 * @author zmzhou
	 * @since 2021/5/11 14:12
	 */
	private static int[][] cloneGrid(int[][] grid) {
		int[][] cloneGrid = grid.clone();
		for (int i = 0; i < grid.length; i++) {
			cloneGrid[i] = grid[i].clone();
		}
		return cloneGrid;
	}

	/**
	 * 递归实现深度优先遍历
	 *
	 * @author zmzhou
	 * @since 2021/5/11 12:00
	 */
	public static int maxAreaOfIsland1(int[][] grid) {
		int max = 0;
		int m = grid.length;
		int n = grid[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				max = Math.max(max, dfs(grid, i, j));
			}
		}
		return max;
	}

	/**
	 * 递归实现深度优先遍历
	 *
	 * @author zmzhou
	 * @since 2021/5/11 12:00
	 */
	public static int dfs(int[][] grid, int i, int j) {
		int area = 0;
		//数组越界或者该点为0，就返回0
		if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] <= 0) {
			return 0;
		}
		//访问过的点就置-1
		grid[i][j] = -1;
		++area;
		for (int[] x : MOVE_ARRAY) {
			area += dfs(grid, i + x[0], j + x[1]);
		}
		return area;
	}

	/**
	 * 栈 + 深度优先遍历（非常重要，要加深理解）
	 * @author zmzhou
	 * @since 2021/5/11 13:39
	 */
	public static int maxAreaOfIsland2(int[][] grid) {
		int max = 0;
		int m = grid.length;
		int n = grid[0].length;
		//用栈实现深度优先遍历
		Deque<int[]> stack = new LinkedList<>();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				stack.add(new int[]{i, j});
				int area = 0;
				while (!stack.isEmpty()) {
					int x = stack.peek()[0];
					int y = stack.pop()[1];
					//数组越界或者该点为0，就继续下一个循环
					if (x < 0 || y < 0 || x >= m || y >= n || grid[x][y] <= 0) {
						continue;
					}
					//访问过的点就置-1
					grid[x][y] = -1;
					++area;
					//上下左右四个方向都要访问
					for (int[] move : MOVE_ARRAY) {
						stack.add(new int[]{x + move[0], y + move[1]});
					}
				}
				max = Math.max(max, area);
			}
		}
		return max;
	}
	/**
	 * 队列方法实现广度优先遍历
	 * BFS算法
	 * @author zmzhou
	 * @since 2021/5/11 13:41
	 */
	public static int maxAreaOfIsland3(int[][] grid) {
		int max = 0;
		int m = grid.length;
		int n = grid[0].length;
		//用队列实现广度优先遍历
		Deque<int[]> queue = new LinkedList<>();
		for(int i = 0; i < m; i++){
			for(int j = 0; j < n; j++){
				queue.addLast(new int[]{i, j});
				int area = 0;
				while (!queue.isEmpty()){
					int size = queue.size();
					for(int t = 0; t <size; t++){
						int x = queue.peekFirst()[0];
						int y = queue.removeFirst()[1];
						//数组越界或者该点为0，就继续下一个循环
						if(x < 0 || y < 0 || x >= m || y >= n || grid[x][y] <= 0) {
							continue;
						}
						//访问过的点就置-1
						grid[x][y] = -1;
						++area;
						//上下左右四个方向都要访问
						for(int[] move : MOVE_ARRAY){
							queue.add(new int[]{x + move[0], y + move[1]});
						}
					}
				}
				max = Math.max(max, area);
			}
		}
		return max;
	}
}
