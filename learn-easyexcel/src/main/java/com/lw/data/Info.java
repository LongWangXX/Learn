package com.lw.data;

public class Info {
    long sumCnt;
    long acceptCnt;
    long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Info(long sumCnt, long acceptCnt, long time) {
        this.sumCnt = sumCnt;
        this.acceptCnt = acceptCnt;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Info{" +
                "sumCnt=" + sumCnt +
                ", acceptCnt=" + acceptCnt +
                ", time=" + time +
                '}';
    }

    public Info() {
    }

    public long getSumCnt() {
        return sumCnt;
    }

    public void setSumCnt(long sumCnt) {
        this.sumCnt = sumCnt;
    }

    public long getAcceptCnt() {
        return acceptCnt;
    }

    public void setAcceptCnt(long acceptCnt) {
        this.acceptCnt = acceptCnt;
    }
}