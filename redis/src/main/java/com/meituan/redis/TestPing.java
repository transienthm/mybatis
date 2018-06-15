package com.meituan.redis;

import redis.clients.jedis.Jedis;

public class TestPing {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("39.104.114.86", 6379);
        System.out.println(jedis.ping());
    }
}
