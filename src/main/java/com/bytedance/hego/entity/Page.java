package com.bytedance.hego.entity;

import lombok.Data;

/**
 * 封装分页相关信息
 */
@Data
public class Page {

    // 当前页码
    private int current = 1;
    // 每页显示数据个数上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows = 0;

    // 当前页的第一条数据的index
    private int start = 0;
    // 当前页的最后一条数据的index
    private int end = 0;
    // 总页数
    private int total = 1;
    // 起始页码
    private int from = 1;
    // 结束页码
    private int to = 1;


    /**
     * 获取当前页的起始位置(在rankIds中的index)
     *
     * @return
     */
    public int getStart() {
        // current * limit - limit
        return Math.max(0, (current - 1) * limit);
    }

    /**
     * 获取当前页的结束位置
     *
     * @return
     */
    public int getEnd() {
        int rows = getRows();
        return Math.min(current * limit, rows);
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        // rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取起始页码
     *
     * @return
     */
    public int getFrom() {
        int from = current - 5;
        return Math.max(from, 1);
    }

    /**
     * 获取结束页码
     *
     * @return
     */
    public int getTo() {
        int to = current + 5;
        int total = getTotal();
        return Math.min(to, total);
    }

}
