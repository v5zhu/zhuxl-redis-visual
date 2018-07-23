//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    public DBUtil() {
    }

    public boolean do_update(String sql) throws Exception {
        try {
            String dbPath = Constants.DATABASEPATH + "servlet-context.xml";
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            Statement stat = conn.createStatement();
            stat.executeUpdate(sql);
            conn.close();
            return true;
        } catch (Exception var5) {
            System.out.println("无法找到配置文件，软件安装路径不能包含中文,空隔！");
            var5.printStackTrace();
            return false;
        }
    }

    public List executeQuery(String sql) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List rslist = new ArrayList();
        StringBuffer sqlPage = new StringBuffer(sql + " ");
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String dbPath = Constants.DATABASEPATH + "servlet-context.xml";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            pstmt = conn.prepareStatement(sqlPage.toString());
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            while (rs.next()) {
                Map row = new HashMap(numberOfColumns);

                for (int i = 1; i <= numberOfColumns; ++i) {
                    Object o = rs.getObject(i);
                    if ("Date".equalsIgnoreCase(rsmd.getColumnTypeName(i)) && o != null) {
                        row.put(rsmd.getColumnName(i), formatter.format(o));
                    } else {
                        row.put(rsmd.getColumnName(i), o == null ? "" : o);
                    }
                }

                rslist.add(row);
            }
        } catch (Exception var24) {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException var23) {
                ;
            }
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException var22) {
                ;
            }

        }

        return rslist;
    }

    public Object setinsertData(String sql) throws Exception {
        Statement stmt = null;
        String flagOper = "0";
        Connection conn = null;

        Integer var8;
        try {
            String dbPath = Constants.DATABASEPATH + "servlet-context.xml";
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            stmt = conn.createStatement();
            var8 = stmt.executeUpdate(sql);
        } catch (SQLException var15) {
            flagOper = "1";
            throw new Exception(var15.getMessage());
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException var14) {
                throw new Exception(var14.getMessage());
            }
        }

        return var8;
    }

    public boolean initDbConfig() {
        boolean bl = true;
        DBUtil db1 = new DBUtil();
        List<Map<String, Object>> list = db1.executeQuery(" select * from  treesoft_config ");
        Map<String, Object> map = (Map) list.get(0);
        Constants.DATABASETYPE = (String) map.get("databaseType");
        Constants.DRIVER = (String) map.get("driver");
        Constants.URL = (String) map.get("url");
        Constants.DATABASENAME = (String) map.get("databaseName");
        Constants.USERNAME = (String) map.get("userName");
        Constants.PASSWROD = (String) map.get("passwrod");
        Constants.PORT = (String) map.get("port");
        Constants.IP = (String) map.get("ip");
        return bl;
    }

    public List<Map<String, Object>> getConfigList() {
        DBUtil db1 = new DBUtil();
        List<Map<String, Object>> list = db1.executeQuery(" select * from  treesoft_config ");
        return list;
    }

    public List<Map<String, Object>> getPersonList() {
        DBUtil db1 = new DBUtil();
        List<Map<String, Object>> list = db1.executeQuery(" select * from  treesoft_users ");
        return list;
    }
}
