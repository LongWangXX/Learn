package com.lw.data.dp;

/**
 * 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
 * <p>
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * <p>
 * 判断你是否能够到达最后一个下标。
 * <p>
 * 输入：nums = [2,3,1,1,4]
 * 输出：true
 * 解释：可以先跳 1 步，从下标 0 到达下标 1, 然后再从下标 1 跳 3 步到达最后一个下标。
 * 输入：nums = [3,2,1,0,4]
 * 输出：false
 * 解释：无论怎样，总会到达下标为 3 的位置。但该下标的最大跳跃长度是 0 ， 所以永远不可能到达最后一个下标。
 * <p>
 * 提示：
 * <p>
 * 1 <= nums.length <= 3 * 104
 * 0 <= nums[i] <= 105
 * <p>
 * <p>
 * 题解：贪心+暴力递归+备忘录减脂
 * 从第[0]个位置开始跳跃，从步数大的开始跳，如果跳到最大的一步，如果最大的一步无法到达终点则记录dp[0+最大的一步]=false；
 * 能到就直接返回false；
 * <p>
 * <p>
 * 动态规划思想来做
 * <p>
 * 求可行性。dp[i]表示是否能到达i号位置
 * 怎么判断是否能达到i呢。判断dp[i-k]等于true并且i-k+num[i-k]>=i;i-k属于0~i；
 * 出事状态dp[0] = true;
 */
public class DP04_跳跃游戏 {
    public static void main(String[] args) {
        int W[] = {2, 0, 0};
        System.out.println(canJump2(W));
    }

    public static boolean canJump(int[] nums) {
        if (nums.length == 1) return true;
        boolean dp[] = new boolean[nums.length];
        for (int i = 0; i < dp.length; i++) {
            dp[i] = true;
        }
        return dd(nums, 0, 0, dp);
    }

    public static boolean dd(int[] nums, int i, int j, boolean dp[]) {
        int newIndex = i + j;
        if (!dp[newIndex]) {
            return false;
        }
        for (int k = nums[newIndex]; k > 0; k--) {
            if (newIndex + k >= nums.length - 1) return true;
            else {
                boolean dd = dd(nums, newIndex, k, dp);
                if (dd) return true;
            }
        }
        dp[newIndex] = false;
        return false;
    }

    public static boolean canJump2(int[] nums) {
        boolean[] dp = new boolean[nums.length];
        dp[0] = true;
        for (int i = 1; i < nums.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (dp[j] && nums[j] + j >= i) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[nums.length - 1];
    }
}
