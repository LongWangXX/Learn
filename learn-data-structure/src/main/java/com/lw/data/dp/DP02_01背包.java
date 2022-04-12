package com.lw.data.dp;

/**
 * 0，1 背包，有N件物品 重量W[N],价值V[N]，数组分别代表第I件物品，重量为W[I],价值为V[I],你有一个背包
 * 要求可以放入的重量K，怎么让背包装的物品价值最高
 * <p>
 * //1.存在子问题。N件物品直接组合
 * <p>
 * //2.子问题之间无后效性
 * <p>
 * //3.最优子结构，大问题 可以有子问题+决策得出
 * <p>
 * 状态转移方程
 * 物品只有两种选择 放入和不放入
 * 假如放入那么背包剩余重量为 k-w[i],假如不放入背包重量为k。
 * 对于背包为0，那么可以存放的重量为0
 * 对于物品数为0，那么可以存放的重量同样为0；
 * 假如只有一件物品，背包重量为1，判断当前背包能承受的重量是否大于 W[1],如果大于则可以放入，将其放入背包，那么背包剩余容量为 1-w[1]，结果等于v[1]+剩余背包容，
 * 可以存放其他物品的最大值（这里为0，因为只有一件物品了）。那么等于 v[1]+0。 假如放入不进去，那么结果就只有0件物品的最大值也同样为0；
 * <p>
 * 状态变量有 可放入背包的物品数，背包重量
 * 定义状态数组dp[i][j],表示用j沉重量的背包去放前i中物品的最大价值。
 * 那么如果如果第i中物品放不下j背包。dp[i][j]等于dp[i-1][j]的值。因为第i件物品放不下，所以结果等于用j重量的背包，存放前i中物品的最大价值。
 * 如果可以放下有两种情况，放或者不放。那么dp[i][j]=max(dp[i-1][j](不放),dp[i-1][j-w[i]]+v[i](放入))
 */
public class DP02_01背包 {
    public static void main(String[] args) {
        //
        int W[] = {0, 3, 2, 5, 11};
        int V[] = {0, 4, 2, 4, 22};
        System.out.println(function(W, V, 10));
    }

    public static int function(int[] W, int[] V, int k) {
        int dp[][] = new int[W.length][k + 1];
        for (int i = 0; i < k + 1; i++) {
            dp[0][i] = 0;
        }
        for (int i = 0; i < W.length; i++) {
            dp[i][0] = 0;
        }
        for (int i = 1; i < W.length ; i++)
            for (int j = 1; j < k + 1; j++) {
                dp[i][j] = -2;
            }

        for (int i = 1; i < W.length; i++) {
            for (int j = 1; j < k + 1; j++) {
                if (j < W[i]) dp[i][j] = dp[i - 1][j];
                else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - W[i]] + V[i]);
                }
            }
        }

        return dp[W.length-1][k];
    }
}
