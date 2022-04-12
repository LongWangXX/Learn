package com.lw.data.dp;

/**
 * 完全背包，有N件物品 重量W[N],价值V[N]，数组分别代表第I件物品，重量为W[I],价值为V[I],你有一个背包
 * * 要求可以放入的重量K，怎么让背包装的物品价值最高,每一种物品可以放无数次
 * 题解：
 * 基于0,1背包，只需要考虑，一件物品放入或者不放入。而现在完全背包则要考虑放0件~x件的价值最大。x*w[i]<k
 * dp[i][j]表示用j容量的背包装前I件物品的最大价值。每件物品可以重复放入
 * <p>
 * dp[i][j] = max(dp[i-1][j],d[i-i][j-1*w[i]]+1*v[i],d[i-i][j-2*w[i]]+2*v[i],d[i-i][j-x*w[i]]+x*v[i])(x*w[i]<k)
 */
public class DP03_完全背包 {
    public static void main(String[] args) {
        int W[] = {0, 3, 2, 5, 11};
        int V[] = {0, 4, 3, 4, 22};
        System.out.println(function(W, V, 4));
    }

    public static int function(int[] W, int[] V, int k) {

        int dp[][] = new int[W.length][k + 1];
        for (int i = 0; i < k + 1; i++) {
            dp[0][i] = 0;
        }
        for (int i = 0; i < W.length; i++) {
            dp[i][0] = 0;
        }
        for (int i = 1; i < W.length; i++) {
            for (int j = 1; j < k + 1; j++) {
                int x = 1;
                dp[i][j] = dp[i - 1][j];
                while (x * W[i] <= j) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - x * W[i]] + x * V[i]);
                    x++;
                }
            }
        }
        return dp[W.length-1][k];
    }
}
