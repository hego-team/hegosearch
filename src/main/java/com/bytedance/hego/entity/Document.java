package com.bytedance.hego.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * leveldb中获取的doc id和对应的tf-idf score
 */
@Data
public class Document {

    @JSONField(name = "docId", deserialize=false)
    private Integer docId;

    private String content;
    private String image;
}
