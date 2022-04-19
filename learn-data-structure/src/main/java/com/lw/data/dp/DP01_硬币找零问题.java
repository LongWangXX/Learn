package com.lw.data.dp;

import java.sql.Array;

/**
 * 给定n种不同面值的硬币，分别记为c[0], c[1], c[2], … c[n]，同时还有一个金额k，
 * 编写一个函数计算出最少需要几枚硬币凑出这个金额k？每种硬币的个数不限，且如果没有任何一种硬币组合能组成总金额时，返回 -1。
 * <p>
 * 题解：
 * 1.首先这是一个求最值的问题
 * <p>
 * 2.存在子问题，无后效性
 * <p>
 * 3.根据子问题的答案 + 决策可以推导出父问题的答案
 * <p>
 * <p>
 * 如果k=0 则 硬币等于0；
 * 变量是K。
 * <p>
 * 目标金额为i。    min(dp[i-values]+1,dp[i])
 */
public class DP01_硬币找零问题 {
    public static void main(String[] args) {
        int[] values = {3, 5, 10}; // 硬币面值
        int total = 1; // 总值
        System.out.println(function(values, total));
    }

    public static int function(int[] values, int total) {

        int[] dp = new int[total + 1];
        dp[0] = 0;
        for (int i = 1; i <= total; i++) {
            dp[i] = total + 1;
        }
        for (int i = 1; i <= total; i++) {
            for (int j = 0; j < values.length; j++) {
                int currentValue = values[j];
                if (i >= currentValue)
                    dp[i] = Math.min(dp[i], dp[i - currentValue] + 1);
            }
        }


        return dp[total] == total + 1 ? -1 : dp[total];
    }
}
