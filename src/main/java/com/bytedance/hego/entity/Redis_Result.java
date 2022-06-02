package com.bytedance.hego.entity;

import lombok.Data;

/**
 * @author swb
 * @create 2022-05-29 17:20
 */
@Data
public class Redis_Result {
    private float time;                  // 查询用时
    private int total;                   // 查到的结果总数
}
