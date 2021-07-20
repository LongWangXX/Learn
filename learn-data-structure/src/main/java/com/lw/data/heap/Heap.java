package com.lw.data.heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 堆
 * <p>
 * 建立堆 时间复杂度 0（n） n代表元素个数。
 * 堆排序的时间复杂度 O(nlogn)
 * @param <T>
 */
public class Heap<T extends Comparable> {
    private ArrayList<T> data;//用于存储数据
    private int n;//n表示容量
    private int count = 0;//cnt表示当前堆中有多少个元素
    private boolean isSmall;

    public Heap(int capacity, boolean isSmall) {
        data = new ArrayList<>();
        this.n = capacity;
        this.isSmall = isSmall;
    }

    /**
     * 向堆中插入一个元素
     */
    public boolean insert(T e) {
        if (count >= n) return false;
        //添加元素到data
        data.add(count, e);
        //进行堆化
        heapifyLowToHigh(count++, data);
        return true;
    }

    /**
     * 堆化，从低到高
     *
     * @param index
     */
    private void heapifyLowToHigh(int index, List<T> list) {
        int parentIndex = (index - 1) / 2;
        while (parentIndex >= 0 && parentIndex != index) {
            T data = this.data.get(index);
            T parentData = this.data.get(parentIndex);
            //如果子元素比父节点大并且是大顶堆，那么交换两个元素
            if (data.compareTo(parentData) >= 0) {
                if (!isSmall) {
                    swap(index, parentIndex, list);
                } else {
                    break;
                }
            } else {
                if (!isSmall) {
                    break;
                } else {
                    swap(index, parentIndex, list);
                }
            }
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    /**
     * 堆化从高到低
     *
     * @param index
     */
    private void heapifyHighToLow(int index, int c, List<T> list, boolean isSmall) {
        int leftSonIndex = index * 2 + 1;
        int rightSonIndex = index * 2 + 2;
        T leftSonData = null;
        T rightSonData = null;
        int targetIndex;
        while (leftSonIndex < c || rightSonIndex < c) {
            targetIndex = index;
            T currentData = list.get(index);
            if (leftSonIndex < c)
                leftSonData = list.get(leftSonIndex);
            if (rightSonIndex < c)
                rightSonData = list.get(rightSonIndex);
            if (!isSmall) {
                if (leftSonData != null && currentData.compareTo(leftSonData) < 0) {
                    targetIndex = leftSonIndex;
                }
                if (rightSonData != null && leftSonData.compareTo(rightSonData) < 0) {
                    targetIndex = rightSonIndex;
                }
                if (index == targetIndex) {
                    break;
                } else {
                    swap(index, targetIndex, list);
                    index = targetIndex;
                    leftSonIndex = index * 2 + 1;
                    rightSonIndex = index * 2 + 2;
                }
            } else {
                if (leftSonData != null && currentData.compareTo(leftSonData) > 0) {
                    targetIndex = leftSonIndex;
                }
                if (rightSonData != null && leftSonData.compareTo(rightSonData) > 0) {
                    targetIndex = rightSonIndex;
                }
                if (index == targetIndex) {
                    break;
                } else {
                    swap(index, targetIndex, list);
                    index = targetIndex;
                    leftSonIndex = index * 2 + 1;
                    rightSonIndex = index * 2 + 2;
                }
            }

        }
    }

    /**
     * 获取堆顶元素
     *
     * @return
     */
    public T getHeapTopData() {
        if (count == 0) return null;
        return data.get(0);
    }

    /**
     * 删除堆顶元素
     *
     * @return
     */
    public boolean deleteHeapTopData() {
        if (count == 0) return false;
        //1.把最后一个元素覆盖堆顶元素，然后从上往下堆化
        T t = data.get(count - 1);
        data.remove(count - 1);
        count--;
        data.remove(0);
        data.add(0, t);
        heapifyHighToLow(0, count, data, isSmall);
        return true;
    }

    private void swap(int from, int target, List<T> list) {
        T tmp = list.get(from);
        list.set(from, list.get(target));
        list.set(target, tmp);
    }

    @Override
    public String toString() {
        return "Heap{" +
                "data=" + data +
                '}';
    }

    /**
     * 堆排序
     *
     * @param isAsc
     * @return
     */
    public ArrayList<T> sort(boolean isAsc) {
        ArrayList<T> arrayList = new ArrayList<>();
        arrayList.addAll(data);
        int c = count - 1;
        while (c >= 1) {
            swap(0, c, arrayList);
            c--;
            heapifyHighToLow(0, c, arrayList, isSmall);
        }
        if (isAsc) {
            if (!isSmall) return arrayList;
            else {
                Collections.reverse(arrayList);
                return arrayList;
            }
        } else {
            if (isSmall) return arrayList;
            else {
                Collections.reverse(arrayList);
                return arrayList;
            }
        }
    }

    public static void main(String[] args) {
        Heap<Integer> heap = new Heap<Integer>(100, false);
        heap.insert(10);
        heap.insert(3);
        heap.insert(2);
        heap.insert(22);
        heap.insert(21);
        heap.insert(52);
        heap.insert(212);
        heap.insert(1001);
        heap.insert(102);
        heap.insert(1);
        heap.insert(0);
        System.out.println(heap);

        heap.deleteHeapTopData();
        System.out.println(heap);
        System.out.println(heap.sort(true));
    }
}
