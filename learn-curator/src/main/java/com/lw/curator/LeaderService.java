package com.lw.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class LeaderService {
    private String zkAddr;
    private String path;
    private CuratorFramework client;
    LeaderLatch leaderLatch;

    public LeaderService(String zkAddr, String path) {
        this.zkAddr = zkAddr;
        this.path = path;
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                .connectString(this.zkAddr)
                .sessionTimeoutMs(2000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        leaderLatch = new LeaderLatch(client, this.path);
    }

    /**
     * 开始选举
     */
    public void start(LeaderLatchListener listener) throws Exception {
        leaderLatch.start();
        leaderLatch.addListener(listener);
    }

    /**
     * 判断自己是否是leader
     *
     * @return
     */
    public boolean isLeader() {
        return leaderLatch.hasLeadership();
    }

    /**
     * 等待自己选举为leader
     *
     * @throws EOFException
     * @throws InterruptedException
     */
    public void await() throws EOFException, InterruptedException {
        leaderLatch.await();
    }

    /**
     * 等待自己选举为leader
     *
     * @throws EOFException
     * @throws InterruptedException
     */
    public void await(long timeout, TimeUnit unit) throws InterruptedException {
        leaderLatch.await(timeout, unit);
    }

    public void stop() throws IOException {
        leaderLatch.close();
    }

    /**
     * 关闭连接
     *
     * @throws EOFException
     * @throws InterruptedException
     */
    public void close() throws IOException {
        leaderLatch.close();
        client.close();
    }


    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            LeaderService leaderService = new LeaderService("hadoop102:2181", "/leader");
            try {
                leaderService.start(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        System.out.println(Thread.currentThread().getName() + "is leader");
                    }

                    @Override
                    public void notLeader() {
                        System.out.println(Thread.currentThread().getName() + "is notLeader");

                    }

                });
                Thread.sleep(1000L);
                leaderService.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            LeaderService leaderService = new LeaderService("hadoop102:2181", "/leader");
            try {
                leaderService.start(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        System.out.println(Thread.currentThread().getName() + "is leader");
                    }

                    @Override
                    public void notLeader() {
                        System.out.println(Thread.currentThread().getName() + "is notLeader");

                    }
                });
                Thread.sleep(3000L);
                leaderService.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        Thread.sleep(10000L);

    }
}
