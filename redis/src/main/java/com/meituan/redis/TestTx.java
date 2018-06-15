package com.meituan.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Iterator;
import java.util.Set;

public class TestTx {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("39.104.114.86", 6379);
        jedis.flushDB();
        Transaction transaction = jedis.multi();
        transaction.set("k4", "v4");
        transaction.set("k5", "v5");
//        transaction.exec();
        transaction.discard();
        Set<String> keys = jedis.keys("*");
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }
}
