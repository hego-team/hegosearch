package com.bytedance.hego.entity;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private float time;                  // 查询用时
    private int total;                   // 查到的结果总数
    private List<Document> documents;    // 查询结果
}