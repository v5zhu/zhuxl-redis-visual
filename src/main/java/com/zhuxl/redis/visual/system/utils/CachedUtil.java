//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

import redis.clients.jedis.Jedis;

public class CachedUtil {
    private static String databaseType = "Memcached";
    private static String userName = "root";
    private static String passwrod = "123456";
    private static String port = "11211";
    private static String ip = "127.0.0.1";
    private static String timeout = "1000";
    private static Jedis jedis;

    public CachedUtil() {
    }

    public void setup() {
        jedis = new Jedis(ip, Integer.parseInt(port), Integer.parseInt(timeout));
        jedis.auth(userName);
    }

    public static boolean testConnection(String databaseType2, String databaseName2, String ip2, String port2, String user2, String pass2) {
        databaseType2.equals("Memcached");
        if (databaseType2.equals("redis")) {
            jedis = new Jedis(ip2, Integer.parseInt(port2), Integer.parseInt(timeout));
            return jedis != null;
        } else {
            return true;
        }
    }
}
