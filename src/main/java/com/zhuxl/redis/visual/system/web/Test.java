//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.web;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public Test() {
    }

    public static void main(String[] args) {
        MemCachedClient client = new MemCachedClient();
        String[] addr = new String[]{"127.0.0.1:11211"};
        Integer[] weights = new Integer[]{3};
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(addr);
        pool.setWeights(weights);
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(200);
        pool.setMaxIdle(900000L);
        pool.setMaintSleep(30L);
        pool.setNagle(false);
        pool.setSocketTO(30);
        pool.setSocketConnectTO(0);
        pool.initialize();
        client.set("test1", "11111111111111");
        client.set("test2", "test2");
        client.set("test3", "33333333");
        client.set("sendTimeTest", new Date());
        client.set("sendTimeT", "2017-06-11 19:00:00");
        ConcurrentHashMap mm = new ConcurrentHashMap();
        mm.put("aa", "aaaa");
        mm.put("bb", "bbbb");
        client.set("ConcurrentHashMap", mm);
        Date date = new Date(2000000L);
        client.set("test1", "test1", date);
        String str = (String) client.get("test1");
        System.out.println("xxxx = " + str);
    }
}
