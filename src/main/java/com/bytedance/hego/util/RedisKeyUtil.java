package com.bytedance.hego.util;

public class RedisKeyUtil {

    private static final String SPLIT = "::";
    private static final String PREFIX_RESULT = "result";

    // 保存某次query返回的docs
    public static String getResultKey(String query, String filter, int current, int limit) {
        return PREFIX_RESULT + SPLIT + query + SPLIT + filter + SPLIT + current + SPLIT + limit;
    }
}

