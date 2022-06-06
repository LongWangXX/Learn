package com.lw.data.demo;

import com.alibaba.excel.annotation.ExcelProperty;

import java.sql.Timestamp;
import java.util.Date;

public class TIsocAsset {
    @ExcelProperty("表头1")
    private String id;
    @ExcelProperty("表头2")
    private String ip;
    private Date createTime;

    @Override
    public String toString() {
        return "TIsocAsset{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
