//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.common.utils;

import com.google.common.collect.Maps;
import java.util.Map;

public class Global {
    private static Map<String, String> map = Maps.newHashMap();
    private static PropertiesLoader propertiesLoader = new PropertiesLoader(new String[]{"application.properties"});

    public Global() {
    }

    public static String getConfig(String key) {
        String value = (String)map.get(key);
        if (value == null) {
            value = propertiesLoader.getProperty(key);
            map.put(key, value);
        }

        return value;
    }

    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }
}
