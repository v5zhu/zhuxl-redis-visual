//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

public final class Constants {
    public static String DATABASETYPE = "Redis";
    public static String DRIVER = "com.mysql.jdbc.Driver";
    public static String DATABASENAME = "mysql";
    public static String URL;
    public static String USERNAME;
    public static String PASSWROD;
    public static String PORT;
    public static String IP;
    public static String DATABASEPATH;

    static {
        URL = "jdbc:mysql://localhost:3306/" + DATABASENAME;
        USERNAME = "root";
        PASSWROD = "123456";
        PORT = "11211";
        IP = "127.0.0.1";
        DATABASEPATH = "";
    }

    public Constants() {
    }
}
