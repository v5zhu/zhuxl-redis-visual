//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxl.redis.visual.common.persistence.Page;
import com.zhuxl.redis.visual.common.utils.DateUtils;
import com.zhuxl.redis.visual.system.entity.Config;
import com.zhuxl.redis.visual.system.entity.NotSqlEntity;
import com.zhuxl.redis.visual.system.entity.Person;
import com.zhuxl.redis.visual.system.utils.*;
import com.zhuxl.redis.visual.system.web.TempDto;
import org.apache.commons.io.FileUtils;

import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class PermissionDao {
    public PermissionDao() {
    }

    public List<Map<String, Object>> getAllDataBase() throws Exception {
        String sql = " select * from  information_schema.schemata  ";
        new DBUtil2(Constants.DATABASENAME);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        List<Map<String, Object>> list2 = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("SCHEMA_NAME", Constants.DATABASENAME);
        list2.add(map);

        for (int i = 0; i < list.size(); ++i) {
            Map<String, Object> map2 = (Map) list.get(i);
            String schema_name = (String) map2.get("SCHEMA_NAME");
            if (!schema_name.equals(Constants.DATABASENAME)) {
                list2.add(map2);
            }
        }

        return list2;
    }

    public List<Map<String, Object>> getAllTables(String dbName) throws Exception {
        new DBUtil2(dbName);
        String sql = " select TABLE_NAME from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE' ";
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViews(String dbName) throws Exception {
        String sql = " select TABLE_NAME   from information_schema.TABLES where table_schema='" + dbName + "' and table_type='VIEW' ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllFuntion(String dbName) throws Exception {
        String sql = " select ROUTINE_NAME   from information_schema.ROUTINES where routine_schema='" + dbName + "' ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns(String dbName, String tableName) throws Exception {
        String sql = "select * from  " + dbName + "." + tableName + " limit 1 ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForColumnOnly(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3(String dbName, String tableName) throws Exception {
        String sql = " select column_name as TREESOFTPRIMARYKEY, COLUMN_NAME,COLUMN_TYPE , DATA_TYPE ,CHARACTER_MAXIMUM_LENGTH,IS_NULLABLE, COLUMN_KEY, COLUMN_COMMENT  from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + dbName + "'  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        new DBUtil2(dbName);
        List<Map<String, Object>> list3 = this.getPrimaryKeyss(dbName, tableName);
        String tem = "";

        Map map0;
        for (Iterator var13 = list3.iterator(); var13.hasNext(); tem = tem + map0.get("column_name") + ",") {
            map0 = (Map) var13.next();
        }

        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }

        String sql = "select count(*) from  " + dbName + "." + tableName;
        String sql2 = "";
        if (orderBy != null && !orderBy.equals("")) {
            sql2 = "select  *  from  " + dbName + "." + tableName + " order by " + orderBy + " " + order + "  LIMIT " + limitFrom + "," + pageSize;
        } else {
            sql2 = "select  *  from  " + dbName + "." + tableName + "  LIMIT " + limitFrom + "," + pageSize;
        }

        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCount(sql);
        List<Map<String, Object>> columns = this.getTableColumns(dbName, tableName);
        List<Map<String, Object>> tempList = new ArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);

        HashMap map2;
        for (Iterator var21 = columns.iterator(); var21.hasNext(); tempList.add(map2)) {
            Map<String, Object> map = (Map) var21.next();
            map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            map2.put("editor", "text");
            if (map.get("data_type").equals("DATETIME")) {
                map2.put("editor", "datebox");
            } else if (!map.get("data_type").equals("INT") && !map.get("data_type").equals("SMALLINT") && !map.get("data_type").equals("TINYINT")) {
                if (map.get("data_type").equals("DOUBLE")) {
                    map2.put("editor", "numberbox");
                } else {
                    map2.put("editor", "text");
                }
            } else {
                map2.put("editor", "numberbox");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }

    public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = " select * from ( " + sql + " ) tab  LIMIT " + limitFrom + "," + pageSize;
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCount2(sql);
        List<Map<String, Object>> columns = this.executeSqlForColumns(sql, dbName);
        List<Map<String, Object>> tempList = new ArrayList();

        HashMap map2;
        for (Iterator var14 = columns.iterator(); var14.hasNext(); tempList.add(map2)) {
            Map<String, Object> map = (Map) var14.next();
            map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            if (map.get("data_type").equals("DATETIME")) {
                map2.put("editor", "datebox");
            } else if (!map.get("data_type").equals("INT") && !map.get("data_type").equals("SMALLINT") && !map.get("data_type").equals("TINYINT")) {
                if (map.get("data_type").equals("DOUBLE")) {
                    map2.put("editor", "numberbox");
                } else {
                    map2.put("editor", "text");
                }
            } else {
                map2.put("editor", "numberbox");
            }
        }

        String primaryKey = "";
        String tableName = "";
        String temp = "";
        if (this.checkSqlIsOneTableForMySql(dbName, sql)) {
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(sql);
            temp = m.replaceAll(" ");
            temp = temp.toLowerCase();

            String tem;
            for (int y = 14; y < temp.length(); ++y) {
                tem = String.valueOf(temp.charAt(y));
                if (tem.equals(" ")) {
                    break;
                }

                tableName = tableName + tem;
            }

            List<Map<String, Object>> list3 = this.getPrimaryKeyss(dbName, tableName);
            tem = "";

            Map map3;
            for (Iterator var21 = list3.iterator(); var21.hasNext(); tem = tem + map3.get("column_name") + ",") {
                map3 = (Map) var21.next();
            }

            if (!tem.equals("")) {
                primaryKey = tem.substring(0, tem.length() - 1);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        page.setTableName(tableName);
        return page;
    }

    public boolean checkSqlIsOneTableForMySql(String dbName, String sql) {
        String temp = "";
        String tableName = "";

        try {
            new DBUtil2(dbName);
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(sql);
            temp = m.replaceAll(" ");
            temp = temp.toLowerCase();
            if (temp.indexOf("select * from") >= 0) {
                for (int y = 14; y < temp.length(); ++y) {
                    String c = String.valueOf(temp.charAt(y));
                    if (c.equals(" ")) {
                        break;
                    }

                    tableName = tableName + c;
                }

                String sql2 = " select TABLE_NAME from information_schema.TABLES where table_schema='" + dbName + "' and table_type='BASE TABLE' and  TABLE_NAME ='" + tableName + "'";
                List<Map<String, Object>> list = DBUtil2.queryForList(sql);
                if (list.size() > 0) {
                    return true;
                }
            }

            return false;
        } catch (Exception var10) {
            return false;
        }
    }

    public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName) throws Exception {
        String sql2 = " select * from  ( " + sql + " ) tab  limit 1 ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.executeSqlForColumns(sql2);
        return list;
    }

    public boolean saveSearchHistory(String name, String sql, String dbName) {
        DBUtil db = new DBUtil();
        String insertSQL = "insert into  treesoft_searchHistory ( createdate, sqls, name, database,user_id) values (  datetime('now') ,'" + sql + "','" + name + "','" + dbName + "','')";

        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception var7) {
            System.out.println(var7.getMessage());
            return false;
        }
    }

    public boolean updateSearchHistory(String id, String name, String sql, String dbName) {
        DBUtil db = new DBUtil();
        String insertSQL = "update  treesoft_searchHistory set createdate= datetime('now') , sqls='" + sql + "', name = '" + name + "', database='" + dbName + "' where id='" + id + "' ";

        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception var8) {
            System.out.println(var8.getMessage());
            return false;
        }
    }

    public boolean deleteSearchHistory(String id) {
        DBUtil db = new DBUtil();
        String insertSQL = "delete  from  treesoft_searchHistory  where id='" + id + "' ";

        try {
            db.do_update(insertSQL);
            return true;
        } catch (Exception var5) {
            System.out.println(var5.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> selectSearchHistory() {
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_searchHistory ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public boolean configUpdate(Config config) throws Exception {
        DBUtil db = new DBUtil();
        String id = config.getId();
        String sql = "";
        String isdefault = config.getIsdefault();
        if (isdefault == null) {
            isdefault = "0";
        }

        if (!id.equals("")) {
            sql = " update treesoft_config  set databaseType='" + config.getDatabaseType() + "' ," + "databaseName='" + config.getDatabaseName() + "' ," + "userName='" + config.getUserName() + "', " + "passwrod='" + config.getPassword() + "', " + "isdefault='" + isdefault + "', " + "port='" + config.getPort() + "', " + "ip='" + config.getIp() + "', " + "name='" + config.getName() + "', " + "url='" + config.getUrl() + "'  where id='" + id + "'";
        } else {
            sql = " insert into treesoft_config (databaseType ,databaseName, userName ,passwrod , port, ip , isdefault ,name , url  ) values ( '" + config.getDatabaseType() + "','" + config.getDatabaseName() + "','" + config.getUserName() + "','" + config.getPassword() + "','" + config.getPort() + "','" + config.getIp() + "','" + isdefault + "','" + config.getName() + "','" + config.getUrl() + "' ) ";
        }

        boolean bl = db.do_update(sql);
        return bl;
    }

    public List<Map<String, Object>> selectUserById(String userId) {
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_users where id='" + userId + "' ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public boolean updateUserPass(String userId, String newPass) throws Exception {
        DBUtil db = new DBUtil();
        String sql = " update treesoft_users  set password='" + newPass + "'  where id='" + userId + "'";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public int executeSqlNotRes(String sql, String dbName) throws Exception {
        new HashMap();
        new DBUtil2(dbName);
        int i = DBUtil2.setupdateData(sql);
        return i;
    }

    public int deleteRows(String databaseName, String tableName, String primary_key, String[] ids) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < ids.length; ++i) {
            String sql = " delete from  " + databaseName + "." + tableName + " where " + primary_key + " ='" + ids[i] + "'";
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int deleteRowsNew(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < condition.size(); ++i) {
            String whereStr = (String) condition.get(i);
            String sql = " delete from  " + databaseName + "." + tableName + " where  1=1 " + whereStr;
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int saveRows(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " insert into " + databaseName + "." + tableName;
        String colums = " ";
        String values = " ";
        Iterator var10 = map.entrySet().iterator();

        while (var10.hasNext()) {
            Entry<String, String> entry = (Entry) var10.next();
            colums = colums + (String) entry.getKey() + ",";
            String str = (String) entry.getValue();
            if (str.equals("")) {
                values = values + " null ,";
            } else {
                values = values + "'" + (String) entry.getValue() + "',";
            }
        }

        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues) {
        new DBUtil2(databaseName);
        String sql2 = " select * from   " + databaseName + "." + tableName + " where " + id + "='" + idValues + "' ";
        List<Map<String, Object>> list = DBUtil2.queryForListWithType(sql2);
        return list;
    }

    public int updateRows(Map<String, Object> map, String databaseName, String tableName, String id, String idValues) throws Exception {
        if (id != null && !"".equals(id)) {
            if (idValues != null && !"".equals(idValues)) {
                new DBUtil2(databaseName);
                String sql = " update  " + databaseName + "." + tableName;
                String ss = " set  ";
                Iterator var11 = map.entrySet().iterator();

                while (var11.hasNext()) {
                    Entry<String, Object> entry = (Entry) var11.next();
                    String str = "" + entry.getValue();
                    if (str.equals("")) {
                        ss = ss + (String) entry.getKey() + "= null ,";
                    } else if (entry.getValue() instanceof String) {
                        ss = ss + (String) entry.getKey() + "= '" + entry.getValue() + "',";
                    } else {
                        ss = ss + (String) entry.getKey() + "= " + entry.getValue() + ",";
                    }
                }

                ss = ss.substring(0, ss.length() - 1);
                sql = sql + ss + " where " + id + "='" + idValues + "'";
                int y = DBUtil2.setupdateData(sql);
                return y;
            } else {
                throw new Exception("数据不完整,保存失败!");
            }
        } else {
            throw new Exception("数据不完整,保存失败!");
        }
    }

    public String getViewSql(String databaseName, String tableName) throws Exception {
        String sql = " select  view_definition  from  information_schema.VIEWS  where  table_name='" + tableName + "' and table_schema='" + databaseName + "'  ";
        String str = "";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = (Map) list.get(0);
            str = (String) map.get("view_definition");
        }

        return str;
    }

    public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName) throws Exception {
        String sql = "select * from  " + databaseName + "." + tableName + " limit 1";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForColumnOnly(sql);
        return list;
    }

    public String getPrimaryKeys(String databaseName, String tableName) {
        DBUtil2 db = new DBUtil2(databaseName);
        return db.getPrimaryKeys(databaseName, tableName);
    }

    public List<Map<String, Object>> getPrimaryKeyss(String databaseName, String tableName) throws Exception {
        String sql = " select   column_name  from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_key='PRI' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass) {
        if (databaseType.equals("Redis")) {
            return RedisAPI.testConnForRedis(databaseType, databaseName, ip, port, user, pass);
        } else {
            return databaseType.equals("Memcache") ? MemcachedUtil.testConnection(databaseType, databaseName, ip, port, user, pass) : false;
        }
    }

    public List<Map<String, Object>> selectSqlStudy() {
        DBUtil db = new DBUtil();
        String sql = " select id, title, content, pid,icon  from  treesoft_study   ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public int saveDesginColumn(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " alter table " + databaseName + "." + tableName + " add column ";
        sql = sql + (String) map.get("COLUMN_NAME") + "  ";
        sql = sql + (String) map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !((String) map.get("CHARACTER_MAXIMUM_LENGTH")).equals("")) {
            sql = sql + " (" + (String) map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }

        if (map.get("COLUMN_COMMENT") != null && !((String) map.get("COLUMN_COMMENT")).equals("")) {
            sql = sql + " comment '" + (String) map.get("COLUMN_COMMENT") + "'";
        }

        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int deleteTableColumn(String databaseName, String tableName, String[] ids) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < ids.length; ++i) {
            String sql = " alter table   " + databaseName + "." + tableName + " drop column  " + ids[i];
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int updateTableColumn(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues) throws Exception {
        if (columnName != null && !"".equals(columnName)) {
            if (idValues != null && !"".equals(idValues)) {
                new DBUtil2(databaseName);
                String old_field_name = (String) map.get("TREESOFTPRIMARYKEY");
                String column_name = (String) map.get("COLUMN_NAME");
                String data_type = (String) map.get("DATA_TYPE");
                String character_maximum_length = "" + map.get("CHARACTER_MAXIMUM_LENGTH");
                String column_comment = (String) map.get("COLUMN_COMMENT");
                String sql2;
                int y;
                if (!old_field_name.endsWith(column_name)) {
                    sql2 = " alter table  " + databaseName + "." + tableName + " change ";
                    sql2 = sql2 + old_field_name + " " + column_name;
                    y = DBUtil2.setupdateData(sql2);
                }

                sql2 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + data_type;
                if (character_maximum_length != null && !character_maximum_length.equals("")) {
                    sql2 = sql2 + " (" + character_maximum_length + ")";
                }

                if (column_comment != null && !column_comment.equals("")) {
                    sql2 = sql2 + " comment '" + column_comment + "'";
                }

                y = DBUtil2.setupdateData(sql2);
                return y;
            } else {
                throw new Exception("数据不完整,保存失败!");
            }
        } else {
            throw new Exception("数据不完整,保存失败!");
        }
    }

    public int dropPrimaryKey(String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";
        DBUtil2.setupdateData(sql4);
        return 0;
    }

    public int savePrimaryKey2(String databaseName, String tableName, String primaryKeys) throws Exception {
        String sql4 = "";
        if (primaryKeys != null && !primaryKeys.equals("")) {
            new DBUtil2(databaseName);
            sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKeys + ")";
            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public int savePrimaryKey(String databaseName, String tableName, String column_name, String isSetting) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            List<String> list2 = this.selectTablePrimaryKey(databaseName, tableName);
            if (isSetting.equals("true")) {
                list2.add(column_name);
            } else {
                list2.remove(column_name);
            }

            String tem = list2.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (primaryKey.equals("")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key ";
            } else if (list2.size() == 1 && isSetting.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " add primary key (" + primaryKey + ")";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " drop primary key, add primary key (" + primaryKey + ")";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public List<String> selectTablePrimaryKey(String databaseName, String tableName) throws Exception {
        String sql = " select column_name   from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + databaseName + "'  and column_key='PRI' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        List<String> list2 = new ArrayList();
        Iterator var8 = list.iterator();

        while (var8.hasNext()) {
            Map<String, Object> map = (Map) var8.next();
            list2.add((String) map.get("column_name"));
        }

        return list2;
    }

    public String selectOneColumnType(String databaseName, String tableName, String column_name) throws Exception {
        String sql = " select   column_type  from information_schema.columns where   table_name='" + tableName + "' and table_schema='" + databaseName + "' and column_name='" + column_name + "'";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return (String) ((Map) list.get(0)).get("column_type");
    }

    public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            String column_type = this.selectOneColumnType(databaseName, tableName, column_name);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + "  null ";
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " not null ";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            String column_type = this.selectOneColumnType(databaseName, tableName, column_name);
            if (column_name2 != null && !column_name2.equals("")) {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " after " + column_name2;
            } else {
                sql4 = " alter table  " + databaseName + "." + tableName + " modify column " + column_name + " " + column_type + " first ";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public List<Map<String, Object>> getAllDataBaseForOracle() throws Exception {
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("SCHEMA_NAME", Constants.DATABASENAME);
        list.add(map);
        return list;
    }

    public List<Map<String, Object>> selectUserByName(String userName) {
        DBUtil db = new DBUtil();
        String sql = " select * from  treesoft_users where username='" + userName + "' ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public List<Map<String, Object>> getAllTablesForOracle(String dbName) throws Exception {
        new DBUtil2(dbName);
        String sql = " select TABLE_NAME  from  user_tables  ";
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3ForOracle(String dbName, String tableName) throws Exception {
        String sql = "select t1.column_name as TREESOFTPRIMARYKEY, t1.COLUMN_NAME,  nvl2( t1.CHAR_COL_DECL_LENGTH,  t1.DATA_TYPE||'(' ||CHAR_COL_DECL_LENGTH||')',t1.DATA_TYPE ) as COLUMN_TYPE ,t1.data_type,   t1.data_length as CHARACTER_MAXIMUM_LENGTH ,CASE t1.nullable when 'Y' then 'YES' END as IS_NULLABLE  ,  nvl2(t3.column_name ,'PRI' ,'')  as COLUMN_KEY,  t2.comments as COLUMN_COMMENT  from user_tab_columns  t1   left join user_col_comments t2  on  t1.table_name = t2.table_name and t1.COLUMN_NAME = t2.COLUMN_NAME   left join   (select a.table_name, a.column_name   from user_cons_columns a, user_constraints b    where a.constraint_name = b.constraint_name    and b.constraint_type = 'P' ) t3    on t1.TABLE_NAME = t3.table_name  and t1.COLUMN_NAME = t3.COLUMN_NAME    where   t1.table_name= '" + tableName + "'  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForOracle(String dbName) throws Exception {
        String sql = " select view_name as TABLE_NAME from  user_views  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public String getViewSqlForOracle(String databaseName, String tableName) throws Exception {
        String sql = " select  view_definition  from  information_schema.VIEWS  where  table_name='" + tableName + "' and table_schema='" + databaseName + "'  ";
        String str = "";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = (Map) list.get(0);
            str = (String) map.get("view_definition");
        }

        return str;
    }

    public List<Map<String, Object>> getAllFuntionForOracle(String dbName) throws Exception {
        String sql = " select object_name as ROUTINE_NAME from  user_procedures ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        new DBUtil2(dbName);
        List<Map<String, Object>> list3 = this.getPrimaryKeyssForOracle(dbName, tableName);
        String tem = "";

        Map map0;
        for (Iterator var13 = list3.iterator(); var13.hasNext(); tem = tem + map0.get("COLUMN_NAME") + ",") {
            map0 = (Map) var13.next();
        }

        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }

        String sql = "select * from  " + tableName;
        String sql2 = "";
        if (orderBy != null && !orderBy.equals("")) {
            sql2 = "select * from (select rownum rn, t1.* from " + tableName + " t1) where rn between " + limitFrom + " and  " + (limitFrom + pageSize) + " order by " + orderBy + " " + order;
        } else {
            sql2 = "select * from (select rownum rn, t1.* from " + tableName + " t1) where rn between " + limitFrom + " and  " + (limitFrom + pageSize);
        }

        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCountForOracle(sql);
        List<Map<String, Object>> columns = this.getTableColumnsForOracle(dbName, tableName);
        List<Map<String, Object>> tempList = new ArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);

        HashMap map2;
        for (Iterator var21 = columns.iterator(); var21.hasNext(); tempList.add(map2)) {
            Map<String, Object> map = (Map) var21.next();
            map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            map2.put("editor", "text");
            if (!map.get("data_type").equals("DATETIME") && !map.get("data_type").equals("DATE")) {
                if (!map.get("data_type").equals("INT") && !map.get("data_type").equals("SMALLINT") && !map.get("data_type").equals("TINYINT")) {
                    if (map.get("data_type").equals("DOUBLE")) {
                        map2.put("editor", "numberbox");
                    } else {
                        map2.put("editor", "text");
                    }
                } else {
                    map2.put("editor", "numberbox");
                }
            } else {
                map2.put("editor", "datebox");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }

    public List<Map<String, Object>> getPrimaryKeyssForOracle(String databaseName, String tableName) throws Exception {
        String sql = "  select  COLUMN_NAME   from   user_cons_columns  where   constraint_name= (select  constraint_name  from user_constraints  where table_name = '" + tableName + "' and constraint_type ='P') ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForOracle(String dbName, String tableName) throws Exception {
        String sql = "select  * from   " + tableName + " where rownum =1 ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForColumnOnly(sql);
        return list;
    }

    public Page<Map<String, Object>> executeSqlHaveResForOracle(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "SELECT * FROM (SELECT A.*, ROWNUM RN  FROM (  " + sql + " ) A ) WHERE RN BETWEEN " + limitFrom + " AND " + (limitFrom + pageSize);
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCountForOracle(sql);
        List<Map<String, Object>> columns = this.executeSqlForColumnsForOracle(sql, dbName);
        List<Map<String, Object>> tempList = new ArrayList();
        Iterator var14 = columns.iterator();

        while (var14.hasNext()) {
            Map<String, Object> map = (Map) var14.next();
            Map<String, Object> map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            tempList.add(map2);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }

    public List<Map<String, Object>> executeSqlForColumnsForOracle(String sql, String dbName) throws Exception {
        String sql2 = " select * from (" + sql + ") where  rownum = 1 ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.executeSqlForColumns(sql2);
        return list;
    }

    public int updateTableNullAbleForOracle(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " modify   " + column_name + "  null ";
            } else {
                sql4 = " alter table  " + tableName + " modify   " + column_name + "  not null ";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public int savePrimaryKeyForOracle(String databaseName, String tableName, String column_name, String isSetting) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            List<Map<String, Object>> list2 = this.selectTablePrimaryKeyForOracle(databaseName, tableName);
            List<String> list3 = new ArrayList();
            Iterator var10 = list2.iterator();

            while (var10.hasNext()) {
                Map map = (Map) var10.next();
                list3.add((String) map.get("COLUMN_NAME"));
            }

            if (isSetting.equals("true")) {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }

            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0) {
                String temp = (String) ((Map) list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table   " + tableName + " drop constraint  " + temp;
                DBUtil2.setupdateData(sql4);
            }

            if (!primaryKey.equals("")) {
                sql4 = " alter table " + tableName + " add   primary key (" + primaryKey + ") ";
                DBUtil2.setupdateData(sql4);
            }
        }

        return 0;
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForOracle(String databaseName, String tableName) throws Exception {
        String sql = " select a.CONSTRAINT_NAME,  a.COLUMN_NAME  from user_cons_columns a, user_constraints b  where a.constraint_name = b.constraint_name   and b.constraint_type = 'P'  and a.table_name = '" + tableName + "' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        new ArrayList();
        return list;
    }

    public int saveDesginColumnForOracle(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " alter table " + tableName + " add  ";
        sql = sql + (String) map.get("COLUMN_NAME") + "  ";
        sql = sql + (String) map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !((String) map.get("CHARACTER_MAXIMUM_LENGTH")).equals("")) {
            sql = sql + " (" + (String) map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }

        if (map.get("COLUMN_COMMENT") != null && !((String) map.get("COLUMN_COMMENT")).equals("")) {
            sql = sql + " comment '" + (String) map.get("COLUMN_COMMENT") + "'";
        }

        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int updateTableColumnForOracle(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues) throws Exception {
        if (columnName != null && !"".equals(columnName)) {
            if (idValues != null && !"".equals(idValues)) {
                new DBUtil2(databaseName);
                String old_field_name = (String) map.get("TREESOFTPRIMARYKEY");
                String column_name = (String) map.get("COLUMN_NAME");
                String data_type = (String) map.get("DATA_TYPE");
                String character_maximum_length = "" + map.get("CHARACTER_MAXIMUM_LENGTH");
                String column_comment = (String) map.get("COLUMN_COMMENT");
                String sql2;
                int y;
                if (!old_field_name.endsWith(column_name)) {
                    sql2 = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
                    y = DBUtil2.setupdateData(sql2);
                }

                sql2 = " alter table  " + tableName + " modify  " + column_name + " " + data_type;
                if (character_maximum_length != null && !character_maximum_length.equals("")) {
                    sql2 = sql2 + " (" + character_maximum_length + ")";
                }

                y = DBUtil2.setupdateData(sql2);
                if (column_comment != null && !column_comment.equals("")) {
                    String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
                    DBUtil2.setupdateData(sql4);
                }

                return y;
            } else {
                throw new Exception("数据不完整,保存失败!");
            }
        } else {
            throw new Exception("数据不完整,保存失败!");
        }
    }

    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < ids.length; ++i) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int saveRowsForOracle(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " insert into  " + tableName;
        String colums = " ";
        String values = " ";
        String columnType = "";
        Iterator var11 = map.entrySet().iterator();

        while (var11.hasNext()) {
            Entry<String, String> entry = (Entry) var11.next();
            colums = colums + (String) entry.getKey() + ",";
            columnType = this.selectColumnTypeForOracle(databaseName, tableName, (String) entry.getKey());
            String str = (String) entry.getValue();
            if (str.equals("")) {
                values = values + " null ,";
            } else if (columnType.equals("DATE")) {
                values = values + " to_date('" + (String) entry.getValue() + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
            } else {
                values = values + "'" + (String) entry.getValue() + "',";
            }
        }

        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public String selectColumnTypeForOracle(String databaseName, String tableName, String column) throws Exception {
        String sql = " select DATA_TYPE from user_tab_columns where table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return (String) ((Map) list.get(0)).get("DATA_TYPE");
    }

    public int deleteRowsNewForOracle(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < condition.size(); ++i) {
            String whereStr = (String) condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public List<Map<String, Object>> getAllDataBaseForPostgreSQL() throws Exception {
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("SCHEMA_NAME", Constants.DATABASENAME);
        list.add(map);
        return list;
    }

    public List<Map<String, Object>> getAllTablesForPostgreSQL(String dbName) throws Exception {
        new DBUtil2(dbName);
        String sql = " select  tablename as \"TABLE_NAME\" from pg_tables  where schemaname='public'  ";
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3ForPostgreSQL(String dbName, String tableName) throws Exception {
        String sql = "   select t1.column_name as \"TREESOFTPRIMARYKEY\", t1.COLUMN_NAME as \"COLUMN_NAME\", t1.DATA_TYPE   as \"COLUMN_TYPE\" , t1.DATA_TYPE as \"DATA_TYPE\" , character_maximum_length as \"CHARACTER_MAXIMUM_LENGTH\" ,   t1.IS_NULLABLE as \"IS_NULLABLE\" ,  '' as \"COLUMN_COMMENT\" , CASE  WHEN t2.COLUMN_NAME IS NULL THEN ''  ELSE 'PRI'  END AS \"COLUMN_KEY\"   from information_schema.columns t1    left join information_schema.constraint_column_usage t2    on t1.table_name = t2.table_name  and t1.COLUMN_NAME = t2.COLUMN_NAME where  t1.table_name='" + tableName + "'    ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForPostgreSQL(String dbName) throws Exception {
        String sql = " select   viewname as \"TABLE_NAME\"  from pg_views  where schemaname='public'  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllFuntionForPostgreSQL(String dbName) throws Exception {
        String sql = "  select prosrc as \"ROUTINE_NAME\" from pg_proc where 1=2  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForPostgreSQL(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        new DBUtil2(dbName);
        List<Map<String, Object>> list3 = this.getPrimaryKeyssForPostgreSQL(dbName, tableName);
        String tem = "";

        Map map0;
        for (Iterator var13 = list3.iterator(); var13.hasNext(); tem = tem + map0.get("COLUMN_NAME") + ",") {
            map0 = (Map) var13.next();
        }

        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }

        String sql = "select * from  " + tableName;
        String sql2 = "";
        if (orderBy != null && !orderBy.equals("")) {
            sql2 = "select  *  from  " + tableName + " order by " + orderBy + " " + order + "  LIMIT " + pageSize + "  OFFSET " + limitFrom;
        } else {
            sql2 = "select  *  from  " + tableName + "  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        }

        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = this.getTableColumnsForPostgreSQL(dbName, tableName);
        List<Map<String, Object>> tempList = new ArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);

        HashMap map2;
        for (Iterator var21 = columns.iterator(); var21.hasNext(); tempList.add(map2)) {
            Map<String, Object> map = (Map) var21.next();
            map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            map2.put("editor", "text");
            if (!map.get("data_type").equals("DATETIME") && !map.get("data_type").equals("DATE")) {
                if (!map.get("data_type").equals("INT") && !map.get("data_type").equals("SMALLINT") && !map.get("data_type").equals("TINYINT")) {
                    if (map.get("data_type").equals("DOUBLE")) {
                        map2.put("editor", "numberbox");
                    } else {
                        map2.put("editor", "text");
                    }
                } else {
                    map2.put("editor", "numberbox");
                }
            } else {
                map2.put("editor", "datebox");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResForPostgreSQL(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        String sql2 = "select  *  from  (" + sql + ") t  LIMIT " + pageSize + " OFFSET  " + limitFrom;
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql2);
        int rowCount = DBUtil2.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = this.executeSqlForColumnsForPostgreSQL(sql, dbName);
        List<Map<String, Object>> tempList = new ArrayList();
        Iterator var14 = columns.iterator();

        while (var14.hasNext()) {
            Map<String, Object> map = (Map) var14.next();
            Map<String, Object> map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            tempList.add(map2);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }

    public int deleteRowsNewForPostgreSQL(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < condition.size(); ++i) {
            String whereStr = (String) condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int saveRowsForPostgreSQL(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " insert into  " + tableName;
        String colums = " ";
        String values = " ";
        String columnType = "";
        Iterator var11 = map.entrySet().iterator();

        while (var11.hasNext()) {
            Entry<String, String> entry = (Entry) var11.next();
            colums = colums + (String) entry.getKey() + ",";
            columnType = this.selectColumnTypeForPostgreSQL(databaseName, tableName, (String) entry.getKey());
            String str = (String) entry.getValue();
            if (str.equals("")) {
                values = values + " null ,";
            } else if (columnType.equals("DATE")) {
                values = values + " to_date('" + (String) entry.getValue() + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
            } else {
                values = values + "'" + (String) entry.getValue() + "',";
            }
        }

        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int saveDesginColumnForPostgreSQL(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " alter table " + tableName + " add  ";
        sql = sql + (String) map.get("COLUMN_NAME") + "  ";
        sql = sql + (String) map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !((String) map.get("CHARACTER_MAXIMUM_LENGTH")).equals("")) {
            sql = sql + " (" + (String) map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }

        if (map.get("COLUMN_COMMENT") != null && !((String) map.get("COLUMN_COMMENT")).equals("")) {
            sql = sql + " comment '" + (String) map.get("COLUMN_COMMENT") + "'";
        }

        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int updateTableColumnForPostgreSQL(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues) throws Exception {
        if (columnName != null && !"".equals(columnName)) {
            if (idValues != null && !"".equals(idValues)) {
                new DBUtil2(databaseName);
                String old_field_name = (String) map.get("TREESOFTPRIMARYKEY");
                String column_name = (String) map.get("COLUMN_NAME");
                String data_type = (String) map.get("DATA_TYPE");
                String character_maximum_length = "" + map.get("CHARACTER_MAXIMUM_LENGTH");
                String column_comment = (String) map.get("COLUMN_COMMENT");
                String sql2;
                int y;
                if (!old_field_name.endsWith(column_name)) {
                    sql2 = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
                    y = DBUtil2.setupdateData(sql2);
                }

                sql2 = " alter table  " + tableName + " modify  " + column_name + " " + data_type;
                if (character_maximum_length != null && !character_maximum_length.equals("")) {
                    sql2 = sql2 + " (" + character_maximum_length + ")";
                }

                y = DBUtil2.setupdateData(sql2);
                if (column_comment != null && !column_comment.equals("")) {
                    String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
                    DBUtil2.setupdateData(sql4);
                }

                return y;
            } else {
                throw new Exception("数据不完整,保存失败!");
            }
        } else {
            throw new Exception("数据不完整,保存失败!");
        }
    }

    public int deleteTableColumnForPostgreSQL(String databaseName, String tableName, String[] ids) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < ids.length; ++i) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int updateTableNullAbleForPostgreSQL(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " drop not null ";
            } else {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " set not null ";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public int savePrimaryKeyForPostgreSQL(String databaseName, String tableName, String column_name, String isSetting) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            List<Map<String, Object>> list2 = this.selectTablePrimaryKeyForPostgreSQL(databaseName, tableName);
            List<String> list3 = new ArrayList();
            Iterator var10 = list2.iterator();

            while (var10.hasNext()) {
                Map map = (Map) var10.next();
                list3.add((String) map.get("COLUMN_NAME"));
            }

            if (isSetting.equals("true")) {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }

            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0) {
                String temp = (String) ((Map) list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table   " + tableName + " drop constraint  " + temp;
                DBUtil2.setupdateData(sql4);
            }

            if (!primaryKey.equals("")) {
                sql4 = " alter table " + tableName + " add   primary key (" + primaryKey + ") ";
                DBUtil2.setupdateData(sql4);
            }
        }

        return 0;
    }

    public List<Map<String, Object>> getPrimaryKeyssForPostgreSQL(String databaseName, String tableName) throws Exception {
        String sql = " select  pg_attribute.attname as \"COLUMN_NAME\" from   pg_constraint  inner join pg_class    on pg_constraint.conrelid = pg_class.oid    inner join pg_attribute on pg_attribute.attrelid = pg_class.oid    and  pg_attribute.attnum = pg_constraint.conkey[1]     inner join pg_type on pg_type.oid = pg_attribute.atttypid  where pg_class.relname = '" + tableName + "'  " + " and pg_constraint.contype='p' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForPostgreSQL(String dbName, String tableName) throws Exception {
        String sql = "select  * from   " + tableName + " limit 1 ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForColumnOnly(sql);
        return list;
    }

    public String selectColumnTypeForPostgreSQL(String databaseName, String tableName, String column) throws Exception {
        String sql = " select data_type as \"DATA_TYPE\"  from  information_schema.columns  where    table_name ='" + tableName + "' AND COLUMN_NAME ='" + column + "' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return (String) ((Map) list.get(0)).get("DATA_TYPE");
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForPostgreSQL(String databaseName, String tableName) throws Exception {
        String sql = " select pg_constraint.conname as \"CONSTRAINT_NAME\" ,pg_attribute.attname as \"COLUMN_NAME\" ,pg_type.typname as typename from   pg_constraint  inner join pg_class   on pg_constraint.conrelid = pg_class.oid    inner join pg_attribute on pg_attribute.attrelid = pg_class.oid    and  pg_attribute.attnum = pg_constraint.conkey[1]   inner join pg_type on pg_type.oid = pg_attribute.atttypid    where pg_class.relname = '" + tableName + "'  " + "  and pg_constraint.contype='p' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        new ArrayList();
        return list;
    }

    public List<Map<String, Object>> executeSqlForColumnsForPostgreSQL(String sql, String dbName) throws Exception {
        String sql2 = " select * from (" + sql + ") t   limit 1; ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.executeSqlForColumns(sql2);
        return list;
    }

    public String getViewSqlForPostgreSQL(String databaseName, String tableName) throws Exception {
        String sql = " select  view_definition  from  information_schema.views  where  table_name='" + tableName + "' and table_catalog='" + databaseName + "'  ";
        String str = " ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = (Map) list.get(0);
            str = (String) map.get("view_definition");
        }

        return str;
    }

    public List<Map<String, Object>> getAllDataBaseForMSSQL() throws Exception {
        String sql = " SELECT Name as SCHEMA_NAME FROM Master..SysDatabases ORDER BY Name  ";
        new DBUtil2(Constants.DATABASENAME);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        List<Map<String, Object>> list2 = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("SCHEMA_NAME", Constants.DATABASENAME);
        list2.add(map);

        for (int i = 0; i < list.size(); ++i) {
            Map<String, Object> map2 = (Map) list.get(i);
            String schema_name = (String) map2.get("SCHEMA_NAME");
            if (!schema_name.equals(Constants.DATABASENAME)) {
                list2.add(map2);
            }
        }

        return list2;
    }

    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName) throws Exception {
        new DBUtil2(dbName);
        String sql = " SELECT Name as TABLE_NAME FROM " + dbName + "..SysObjects Where XType='U' ORDER BY Name ";
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumns3ForMSSQL(String dbName, String tableName) throws Exception {
        String sql = " select b.name   TREESOFTPRIMARYKEY, b.name COLUMN_NAME, ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')' , c.name ) as  COLUMN_TYPE, c.name DATA_TYPE, b.length CHARACTER_MAXIMUM_LENGTH ,  case when b.isnullable=1  then 'YES' else 'NO' end IS_NULLABLE , '' as COLUMN_COMMENT , '' as COLUMN_KEY  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='" + tableName + "' and a.xtype='U'  and b.xtype=c.xtype ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList2(sql);
        return list;
    }

    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName) throws Exception {
        String sql = "  SELECT  NAME AS TABLE_NAME FROM  sysobjects where XTYPE ='V'  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName) throws Exception {
        String sql = " SELECT  NAME AS ROUTINE_NAME FROM  sysobjects where XTYPE ='FN' ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            --limitFrom;
        }

        String orderBy = page.getOrderBy();
        String order = page.getOrder();
        new DBUtil2(dbName);
        List<Map<String, Object>> list3 = this.getPrimaryKeyssForMSSQL(dbName, tableName);
        String tem = "";

        Map map0;
        for (Iterator var13 = list3.iterator(); var13.hasNext(); tem = tem + map0.get("COLUMN_NAME") + ",") {
            map0 = (Map) var13.next();
        }

        String primaryKey = "";
        if (!tem.equals("")) {
            primaryKey = tem.substring(0, tem.length() - 1);
        }

        String sql = "select * from  " + tableName;
        String sql2 = "";
        if (orderBy != null && !orderBy.equals("")) {
            sql2 = "select * from  " + tableName + " order by " + order;
        } else {
            sql2 = "select * from  " + tableName;
        }

        List<Map<String, Object>> list = DBUtil2.queryForListPage(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = DBUtil2.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = this.getTableColumnsForMSSQL(dbName, tableName);
        List<Map<String, Object>> tempList = new ArrayList();
        Map<String, Object> map1 = new HashMap();
        map1.put("field", "treeSoftPrimaryKey");
        map1.put("checkbox", true);
        tempList.add(map1);

        HashMap map2;
        for (Iterator var21 = columns.iterator(); var21.hasNext(); tempList.add(map2)) {
            Map<String, Object> map = (Map) var21.next();
            map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            map2.put("editor", "text");
            if (!map.get("data_type").equals("DATETIME") && !map.get("data_type").equals("DATE")) {
                if (!map.get("data_type").equals("INT") && !map.get("data_type").equals("SMALLINT") && !map.get("data_type").equals("TINYINT")) {
                    if (map.get("data_type").equals("DOUBLE")) {
                        map2.put("editor", "numberbox");
                    } else {
                        map2.put("editor", "text");
                    }
                } else {
                    map2.put("editor", "numberbox");
                }
            } else {
                map2.put("editor", "datebox");
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        page.setPrimaryKey(primaryKey);
        return page;
    }

    public Page<Map<String, Object>> executeSqlHaveResForMSSQL(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        if (limitFrom > 0) {
            --limitFrom;
        }

        String sql2 = " select  * from (" + sql + ")  t1  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForListPage(sql2, pageNo * pageSize, (pageNo - 1) * pageSize);
        int rowCount = DBUtil2.executeQueryForCountForPostgreSQL(sql);
        List<Map<String, Object>> columns = this.executeSqlForColumnsForMSSQL(sql, dbName);
        List<Map<String, Object>> tempList = new ArrayList();
        Iterator var14 = columns.iterator();

        while (var14.hasNext()) {
            Map<String, Object> map = (Map) var14.next();
            Map<String, Object> map2 = new HashMap();
            map2.put("field", map.get("column_name"));
            map2.put("title", map.get("column_name"));
            map2.put("sortable", true);
            tempList.add(map2);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonfromList = "[" + mapper.writeValueAsString(tempList) + "]";
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        page.setColumns(jsonfromList);
        return page;
    }

    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < condition.size(); ++i) {
            String whereStr = (String) condition.get(i);
            String sql = " delete from  " + tableName + " where  1=1 " + whereStr;
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public String getViewSqlForMSSQL(String databaseName, String tableName) throws Exception {
        String sql = " select  view_definition  from  information_schema.views  where  table_name='" + tableName + "' and table_catalog='" + databaseName + "'  ";
        String str = " ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        if (list.size() == 1) {
            Map<String, Object> map = (Map) list.get(0);
            str = (String) map.get("view_definition");
        }

        return str;
    }

    public int saveRowsForMSSQL(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " insert into  " + tableName;
        String colums = " ";
        String values = " ";
        String columnType = "";
        Iterator var11 = map.entrySet().iterator();

        while (var11.hasNext()) {
            Entry<String, String> entry = (Entry) var11.next();
            colums = colums + (String) entry.getKey() + ",";
            columnType = this.selectColumnTypeForPostgreSQL(databaseName, tableName, (String) entry.getKey());
            String str = (String) entry.getValue();
            if (str.equals("")) {
                values = values + " null ,";
            } else if (columnType.equals("DATE")) {
                values = values + " to_date('" + (String) entry.getValue() + "' ,'yyyy-mm-dd hh24:mi:ss') ,";
            } else {
                values = values + "'" + (String) entry.getValue() + "',";
            }
        }

        colums = colums.substring(0, colums.length() - 1);
        values = values.substring(0, values.length() - 1);
        sql = sql + " (" + colums + ") values (" + values + ")";
        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int saveDesginColumnForMSSQL(Map<String, String> map, String databaseName, String tableName) throws Exception {
        new DBUtil2(databaseName);
        String sql = " alter table " + tableName + " add  ";
        sql = sql + (String) map.get("COLUMN_NAME") + "  ";
        sql = sql + (String) map.get("DATA_TYPE");
        if (map.get("CHARACTER_MAXIMUM_LENGTH") != null && !((String) map.get("CHARACTER_MAXIMUM_LENGTH")).equals("")) {
            sql = sql + " (" + (String) map.get("CHARACTER_MAXIMUM_LENGTH") + ") ";
        }

        if (map.get("COLUMN_COMMENT") != null && !((String) map.get("COLUMN_COMMENT")).equals("")) {
            sql = sql + " comment '" + (String) map.get("COLUMN_COMMENT") + "'";
        }

        int y = DBUtil2.setupdateData(sql);
        return y;
    }

    public int updateTableColumnForMSSQL(Map<String, Object> map, String databaseName, String tableName, String columnName, String idValues) throws Exception {
        if (columnName != null && !"".equals(columnName)) {
            if (idValues != null && !"".equals(idValues)) {
                new DBUtil2(databaseName);
                String old_field_name = (String) map.get("TREESOFTPRIMARYKEY");
                String column_name = (String) map.get("COLUMN_NAME");
                String data_type = (String) map.get("DATA_TYPE");
                String character_maximum_length = "" + map.get("CHARACTER_MAXIMUM_LENGTH");
                String column_comment = (String) map.get("COLUMN_COMMENT");
                String sql2;
                int y;
                if (!old_field_name.endsWith(column_name)) {
                    sql2 = " ALTER TABLE " + tableName + " RENAME COLUMN " + old_field_name + " to  " + column_name;
                    y = DBUtil2.setupdateData(sql2);
                }

                sql2 = " alter table  " + tableName + " alter column " + column_name + " " + data_type;
                if (character_maximum_length != null && !character_maximum_length.equals("")) {
                    sql2 = sql2 + " (" + character_maximum_length + ")";
                }

                y = DBUtil2.setupdateData(sql2);
                if (column_comment != null && !column_comment.equals("")) {
                    String sql4 = "  comment on column " + tableName + "." + column_name + " is '" + column_comment + "' ";
                    DBUtil2.setupdateData(sql4);
                }

                return y;
            } else {
                throw new Exception("数据不完整,保存失败!");
            }
        } else {
            throw new Exception("数据不完整,保存失败!");
        }
    }

    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids) throws Exception {
        new DBUtil2(databaseName);
        int y = 0;

        for (int i = 0; i < ids.length; ++i) {
            String sql = " alter table   " + tableName + " drop (" + ids[i] + ")";
            y += DBUtil2.setupdateData(sql);
        }

        return y;
    }

    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            String column_type = this.selectOneColumnTypeForMSSQL(databaseName, tableName, column_name);
            if (is_nullable.equals("true")) {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " " + column_type + " " + "  null ";
            } else {
                sql4 = " alter table  " + tableName + " alter column   " + column_name + " " + column_type + " " + "  not null ";
            }

            DBUtil2.setupdateData(sql4);
        }

        return 0;
    }

    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String column_name, String isSetting) throws Exception {
        String sql4 = "";
        if (column_name != null && !column_name.equals("")) {
            new DBUtil2(databaseName);
            List<Map<String, Object>> list2 = this.selectTablePrimaryKeyForMSSQL(databaseName, tableName);
            List<String> list3 = new ArrayList();
            Iterator var10 = list2.iterator();

            while (var10.hasNext()) {
                Map map = (Map) var10.next();
                list3.add((String) map.get("COLUMN_NAME"));
            }

            if (isSetting.equals("true")) {
                list3.add(column_name);
            } else {
                list3.remove(column_name);
            }

            String tem = list3.toString();
            String primaryKey = tem.substring(1, tem.length() - 1);
            if (list2.size() > 0) {
                String temp = (String) ((Map) list2.get(0)).get("CONSTRAINT_NAME");
                sql4 = " alter table   " + tableName + " drop constraint  " + temp;
                DBUtil2.setupdateData(sql4);
            }

            if (!primaryKey.equals("")) {
                sql4 = " alter table " + tableName + " add   primary key (" + primaryKey + ") ";
                DBUtil2.setupdateData(sql4);
            }
        }

        return 0;
    }

    public List<Map<String, Object>> selectTablePrimaryKeyForMSSQL(String databaseName, String tableName) throws Exception {
        String sql = " select pg_constraint.conname as \"CONSTRAINT_NAME\" ,pg_attribute.attname as \"COLUMN_NAME\" ,pg_type.typname as typename from   pg_constraint  inner join pg_class   on pg_constraint.conrelid = pg_class.oid    inner join pg_attribute on pg_attribute.attrelid = pg_class.oid    and  pg_attribute.attnum = pg_constraint.conkey[1]   inner join pg_type on pg_type.oid = pg_attribute.atttypid    where pg_class.relname = '" + tableName + "'  " + "  and pg_constraint.contype='p' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        new ArrayList();
        return list;
    }

    public List<Map<String, Object>> getPrimaryKeyssForMSSQL(String databaseName, String tableName) throws Exception {
        String sql = " select  c.name as COLUMN_NAME from sysindexes i   join sysindexkeys k on i.id = k.id and i.indid = k.indid    join sysobjects o on i.id = o.id    join syscolumns c on i.id=c.id and k.colid = c.colid    where o.xtype = 'U'   and exists(select 1 from sysobjects where  xtype = 'PK'  and name = i.name)     and o.name='" + tableName + "' ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return list;
    }

    public List<Map<String, Object>> getTableColumnsForMSSQL(String dbName, String tableName) throws Exception {
        String sql = "select top 1 * from   " + tableName;
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.queryForColumnOnly(sql);
        return list;
    }

    public List<Map<String, Object>> executeSqlForColumnsForMSSQL(String sql, String dbName) throws Exception {
        String sql2 = " select top 1 * from (" + sql + ") t  ";
        new DBUtil2(dbName);
        List<Map<String, Object>> list = DBUtil2.executeSqlForColumns(sql2);
        return list;
    }

    public String selectOneColumnTypeForMSSQL(String databaseName, String tableName, String column_name) throws Exception {
        String sql = " select  ISNULL( c.name +'('+  cast(b.length as varchar(10)) +')' , c.name ) as  column_type  from sysobjects a,syscolumns b,systypes c  where a.id=b.id  and a.name='" + tableName + "'  and  b.name='" + column_name + "' and a.xtype='U'  and b.xtype=c.xtype ";
        new DBUtil2(databaseName);
        List<Map<String, Object>> list = DBUtil2.queryForList(sql);
        return (String) ((Map) list.get(0)).get("column_type");
    }

    public boolean backupDatabaseExecute(String databaseName, String path) throws Exception {
        try {
            this.backupForMysql(databaseName, path);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return true;
    }

    public void backupForMysql(String databaseName, String path) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String fileName = databaseName + "-" + df.format(new Date()) + ".sql";

        try {
            Runtime rt = Runtime.getRuntime();
            Properties prop = System.getProperties();
            String os = prop.getProperty("os.name");
            String cmd = "";
            if (!os.startsWith("win") && !os.startsWith("Win")) {
                cmd = "mysqldump -h " + Constants.IP + " -u" + Constants.USERNAME + " -p" + Constants.PASSWROD + "  " + Constants.DATABASENAME + " > " + path + "backup" + File.separator + fileName;
                rt.exec(new String[]{"sh", "-c", cmd});
            } else {
                cmd = "cmd /c " + path + "WEB-INF\\lib\\mysqldump -h " + Constants.IP + " -u" + Constants.USERNAME + " -p" + Constants.PASSWROD + "  " + Constants.DATABASENAME + " > " + path + "backup" + File.separator + fileName;
                rt.exec(cmd);
            }
        } catch (Exception var10) {
            System.out.println(var10.getMessage());
            var10.printStackTrace();
        }

    }

    public boolean deleteBackupFile(String[] ids, String path) throws Exception {
        for (int i = 0; i < ids.length; ++i) {
            File f = new File(path + ids[i]);
            if (f.exists()) {
                f.delete();
            }
        }

        return true;
    }

    public Page<Map<String, Object>> getNoSQLDBForMemcached(Page<Map<String, Object>> page, String NoSQLDbName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        new MemcachedUtil();
        Map<String, Object> tempMap = MemcachedUtil.getAllKeyAndValue(pageSize, limitFrom, NoSQLDbName, selectKey, selectValue, ip, port, password);
        page.setTotalCount((long) (Integer) tempMap.get("rowCount"));
        page.setResult((List) tempMap.get("dataList"));
        return page;
    }

    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil du = new DBUtil();
        List<Map<String, Object>> list = du.getConfigList();
        int rowCount = list.size();
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        return page;
    }

    public int getDbAmountForRedis(Map<String, Object> map) {
        RedisAPI ra = new RedisAPI();
        return ra.getDbAmountForRedis(map);
    }

    public Page<Map<String, Object>> getNoSQLDBForRedis(Page<Map<String, Object>> page, String NoSQLDbName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        Map<String, Object> map = this.getConfig(databaseConfigId);
        Map<String, Object> tempMap = RedisAPI.getNoSQLDBForRedis(pageSize, limitFrom, map, NoSQLDbName, selectKey, selectValue);
        page.setTotalCount((long) (Integer) tempMap.get("rowCount"));
        page.setResult((List) tempMap.get("dataList"));
        return page;
    }

    public List<Map<String, Object>> getConfigAllDataBase() throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select id,name , databaseType , port, ip  from  treesoft_config order by isdefault desc ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        return list;
    }

    public int deleteNoSQLKeyForRedis(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        int y = 0;
        Map<String, Object> map = this.getConfig(databaseConfigId);
        RedisAPI.deleteKeys(map, NoSQLDbName, ids);
        return y;
    }

    public int deleteNoSQLKeyForMemcached(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        int y = 0;
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        MemcachedUtil memcached = new MemcachedUtil();

        for (int i = 0; i < ids.length; ++i) {
            memcached.memcachedDelete(ids[i], ip, port, password);
            ++y;
        }

        return y;
    }

    public boolean saveNotSqlDataForRedis(NotSqlEntity notSqlEntity, String databaseConfigId, String NoSQLDbName) throws Exception {
        Map<String, Object> map = this.getConfig(databaseConfigId);
        boolean bl = RedisAPI.set(notSqlEntity, map, NoSQLDbName);
        return bl;
    }

    public boolean saveNotSqlDataForMemcached(NotSqlEntity notSqlEntity, String databaseConfigId) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        MemcachedUtil dt = new MemcachedUtil();
        boolean bl = dt.memcachedSet(notSqlEntity, ip, port, password);
        return bl;
    }

    public Map<String, Object> selectNotSqlDataForRedis(String key, String NoSQLDbName, String databaseConfigId) {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        return RedisAPI.get2(key, NoSQLDbName, map3);
    }

    public Map<String, Object> selectNotSqlDataForMemcached(String key, String databaseConfigId) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        new MemcachedUtil();
        new HashMap();
        Map<String, Object> map = MemcachedUtil.memcachedGet3(key, ip, port, password);
        return map;
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForRedis(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String info = RedisAPI.getInfo(map3);
        Properties properties = new Properties();
        InputStream inStream = new ByteArrayInputStream(info.getBytes());
        properties.load(inStream);
        String parameter = "";
        String value = "";

        HashMap map;
        for (Iterator it = properties.entrySet().iterator(); it.hasNext(); list.add(map)) {
            map = new HashMap();
            Entry<Object, Object> entry = (Entry) it.next();
            parameter = (String) entry.getKey();
            value = (String) entry.getValue();
            map.put("parameter", parameter);
            if (parameter.equals("redis_version")) {
                map.put("value", value);
                map.put("content", "redis版本 ");
            } else if (parameter.equals("aof_enabled")) {
                map.put("value", value);
                map.put("content", "Redis是否开启了aof");
            } else if (parameter.equals("used_memory_peak")) {
                map.put("value", value);
                map.put("content", "Redis所用内存的高峰值");
            } else if (parameter.equals("used_memory_peak_human")) {
                map.put("value", value);
                map.put("content", "Redis所用内存的高峰值");
            } else if (parameter.equals("used_memory_human")) {
                map.put("value", value);
                map.put("content", "Redis分配的内存总量");
            } else if (parameter.equals("connected_clients")) {
                map.put("value", value);
                map.put("content", "连接的客户端数量");
            } else if (parameter.equals("mem_fragmentation_ratio")) {
                map.put("value", value);
                map.put("content", "内存碎片比率");
            } else if (parameter.equals("used_memory")) {
                map.put("value", value);
                map.put("content", "由 Redis分配器分配的内存总量，以字节（byte）为单位");
            } else if (parameter.equals("total_connections_received")) {
                map.put("value", value);
                map.put("content", "运行以来连接过的客户端的总数量");
            } else if (parameter.equals("role")) {
                map.put("value", value);
                map.put("content", "当前实例的角色master还是slave");
            } else if (parameter.equals("keyspace_misses")) {
                map.put("value", value);
                map.put("content", "没命中key 的次数");
            } else if (parameter.equals("expired_keys")) {
                map.put("value", value);
                map.put("content", "运行以来过期的 key 的数量");
            } else if (parameter.equals("keyspace_hits")) {
                map.put("value", value);
                map.put("content", "命中key 的次数");
            } else if (parameter.equals("client_longest_output_list")) {
                map.put("value", value);
                map.put("content", "当前连接的客户端当中，最长的输出列表");
            } else if (parameter.equals("used_cpu_user_children")) {
                map.put("value", value);
                map.put("content", "后台进程耗费的用户 CPU");
            } else if (parameter.equals("uptime_in_seconds")) {
                map.put("value", value);
                map.put("content", "自 Redis 服务器启动以来，经过的秒数");
            } else if (parameter.equals("lru_clock")) {
                map.put("value", value);
                map.put("content", "以分钟为单位进行自增的时钟，用于 LRU 管理");
            } else if (parameter.equals("redis_git_sha1")) {
                map.put("value", value);
                map.put("content", "Git SHA1");
            } else if (parameter.equals("redis_git_dirty")) {
                map.put("value", value);
                map.put("content", "Git dirty flag");
            } else if (parameter.equals("latest_fork_usec")) {
                map.put("value", value);
                map.put("content", "最近一次 fork() 操作耗费的毫秒数");
            } else if (parameter.equals("used_cpu_sys")) {
                map.put("value", value);
                map.put("content", "Redis服务器耗费的系统 CPU");
            } else if (parameter.equals("used_cpu_user")) {
                map.put("value", value);
                map.put("content", "Redis服务器耗费的用户 CPU");
            } else if (parameter.equals("used_cpu_sys_children")) {
                map.put("value", value);
                map.put("content", "后台进程耗费的系统 CPU");
            } else if (parameter.equals("process_id")) {
                map.put("value", value);
                map.put("content", "服务器进程的 PID");
            } else if (parameter.equals("mem_allocator")) {
                map.put("value", value);
                map.put("content", "在编译时指定的， Redis 所使用的内存分配器。可以是 libc 、 jemalloc 或者 tcmalloc");
            } else if (parameter.equals("pubsub_channels")) {
                map.put("value", value);
                map.put("content", "目前被订阅的频道数量");
            } else if (parameter.equals("evicted_keys")) {
                map.put("value", value);
                map.put("content", "因为最大内存容量限制而被驱逐（evict）的键数量");
            } else if (parameter.equals("uptime_in_days")) {
                map.put("value", value);
                map.put("content", "自 Redis服务器启动以来，经过的天数");
            } else if (parameter.equals("blocked_clients")) {
                map.put("value", value);
                map.put("content", "正在等待阻塞命令（BLPOP、BRPOP、BRPOPLPUSH）的客户端的数量");
            } else if (parameter.equals("changes_since_last_save")) {
                map.put("value", value);
                map.put("content", "距离最近一次成功创建持久化文件之后，经过了多少秒");
            } else if (parameter.equals("multiplexing_api")) {
                map.put("value", value);
                map.put("content", "Redis所使用的事件处理机制");
            } else if (parameter.equals("bgsave_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 RDB文件");
            } else if (parameter.equals("pubsub_patterns")) {
                map.put("value", value);
                map.put("content", "目前被订阅的模式数量");
            } else if (parameter.equals("total_commands_processed")) {
                map.put("value", value);
                map.put("content", "服务器已执行的命令数量");
            } else if (parameter.equals("used_memory_rss")) {
                map.put("value", value);
                map.put("content", "从操作系统的角度，返回 Redis 已分配的内存总量（俗称常驻集大小）");
            } else if (parameter.equals("loading")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在载入持久化文件");
            } else if (parameter.equals("connected_slaves")) {
                map.put("value", value);
                map.put("content", "已连接的从服务器数量");
            } else if (parameter.equals("rdb_changes_since_last_save")) {
                map.put("value", value);
                map.put("content", "距离最近一次成功创建持久化文件之后，经过了多少秒");
            } else if (parameter.equals("rdb_last_save_time")) {
                map.put("value", value);
                map.put("content", "最近一次成功创建 RDB 文件的 UNIX 时间戳");
            } else if (parameter.equals("tcp_port")) {
                map.put("value", value);
                map.put("content", "TCP/IP 监听端口");
            } else if (parameter.equals("aof_last_rewrite_time_sec")) {
                map.put("value", value);
                map.put("content", "最近一次创建 AOF 文件耗费的时长");
            } else if (parameter.equals("run_id")) {
                map.put("value", value);
                map.put("content", "Redis 服务器的随机标识符（用于 Sentinel 和集群）");
            } else if (parameter.equals("aof_rewrite_scheduled")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了在 RDB 文件创建完毕之后，是否需要执行预约的 AOF 重写操作");
            } else if (parameter.equals("os")) {
                map.put("value", value);
                map.put("content", "Redis服务器的宿主操作系统");
            } else if (parameter.equals("rdb_last_bgsave_time_sec")) {
                map.put("value", value);
                map.put("content", "记录了最近一次创建 RDB 文件耗费的秒数");
            } else if (parameter.equals("aof_last_bgrewrite_status")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了最近一次创建 AOF 文件的结果是成功还是失败");
            } else if (parameter.equals("rdb_current_bgsave_time_sec")) {
                map.put("value", value);
                map.put("content", "如果服务器正在创建 RDB 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数");
            } else if (parameter.equals("rdb_last_bgsave_status")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了最近一次创建 RDB 文件的结果是成功还是失败");
            } else if (parameter.equals("aof_rewrite_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 AOF 文件");
            } else if (parameter.equals("aof_current_rewrite_time_sec")) {
                map.put("value", value);
                map.put("content", "如果服务器正在创建 AOF 文件，那么这个域记录的就是当前的创建操作已经耗费的秒数");
            } else if (parameter.equals("instantaneous_ops_per_sec")) {
                map.put("value", value);
                map.put("content", "服务器每秒钟执行的命令数量");
            } else if (parameter.equals("arch_bits")) {
                map.put("value", value);
                map.put("content", "架构（32 或 64 位）");
            } else if (parameter.equals("used_memory_lua")) {
                map.put("value", value);
                map.put("content", "Lua 引擎所使用的内存大小（以字节为单位）");
            } else if (parameter.equals("gcc_version")) {
                map.put("value", value);
                map.put("content", "编译 Redis 时所使用的 GCC 版本");
            } else if (parameter.equals("rdb_bgsave_in_progress")) {
                map.put("value", value);
                map.put("content", "一个标志值，记录了服务器是否正在创建 RDB 文件");
            } else if (parameter.equals("client_biggest_input_buf")) {
                map.put("value", value);
                map.put("content", " 最大输入缓冲区，单位字节，其实就是最大 qbuf");
            } else if (parameter.equals("redis_mode")) {
                map.put("value", value);
                map.put("content", "Redis运行模式，单机or集群");
            } else if (parameter.equals("rejected_connections")) {
                map.put("value", value);
                map.put("content", "因为最大客户端数量限制而被拒绝的连接请求数量");
            } else if (parameter.equals("redis_build_id")) {
                map.put("value", value);
                map.put("content", "ID");
            } else if (parameter.equals("repl_backlog_size")) {
                map.put("value", value);
                map.put("content", "一个环形缓存区,默认1024*1024大小, 即1Mb");
            } else if (parameter.equals("hz")) {
                map.put("value", value);
                map.put("content", "执行后台任务的频率,默认为10,此值越大表示redis对\"间歇性task\"的执行次数越频繁(次数/秒)");
            } else if (parameter.equals("last_save_time")) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(value + "000"));
                map.put("value", formatter.format(calendar.getTime()));
                map.put("content", "上次保存RDB文件的时间");
            } else {
                map.put("value", value);
                map.put("content", "");
            }
        }

        int rowCount = list.size();
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        return page;
    }

    public Map<String, Object> queryInfoItemForRedis(String databaseConfigId) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String info = RedisAPI.getInfo(map3);
        Properties properties = new Properties();
        InputStream inStream = new ByteArrayInputStream(info.getBytes());
        properties.load(inStream);
        Map<String, Object> map = new HashMap();
        int totalKeys = 0;
        String parameter = "";
        String value = "";
        Iterator it = properties.entrySet().iterator();

        while (it.hasNext()) {
            Entry<Object, Object> entry = (Entry) it.next();
            parameter = (String) entry.getKey();
            value = (String) entry.getValue();
            if (parameter.indexOf("db") == 0) {
                String ssi = value.substring(5, value.indexOf(","));
                totalKeys += Integer.parseInt(ssi);
            } else {
                map.put(parameter, value);
            }
        }

        int keyspace_hits = Integer.parseInt("" + properties.get("keyspace_hits"));
        int keyspace_misses = Integer.parseInt("" + properties.get("keyspace_misses"));
        int keyspace_hits_scope = 0;
        if (keyspace_misses + keyspace_hits > 0) {
            keyspace_hits_scope = Math.round((float) (keyspace_hits * 100 / (keyspace_misses + keyspace_hits)));
        }

        map.put("keyspace_hits_scope", keyspace_hits_scope);
        map.put("totalKeys", totalKeys);
        return map;
    }

    public Map<String, Object> queryInfoItemForMemcached(String databaseConfigId) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        new MemcachedUtil();
        Map<String, String> info = MemcachedUtil.memcachedStatus(ip, port, password);
        String parameter = "";
        String value = "";
        Set set = info.entrySet();
        Iterator i = set.iterator();
        HashMap map = new HashMap();

        while (i.hasNext()) {
            Entry<String, String> entry1 = (Entry) i.next();
            map.put((String) entry1.getKey(), entry1.getValue());
            if (((String) entry1.getKey()).equals("curr_connections")) {
                map.put("connected_clients", entry1.getValue());
            }

            if (((String) entry1.getKey()).equals("get_hits")) {
                map.put("get_hits", entry1.getValue());
            }

            if (((String) entry1.getKey()).equals("cmd_get")) {
                map.put("cmd_get", entry1.getValue());
            }

            if (((String) entry1.getKey()).equals("curr_items")) {
                map.put("totalKeys", entry1.getValue());
            }

            if (((String) entry1.getKey()).equals("bytes")) {
                map.put("used_memory", entry1.getValue());
                map.put("used_memory_peak", entry1.getValue());
            }

            if (((String) entry1.getKey()).equals("bytes")) {
                int size = Integer.parseInt((String) entry1.getValue());
                if (size < 1024) {
                    map.put("used_memory_human", size + "Byte");
                } else {
                    size /= 1024;
                    if (size < 1024) {
                        map.put("used_memory_human", size + "KB");
                    } else {
                        size /= 1024;
                        if (size < 1024) {
                            size *= 100;
                            map.put("used_memory_human", size / 100 + "." + size % 100 + "MB");
                        } else {
                            size = size * 100 / 1024;
                            map.put("used_memory_human", size + size / 100 + "." + size % 100 + "GB");
                        }
                    }
                }
            } else if (((String) entry1.getKey()).equals("total_items")) {
                map.put("total_commands_processed", entry1.getValue());
            }
        }

        String get_hits = "" + map.get("get_hits");
        String cmd_get = "" + map.get("cmd_get");
        int keyspace_hits_scope;
        if (cmd_get.equals("0")) {
            keyspace_hits_scope = 0;
        } else {
            keyspace_hits_scope = Math.round((float) (Integer.parseInt(get_hits) * 100 / Integer.parseInt(cmd_get)));
        }

        map.put("keyspace_hits_scope", keyspace_hits_scope);
        return map;
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForMemcached(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        List<Map<String, Object>> list = new ArrayList();
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        String password = (String) map3.get("password");
        new MemcachedUtil();
        Map<String, String> info = MemcachedUtil.memcachedStatus(ip, port, password);
        String parameter = "";
        String value = "";
        Set set = info.entrySet();

        HashMap map;
        for (Iterator i = set.iterator(); i.hasNext(); list.add(map)) {
            Entry<String, String> entry1 = (Entry) i.next();
            map = new HashMap();
            map.put("parameter", entry1.getKey());
            map.put("value", entry1.getValue());
            if (((String) entry1.getKey()).equals("version")) {
                map.put("content", "Memcached版本 ");
            } else if (((String) entry1.getKey()).equals("uptime")) {
                map.put("content", "服务器已经运行的秒数");
            } else if (((String) entry1.getKey()).equals("pointer_size")) {
                map.put("content", "当前操作系统的指针大小（32位系统一般是32bit,64就是64位操作系统） ");
            } else if (((String) entry1.getKey()).equals("rusage_system")) {
                map.put("content", "进程的累计系统时间 ");
            } else if (((String) entry1.getKey()).equals("curr_connections")) {
                map.put("content", "当前打开连接数 ");
            } else if (((String) entry1.getKey()).equals("cmd_flush")) {
                map.put("content", "flush命令请求次数 ");
            } else if (((String) entry1.getKey()).equals("get_hits")) {
                map.put("content", "总命中次数 ");
            } else if (((String) entry1.getKey()).equals("cmd_set")) {
                map.put("content", "set命令总请求次数 ");
            } else if (((String) entry1.getKey()).equals("cmd_get")) {
                map.put("content", "get命令总请求次数 ");
            } else if (((String) entry1.getKey()).equals("pid")) {
                map.put("content", "进程ID ");
            } else if (((String) entry1.getKey()).equals("total_connections")) {
                map.put("content", "曾打开的连接总数 ");
            } else if (((String) entry1.getKey()).equals("connection_structures")) {
                map.put("content", "服务器分配的连接结构数 ");
            } else if (((String) entry1.getKey()).equals("bytes_read")) {
                map.put("content", "读取字节总数 ");
            } else if (((String) entry1.getKey()).equals("limit_maxbytes")) {
                map.put("content", "分配的内存数（字节） ");
            } else if (((String) entry1.getKey()).equals("get_misses")) {
                map.put("content", "总未命中次数 ");
            } else if (((String) entry1.getKey()).equals("delete_hits")) {
                map.put("content", "delete命令命中次数 ");
            } else if (((String) entry1.getKey()).equals("incr_misses")) {
                map.put("content", "incr命令未命中次数 ");
            } else if (((String) entry1.getKey()).equals("threads")) {
                map.put("content", "当前线程数 ");
            } else if (((String) entry1.getKey()).equals("bytes")) {
                map.put("content", "当前存储占用的字节数 ");
            } else if (((String) entry1.getKey()).equals("curr_items")) {
                map.put("content", "当前存储的数据总数 ");
            } else if (((String) entry1.getKey()).equals("total_items")) {
                map.put("content", "启动以来存储的数据总数 ");
            } else {
                map.put("content", " ");
            }
        }

        int rowCount = list.size();
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        return page;
    }

    public boolean backupNotSqlDatabaseForRedis(String databaseConfigId, String path) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        String ip = (String) map3.get("ip");
        String port = (String) map3.get("port");
        RedisAPI.bgsave(map3);
        String redisDir = RedisAPI.getConfig2(map3, "dir");
        String fileName = ip + "(" + port + ")-" + DateUtils.getDateTimeString(new Date()) + ".rdb";
        File srcFile = new File(redisDir + File.separator + "dump.rdb");
        if (!srcFile.exists()) {
            throw new FileNotFoundException();
        } else {
            File tarFile = new File(path + File.separator + "backup" + File.separator + fileName);
            if (srcFile.exists()) {
                FileUtils.copyFile(srcFile, tarFile);
                return true;
            } else {
                return false;
            }
        }
    }

    public List<String> getAllDataBaseForMemcached(String ip, String port, String password) {
        new MemcachedUtil();
        return MemcachedUtil.getAllDataBaseForMemcached(ip, port, password);
    }

    public Map<String, Object> getConfig(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id,name, databaseType , databaseName, userName , passwrod as password, port, ip ,url ,isdefault from  treesoft_config where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = (Map) list.get(0);
        return map;
    }

    public boolean deleteConfig(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_config where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean flushAllForRedis(String databaseConfigId) throws Exception {
        Map<String, Object> map3 = this.getConfig(databaseConfigId);
        return RedisAPI.flushAllForRedis(map3);
    }

    public boolean authorize() throws Exception {
        DBUtil db = new DBUtil();
        String sql = " select id, computer, license,valid  from  treesoft_config   ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = (Map) list.get(0);
        String computer = (String) map.get("computer");
        String license = (String) map.get("license");
        String valid = (String) map.get("valid");
        return true;
    }

    public boolean deletePerson(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < ids.length; ++i) {
            sb = sb.append("'" + ids[i] + "',");
        }

        String newStr = sb.toString();
        String str3 = newStr.substring(0, newStr.length() - 1);
        String sql = "  delete  from  treesoft_users where id in (" + str3 + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public Map<String, Object> getPerson(String id) {
        DBUtil db = new DBUtil();
        String sql = " select id, username,  realname ,role, status, note ,expiration , permission  from  treesoft_users where id='" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = (Map) list.get(0);
        return map;
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page) throws Exception {
        int pageNo = page.getPageNo();
        int pageSize = page.getPageSize();
        int limitFrom = (pageNo - 1) * pageSize;
        DBUtil du = new DBUtil();
        List<Map<String, Object>> list = du.getPersonList();
        int rowCount = list.size();
        page.setTotalCount((long) rowCount);
        page.setResult(list);
        return page;
    }

    public boolean personUpdate(Person person) throws Exception {
        DBUtil db = new DBUtil();
        String id = person.getId();
        String sql = "";
        String username = person.getUsername();
        String password = person.getPassword();
        password = StringUtils.MD5(password + "treesoft" + username);
        if (!id.equals("")) {
            sql = " update treesoft_users  set username='" + person.getUsername() + "' ," + "realname='" + person.getRealname() + "' ," + "expiration='" + person.getExpiration() + "' ," + "permission='" + person.getPermission() + "' ," + "status='" + person.getStatus() + "' ," + "role='" + person.getRole() + "' ," + "note='" + person.getNote() + "' " + "  where id='" + id + "'";
        } else {
            sql = " insert into treesoft_users ( createDate ,username,password,realname , status,token,role,note,expiration , permission ) values ( '" + DateUtils.getDateTime() + "','" + person.getUsername() + "','" + password + "','" + person.getRealname() + "','" + person.getStatus() + "','" + StringUtils.MD5(password + "39") + "','" + person.getRole() + "','" + person.getNote() + "','" + person.getExpiration() + "','" + person.getPermission() + "' ) ";
        }

        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        DBUtil db = new DBUtil();
        String username = "";
        String password = "";
        String token = "";

        for (int i = 0; i < ids.length; ++i) {
            List<Map<String, Object>> list = this.selectUserById(ids[i]);
            username = (String) ((Map) list.get(0)).get("username");
            password = StringUtils.MD5("treesofttreesoft" + username);
            token = StringUtils.MD5(password + "39");
            String sql = "  update  treesoft_users set password='" + password + "' ,token ='" + token + "'  where id in (" + ids[i] + ")";
            db.do_update(sql);
        }

        boolean bl = true;
        return bl;
    }

    public boolean saveLog(String sql, String username, String ip) throws Exception {
        DBUtil db = new DBUtil();
        sql = sql.replace("'", "''");
        String sqls = "insert into treesoft_log( createdate,operator,username ,log, ip  ) values ( '" + DateUtils.getDateTime() + "'," + "'operator'," + "'" + username + "'," + "'" + sql + "'," + "'" + ip + "' )";
        boolean bl = db.do_update(sqls);
        return bl;
    }

    public boolean registerUpdate(TempDto tem) throws Exception {
        DBUtil db = new DBUtil();
        String personNumber = tem.getPersonNumber();
        String company = tem.getCompany();
        String token = tem.getToken();
        boolean isvalidate = false;
        boolean bl = false;
        String validToken = StringUtils.MD5(personNumber + company + "treesoft");
        if (token.equals(validToken)) {
            isvalidate = true;
        }

        String sql;
        if (isvalidate) {
            sql = "  update  treesoft_license set createDate = '" + DateUtils.getDateTime() + "' , personNumber='" + personNumber + "' , company ='" + company + "' ,token ='" + token + "', mess ='registered' ";
            bl = db.do_update(sql);
        } else {
            sql = "  update  treesoft_license set createDate = '' , personNumber='" + personNumber + "' , company ='' ,token ='', mess ='unregistered' ";
            db.do_update(sql);
            bl = false;
        }

        return bl;
    }

    public Map<String, Object> getRegisterMess() {
        DBUtil db = new DBUtil();
        String sql = " select id, personNumber, company  , token , mess   from  treesoft_license ";
        List<Map<String, Object>> list = db.executeQuery(sql);
        Map<String, Object> map = (Map) list.get(0);
        return map;
    }

    public boolean identifying() {
        Map<String, Object> map = this.getRegisterMess();
        String personNumber = "" + map.get("personNumber");
        String company = "" + map.get("company");
        String token = "" + map.get("token");
        boolean isvalidate = false;
        String validToken = StringUtils.MD5(personNumber + company + "treesoft");
        if (token.equals(validToken)) {
            isvalidate = true;
        }

        return isvalidate;
    }
}
