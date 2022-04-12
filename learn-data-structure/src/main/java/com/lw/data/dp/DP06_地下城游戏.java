package com.lw.data.dp;
/*
一些恶魔抓住了公主（P）并将她关在了地下城的右下角。地下城是由 M x N 个房间组成的二维网格。我们英勇的骑士（K）最初被安置在左上角的房间里，他必须穿过地下城并通过对抗恶魔来拯救公主。

骑士的初始健康点数为一个正整数。如果他的健康点数在某一时刻降至 0 或以下，他会立即死亡。

有些房间由恶魔守卫，因此骑士在进入这些房间时会失去健康点数（若房间里的值为负整数，则表示骑士将损失健康点数）；其他房间要么是空的（房间里的值为 0），要么包含增加骑士健康点数的魔法球（若房间里的值为正整数，则表示骑士将增加健康点数）。

为了尽快到达公主，骑士决定每次只向右或向下移动一步。

 

编写一个函数来计算确保骑士能够拯救到公主所需的最低初始健康点数。

例如，考虑到如下布局的地下城，如果骑士遵循最佳路径 右 -> 右 -> 下 -> 下，则骑士的初始健康点数至少为 7。

-2 (K)	-3	3
-5	-10	1
10	30	-5 (P)
 

说明:

骑士的健康点数没有上限。

任何房间都可能对骑士的健康点数造成威胁，也可能增加骑士的健康点数，包括骑士进入的左上角房间以及公主被监禁的右下角房间。

 */

public class DP06_地下城游戏 {
    public static void main(String[] args) {
        int[][] a = {{1,-3,3},{0,-2,0},{-3,-3,-3}};
        System.out.println(calculateMinimumHP(a));
    }

    public static int calculateMinimumHP(int[][] dungeon) {

            int[][] dp = new int[dungeon.length][dungeon[0].length];
            int[][] dp2 = new int[dungeon.length][dungeon[0].length];
            dp[0][0] = dungeon[0][0];
            dp2[0][0] = 0;
            if (dungeon[0][0] > 0) {
                dp2[0][0] = 1;
                dp[0][0] = dp[0][0] + 1;
            }
            else{
                dp2[0][0] = -dungeon[0][0] + 1;
                dp[0][0] = 1;
            }

            for (int i = 1; i < dungeon.length; i++) {
                dp[i][0] = dp[i - 1][0] + dungeon[i][0];
                if (dungeon[i][0] >= 0) dp2[i][0] = dp2[i - 1][0];
                else {
                    if (dp[i][0] > 0) dp2[i][0] = dp2[i - 1][0];
                    else {
                        dp2[i][0] = dp2[i - 1][0] - dp[i][0] + 1;
                        dp[i][0] = 1;
                    }
                }
            }

            for (int i = 1; i < dungeon[0].length; i++) {
                dp[0][i] = dp[0][i - 1] + dungeon[0][i];
                if (dungeon[0][i] >= 0) dp2[0][i] = dp2[0][i - 1];
                else {
                    if (dp[0][i] > 0) dp2[0][i] = dp2[0][i - 1];
                    else {
                        dp2[0][i] = dp2[0][i - 1] - dp[0][i] + 1;
                        dp[0][i] = 1;
                    }
                }
            }

            for (int i = 1; i < dungeon.length; i++) {
                for (int j = 1; j < dungeon[0].length; j++) {
                    if (dungeon[i][j] >= 0) {
                        if (dp2[i][j - 1] > dp2[i - 1][j]) {
                            dp2[i][j] = dp2[i - 1][j];
                            dp[i][j] = dp[i - 1][j] + dungeon[i][j];
                        } else {
                            dp2[i][j] = dp2[i][j - 1];
                            dp[i][j] = dp[i][j - 1] + dungeon[i][j];
                        }
                    } else {
                        int c1 = dp[i][j - 1] + dungeon[i][j];
                        int c2 = dp[i - 1][j] + dungeon[i][j];
                        int tmp1 = 0;
                        int tmp2 = 0;
                        int x1 = 0;
                        int x2 = 0;
                        if (c1 > 0) {
                            tmp1 = dp2[i][j - 1];
                            x1 = c1;
                        } else {
                            tmp1 = dp2[i][j - 1] - c1 + 1;
                            x1 = 1;
                        }
                        if (c2 > 0) {
                            tmp2 = dp2[i - 1][j];
                            x2 = c2;
                        } else {
                            tmp2 = dp2[i - 1][j] - c2 + 1;
                            x2 = 1;
                        }
                        if (tmp1 > tmp2) {
                            dp2[i][j] = tmp2;
                            dp[i][j] = x2;
                        } else {
                            dp2[i][j] = tmp1;
                            dp[i][j] = x1;
                        }
                    }
                }
            }
            return dp2[dungeon.length - 1][dungeon[0].length - 1];
    }
}

