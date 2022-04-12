package com.lw.data.dp;

public class DP07_买股票问题 {
    public static void main(String[] args) {
        int[] a = {0, 7, 1, 5, 3, 6, 4};
        System.out.println(maxProfit2(a));
    }

    public static int maxProfit(int[] prices) {
        int dp[][][] = new int[prices.length][2][2];
        //dp[i][j][k] 第i天，j=0，表示未持股，j=1表示持股，k表示卖出k次的最大值。
        dp[0][0][0] = 0;
        dp[0][0][1] = 0;

        dp[0][1][0] = -prices[1];
        dp[0][1][1] = -prices[1];
        for (int i = 1; i < prices.length; i++) {
            //今日不持股且卖出过0次；
            dp[i][0][0] = 0;
            //今日不持股，且卖出过一次股票，表示可能今天把股票卖出去，或者前一天卖出
            dp[i][0][1] = Math.max(dp[i - 1][1][0] + prices[i], dp[i - 1][0][1]);
            //今天持股，且卖出过0次；，表示有可能是今天把股票买进来的，也可能是前一天把股票买进来的
            dp[i][1][0] = Math.max(dp[i - 1][0][0] - prices[i], dp[i - 1][1][0]);

            dp[i][1][1] = 0;
        }

        return Math.max(dp[prices.length - 1][0][0], dp[prices.length - 1][0][1]);
    }

    public static int maxProfit2(int[] prices) {
        int dp[][][] = new int[prices.length + 1][2][prices.length + 1];
        //dp[i][j][k] 第i天，j=0，表示未持股，j=1表示持股，k表示卖出k次的最大值。
        dp[1][0][0] = 0;
        dp[1][0][1] = 0;

        dp[1][1][0] = -prices[1];
        dp[1][1][1] =  -prices[1];
        for (int i = 2; i < prices.length + 1; i++) {
            //今日不持股且卖出过0次；
//            dp[i][0][0] = 0;
//            dp[i][1][0]
            for (int j = 0; j <= i; j++) {
                dp[i][1][j] = Math.max(dp[i - 1][0][j] - prices[i], dp[i - 1][1][j]);
                if (j == 0) dp[i][0][j] = 0;
                else
                    dp[i][0][j] = Math.max(dp[i][1][j - 1] + prices[i], dp[i - 1][0][j]);
                //今天持股，且卖出过0次；，表示有可能是今天把股票买进来的，也可能是前一天把股票买进来的
            }
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < prices.length + 1; i++) {
            if (max < dp[prices.length][0][i]) max = dp[prices.length][0][i];
        }
        return max;

    }

}
