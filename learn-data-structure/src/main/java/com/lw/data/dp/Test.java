package com.lw.data.dp;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        int a[] = new int[]{-1, 0, 1, 2, -1, -4};
        System.out.println(threeSum(a));
    }

    public static List<List<Integer>> threeSum(int[] nums) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            list.add(nums[i]);
        }
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        HashSet<String> strings = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                int sum = list.get(i) + list.get(j);
                int taget = -sum;
                System.out.println(sum);
                int start = j + 1;
                int end = nums.length - 1;
                int mid;
                int k = -1;
                while (start <= end) {
                    mid = (start + end) / 2;
                    if (list.get(mid) == taget) {
                        k = mid;
                        break;
                    } else if (list.get(mid) > taget) {
                        end = mid - 1;
                    } else {
                        start = mid + 1;
                    }
                }
                if (k != -1) {
                    strings.add(list.get(i) + "_" + list.get(j) + "_" + list.get(k));
                }
            }
        }
        System.out.println(strings);
        System.out.println(list);
        List<List<Integer>> result = new ArrayList<>();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            String[] s = next.split("_");
            ArrayList<Integer> integers = new ArrayList<>();
            result.add(integers);
            for (String s1 : s) {
                integers.add(Integer.parseInt(s1));
            }
        }
        return result;
    }
}
