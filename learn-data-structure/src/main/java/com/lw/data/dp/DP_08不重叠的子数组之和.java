package com.lw.data.dp;

public class DP_08不重叠的子数组之和 {
    public static void main(String[] args) {
        int[] a = {-1, 4, -2, 3, -2, 3};
        maxSubArray(a, 2);
    }


    public static int maxSubArray(int[] nums, int k) {
        int n = nums.length;

        int[][] m = new int[n + 1][k + 1];
        int[][] dp = new int[n + 1][k + 1];

        for (int i = 0; i <= n; i++) { // 初始化状态
            for (int j = 0; j <= k; j++) {
                m[i][j] = 0;
                dp[i][j] = 0;
            }
        }

        for (int i = 1; i <= n; i++) { // 决策过程
            for (int j = Math.min(i, k); j > 0; j--) {
                if (i == j) {
                    m[i][j] = m[i - 1][j - 1] + nums[i - 1];
                    dp[i][j] = dp[i - 1][j - 1] + nums[i - 1];
                } else {
                    m[i][j] = Math.max(m[i - 1][j], dp[i - 1][j - 1]) + nums[i - 1];
                    dp[i][j] = Math.max(dp[i - 1][j], m[i][j]);
                }
            }
        }

        return dp[n][k]; // 输出答案
    }
}
