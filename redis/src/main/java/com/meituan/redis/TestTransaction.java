package com.meituan.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

public class TestTransaction {
    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("39.104.114.86", 6379);
        int balance;// 可用余额
        int debt;// 欠额
        int amtToSubtract = 10;// 实刷额度

        balance = Integer.parseInt(jedis.get("balance"));
        System.out.println("before watch===========> balance = " + balance);

        jedis.watch("balance");
//        jedis.set("balance","5");//此句不该出现，讲课方便。模拟其他程序已经修改了该条目
        Thread.sleep(7000);
        balance = Integer.parseInt(jedis.get("balance"));
        System.out.println("before tx===========> balance = " + balance);
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("modify");
            return false;
        } else {
            System.out.println("***********transaction");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("debt", amtToSubtract);
            List<Object> exec = transaction.exec();
            System.out.println("============exec list = " + exec);
            for (Object o : exec) {
                System.out.println(o);
            }
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));

            System.out.println("*******" + balance);
            System.out.println("*******" + debt);
            return true;
        }
    }

    /**
     * 通俗点讲，watch命令就是标记一个键，如果标记了一个键， 在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中
     * 重新再尝试一次。
     * 首先标记了键balance，然后检查余额是否足够，不足就取消标记，并不做扣减； 足够的话，就启动事务进行更新操作，
     * 如果在此期间键balance被其它人修改， 那在提交事务（执行exec）时就会报错， 程序中通常可以捕获这类错误再重新执行一次，直到成功。
     */
    public static void main(String[] args) throws InterruptedException {
        TestTransaction test = new TestTransaction();
        boolean retValue = test.transMethod();
        System.out.println("main retValue-------: " + retValue);
    }

}
