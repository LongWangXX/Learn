package com.lw.data.demo;

import com.alibaba.excel.annotation.ExcelProperty;

public class DemoData {
    // 根据Excel中指定列名或列的索引读取
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private String salary;
    @ExcelProperty(index = 0)
    private String hireDate;
    // lombok 会生成getter/setter方法

    @Override
    public String toString() {
        return "DemoData{" +
                "name='" + name + '\'' +
                ", hireDate='" + hireDate + '\'' +
                ", salary='" + salary + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}

