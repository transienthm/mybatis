package com.meituan.redis;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.Set;

public class TestApi {
    
    @Value("${redis.ip}")
    String host;
    
    @Value("${redis.port}")
    int port;
    
    public static void main(String[] args) {
        Jedis jedis = new Jedis("39.104.114.86", 6379);
        jedis.set("k1", new Date(System.currentTimeMillis()).toString());

        System.out.println(jedis.get("k1"));
        Set<String> keys = jedis.keys("*");
        for (String s : keys) {
            System.out.println(s);
        }
    }
}
