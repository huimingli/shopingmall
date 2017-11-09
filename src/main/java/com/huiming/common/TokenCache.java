package com.huiming.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/*
 * @author:15737
 * @createtime:2017/11/08 16:11
 * @desc:
*/
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    //lru算法，清除最少引用
    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认数据加载实现，如果get取值没有对应值，就调用这个方法进行加载
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        loadingCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = loadingCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localcache gets error", e);
        }
        return null;
    }
}
