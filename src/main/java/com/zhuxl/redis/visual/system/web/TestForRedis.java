//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.web;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ConcurrentHashMap;

public class TestForRedis {
    public TestForRedis() {
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("admin");
        jedis.set("name", "xinxin");
        System.out.println(jedis.get("name"));
        ConcurrentHashMap mm = new ConcurrentHashMap();
        mm.put("aa", "aaaa");
        mm.put("bb", "bbbb");
    }
}
