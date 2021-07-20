package com.lw.data.heap;

/**
 * 优先队列
 * @param <T>
 */
public class PriorityQueue<T extends Comparable> {
    Heap<T> heap;

    public PriorityQueue() {
        heap = new Heap<>(Integer.MAX_VALUE, false);
    }

    /**
     * 入队
     *
     * @param t
     */
    private void put(T t) {
        heap.insert(t);
    }

    private T take() {
        T heapTopData = heap.getHeapTopData();
        heap.deleteHeapTopData();
        return heapTopData;
    }

}
