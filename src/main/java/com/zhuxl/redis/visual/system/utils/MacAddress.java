//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class MacAddress {
    public MacAddress() {
    }

    public static String getMac() {
        String mac = "";

        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

            while (enumeration.hasMoreElements()) {
                StringBuffer stringBuffer = new StringBuffer();
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                if (networkInterface != null) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    if (bytes != null) {
                        for (int i = 0; i < bytes.length; ++i) {
                            if (i != 0) {
                                stringBuffer.append("T");
                            }

                            int tmp = bytes[i] & 255;
                            String str = Integer.toHexString(tmp);
                            if (str.length() == 1) {
                                stringBuffer.append("0" + str);
                            } else {
                                stringBuffer.append(str);
                            }
                        }

                        mac = stringBuffer.toString().toUpperCase();
                    }
                }

                mac = mac.trim();
                if (!mac.equals("")) {
                    break;
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return mac;
    }
}
