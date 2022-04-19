package com.lw.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZkLock {
    private String zkAddr;
    private String lockPath;
    private InterProcessLock lock;
    private static ZkLock zkLock = null;

    public ZkLock(String zkAddr, String lockPath) {
        this.zkAddr = zkAddr;
        this.lockPath = lockPath;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkAddr)
                .sessionTimeoutMs(2000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        this.lock = new InterProcessMutex(client, lockPath);
    }

    public static ZkLock getInstance(String zkAddr, String lockPath) {
        if (zkLock == null) {
            //5.加锁防止多线程并发的问题
            synchronized (ZkLock.class) {
                //6.第二重判断
                if (zkLock == null) {
                    //7.创建对象，该对象是全局只有一个实例的单例对象
                    zkLock = new ZkLock(zkAddr, lockPath);
                }

            }
        }
        return zkLock;
    }

    /**
     * 获取锁。如果当前已经被其他进程获取锁了。那么就线程等待
     */
    public void lock() throws Exception {
        lock.acquire();
    }

    /**
     * 释放zk的锁
     */
    public void release() throws Exception {
        lock.release();

    }

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                ZkLock zkLock = new ZkLock("hadoop102:2181", "/roma1");
                zkLock.lock();
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000 * 10);
                System.out.println(Thread.currentThread().getName() + " release lock");

                zkLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "Thread1").start();
        new Thread(() -> {
            try {
                ZkLock zkLock = new ZkLock("hadoop102:2181", "/roma");
                zkLock.lock();
                System.out.println(Thread.currentThread().getName() + " get lock");
                Thread.sleep(1000 * 10);
                System.out.println(Thread.currentThread().getName() + " release lock");

                zkLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, "Thread2").start();


    }
}
