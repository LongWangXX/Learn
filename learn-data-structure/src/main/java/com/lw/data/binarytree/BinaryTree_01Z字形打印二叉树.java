package com.lw.data.binarytree;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class BinaryTree_01Z字形打印二叉树 {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TreeNode treeNode = new TreeNode(1);
        TreeNode treeNodeL = new TreeNode(2);
        TreeNode treeNodeR = new TreeNode(3);
        TreeNode treeNodeRL = new TreeNode(4);
        treeNode.left = treeNodeL;
        treeNode.right = treeNodeR;
        treeNodeR.left = treeNodeRL;
        System.out.println(levelOrder(treeNode));

    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        ArrayList<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        try {
            ArrayList<TreeNode> tmp = new ArrayList<>();
            LinkedBlockingQueue<TreeNode> queue = new LinkedBlockingQueue<TreeNode>();
            queue.add(root);
            while (!queue.isEmpty()) {
                ArrayList<Integer> list = new ArrayList<>();
                while (!queue.isEmpty()) {
                    TreeNode peek = queue.take();
                    list.add(peek.val);
                    if (peek.left != null) tmp.add(peek.left);
                    if (peek.right != null) tmp.add(peek.right);
                }
                tmp.forEach(node -> queue.add(node));
                tmp.clear();
                result.add(list);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < result.size(); i++) {
            if (i % 2 == 1) {
                Collections.reverse(result.get(i));
            }
        }
        return result;
    }
}
