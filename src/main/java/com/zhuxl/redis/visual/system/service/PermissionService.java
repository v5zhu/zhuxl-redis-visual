//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.service;

import com.alibaba.fastjson.JSONArray;
import com.zhuxl.redis.visual.common.persistence.Page;
import com.zhuxl.redis.visual.system.dao.PermissionDao;
import com.zhuxl.redis.visual.system.entity.Config;
import com.zhuxl.redis.visual.system.entity.NotSqlEntity;
import com.zhuxl.redis.visual.system.entity.Person;
import com.zhuxl.redis.visual.system.web.TempDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Service
@Transactional(
        readOnly = true
)
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;

    public PermissionService() {
    }

    public List<Map<String, Object>> getAllDataBase() throws Exception {
        return this.permissionDao.getAllDataBase();
    }

    public List<Map<String, Object>> getAllTables(String dbName) throws Exception {
        return this.permissionDao.getAllTables(dbName);
    }

    public List<Map<String, Object>> getAllViews(String dbName) throws Exception {
        return this.permissionDao.getAllViews(dbName);
    }

    public List<Map<String, Object>> getAllFuntion(String dbName) throws Exception {
        return this.permissionDao.getAllFuntion(dbName);
    }

    public List<Map<String, Object>> getTableColumns(String dbName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns(dbName, tableName);
    }

    public Page<Map<String, Object>> getData(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        return this.permissionDao.getData(page, tableName, dbName);
    }

    public Page<Map<String, Object>> executeSql(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        return this.permissionDao.executeSql(page, sql, dbName);
    }

    public List<Map<String, Object>> executeSqlForColumns(String sql, String dbName) throws Exception {
        return this.permissionDao.executeSqlForColumns(sql, dbName);
    }

    public boolean saveSearchHistory(String name, String sql, String dbName) {
        return this.permissionDao.saveSearchHistory(name, sql, dbName);
    }

    public boolean updateSearchHistory(String id, String name, String sql, String dbName) {
        return this.permissionDao.updateSearchHistory(id, name, sql, dbName);
    }

    public boolean deleteSearchHistory(String id) {
        return this.permissionDao.deleteSearchHistory(id);
    }

    public List<Map<String, Object>> selectSearchHistory() {
        return this.permissionDao.selectSearchHistory();
    }

    public boolean configUpdate(Config config) throws Exception {
        return this.permissionDao.configUpdate(config);
    }

    public int executeSqlNotRes(String sql, String dbName) throws Exception {
        return this.permissionDao.executeSqlNotRes(sql, dbName);
    }

    public int deleteRows(String databaseName, String tableName, String primary_key, String[] ids) throws Exception {
        return this.permissionDao.deleteRows(databaseName, tableName, primary_key, ids);
    }

    public int deleteRowsNew(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        return this.permissionDao.deleteRowsNew(databaseName, tableName, primary_key, condition);
    }

    public int saveRows(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveRows(map, databaseName, tableName);
    }

    public List<Map<String, Object>> getOneRowById(String databaseName, String tableName, String id, String idValues) {
        return this.permissionDao.getOneRowById(databaseName, tableName, id, idValues);
    }

    public int updateRows(Map map, String databaseName, String tableName, String id, String idValues) throws Exception {
        return this.permissionDao.updateRows(map, databaseName, tableName, id, idValues);
    }

    public int updateRowsNew(String databaseName, String tableName, List<String> strList) throws Exception {
        String sql = "";
        Iterator var6 = strList.iterator();

        while (var6.hasNext()) {
            String str1 = (String) var6.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }

            sql = " update  " + databaseName + "." + tableName + str1;
            this.permissionDao.executeSqlNotRes(sql, databaseName);
        }

        return 0;
    }

    public String getViewSql(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getViewSql(databaseName, tableName);
    }

    public List<Map<String, Object>> getTableColumns2(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns2(databaseName, tableName);
    }

    public List<Map<String, Object>> getTableColumns3(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns3(databaseName, tableName);
    }

    public String getPrimaryKeys(String databaseName, String tableName) {
        return this.permissionDao.getPrimaryKeys(databaseName, tableName);
    }

    public boolean testConn(String databaseType, String databaseName, String ip, String port, String user, String pass) throws Exception {
        return this.permissionDao.testConn(databaseType, databaseName, ip, port, user, pass);
    }

    public List<Map<String, Object>> selectSqlStudy() {
        return this.permissionDao.selectSqlStudy();
    }

    public int saveDesginColumn(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveDesginColumn(map, databaseName, tableName);
    }

    public int deleteTableColumn(String databaseName, String tableName, String[] ids) throws Exception {
        return this.permissionDao.deleteTableColumn(databaseName, tableName, ids);
    }

    @Transactional
    public int updateTableColumn(String updated, String databaseName, String tableName) throws Exception {
        if (updated != null) {
            JSONArray updateArray = JSONArray.parseArray(updated);

            for (int i = 0; i < updateArray.size(); ++i) {
                Map<String, Object> map1 = (Map) updateArray.get(i);
                Map<String, Object> maps = new HashMap();
                Iterator var9 = map1.keySet().iterator();

                String idValues;
                while (var9.hasNext()) {
                    idValues = (String) var9.next();
                    maps.put(idValues, map1.get(idValues));
                }

                idValues = "" + maps.get("TREESOFTPRIMARYKEY");
                this.permissionDao.updateTableColumn(maps, databaseName, tableName, "column_name", idValues);
            }
        }

        return 0;
    }

    public int savePrimaryKey(String databaseName, String tableName, String column_name, String column_key) throws Exception {
        return this.permissionDao.savePrimaryKey(databaseName, tableName, column_name, column_key);
    }

    public int updateTableNullAble(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        return this.permissionDao.updateTableNullAble(databaseName, tableName, column_name, is_nullable);
    }

    public int upDownColumn(String databaseName, String tableName, String column_name, String column_name2) throws Exception {
        return this.permissionDao.upDownColumn(databaseName, tableName, column_name, column_name2);
    }

    public List<Map<String, Object>> selectUserByName(String userName) {
        List<Map<String, Object>> list = this.permissionDao.selectUserByName(userName);
        return list;
    }

    public String changePassUpdate(String userId, String newPass) throws Exception {
        this.permissionDao.updateUserPass(userId, newPass);
        return "success";
    }

    public List<Map<String, Object>> getAllDataBaseForOracle() throws Exception {
        return this.permissionDao.getAllDataBaseForOracle();
    }

    public List<Map<String, Object>> getAllTablesForOracle(String dbName) throws Exception {
        return this.permissionDao.getAllTablesForOracle(dbName);
    }

    public List<Map<String, Object>> getTableColumns3ForOracle(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns3ForOracle(databaseName, tableName);
    }

    public String getViewSqlForOracle(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getViewSqlForOracle(databaseName, tableName);
    }

    public List<Map<String, Object>> getAllViewsForOracle(String dbName) throws Exception {
        return this.permissionDao.getAllViewsForOracle(dbName);
    }

    public List<Map<String, Object>> getAllFuntionForOracle(String dbName) throws Exception {
        return this.permissionDao.getAllFuntionForOracle(dbName);
    }

    public Page<Map<String, Object>> getDataForOracle(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        return this.permissionDao.getDataForOracle(page, tableName, dbName);
    }

    public Page<Map<String, Object>> executeSqlHaveResForOracle(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        return this.permissionDao.executeSqlHaveResForOracle(page, sql, dbName);
    }

    public int updateRowsNewForOracle(String databaseName, String tableName, List<String> strList) throws Exception {
        String sql = "";
        Iterator var6 = strList.iterator();

        while (var6.hasNext()) {
            String str1 = (String) var6.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }

            sql = " update  " + tableName + str1;
            this.permissionDao.executeSqlNotRes(sql, databaseName);
        }

        return 0;
    }

    public int updateTableNullAbleForOracle(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        return this.permissionDao.updateTableNullAbleForOracle(databaseName, tableName, column_name, is_nullable);
    }

    public int savePrimaryKeyForOracle(String databaseName, String tableName, String column_name, String column_key) throws Exception {
        return this.permissionDao.savePrimaryKeyForOracle(databaseName, tableName, column_name, column_key);
    }

    public int saveDesginColumnForOracle(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveDesginColumnForOracle(map, databaseName, tableName);
    }

    @Transactional
    public int updateTableColumnForOracle(String updated, String databaseName, String tableName) throws Exception {
        if (updated != null) {
            JSONArray updateArray = JSONArray.parseArray(updated);

            for (int i = 0; i < updateArray.size(); ++i) {
                Map<String, Object> map1 = (Map) updateArray.get(i);
                Map<String, Object> maps = new HashMap();
                Iterator var9 = map1.keySet().iterator();

                String idValues;
                while (var9.hasNext()) {
                    idValues = (String) var9.next();
                    maps.put(idValues, map1.get(idValues));
                }

                idValues = "" + maps.get("TREESOFTPRIMARYKEY");
                this.permissionDao.updateTableColumnForOracle(maps, databaseName, tableName, "column_name", idValues);
            }
        }

        return 0;
    }

    public int deleteTableColumnForOracle(String databaseName, String tableName, String[] ids) throws Exception {
        return this.permissionDao.deleteTableColumnForOracle(databaseName, tableName, ids);
    }

    public int saveRowsForOracle(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveRowsForOracle(map, databaseName, tableName);
    }

    public String selectColumnTypeForOracle(String databaseName, String tableName, String column) throws Exception {
        return this.permissionDao.selectColumnTypeForOracle(databaseName, tableName, column);
    }

    public int deleteRowsNewForOracle(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        return this.permissionDao.deleteRowsNewForOracle(databaseName, tableName, primary_key, condition);
    }

    public List<Map<String, Object>> getAllDataBaseForPostgreSQL() throws Exception {
        return this.permissionDao.getAllDataBaseForPostgreSQL();
    }

    public List<Map<String, Object>> getAllTablesForPostgreSQL(String dbName) throws Exception {
        return this.permissionDao.getAllTablesForPostgreSQL(dbName);
    }

    public List<Map<String, Object>> getTableColumns3ForPostgreSQL(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns3ForPostgreSQL(databaseName, tableName);
    }

    public List<Map<String, Object>> getAllViewsForPostgreSQL(String dbName) throws Exception {
        return this.permissionDao.getAllViewsForPostgreSQL(dbName);
    }

    public List<Map<String, Object>> getAllFuntionForPostgreSQL(String dbName) throws Exception {
        return this.permissionDao.getAllFuntionForPostgreSQL(dbName);
    }

    public int updateRowsNewForPostgreSQL(String databaseName, String tableName, List<String> strList) throws Exception {
        String sql = "";
        Iterator var6 = strList.iterator();

        while (var6.hasNext()) {
            String str1 = (String) var6.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }

            sql = " update  " + tableName + str1;
            this.permissionDao.executeSqlNotRes(sql, databaseName);
        }

        return 0;
    }

    public int saveDesginColumnForPostgreSQL(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveDesginColumnForPostgreSQL(map, databaseName, tableName);
    }

    @Transactional
    public int updateTableColumnForPostgreSQL(String updated, String databaseName, String tableName) throws Exception {
        if (updated != null) {
            JSONArray updateArray = JSONArray.parseArray(updated);

            for (int i = 0; i < updateArray.size(); ++i) {
                Map<String, Object> map1 = (Map) updateArray.get(i);
                Map<String, Object> maps = new HashMap();
                Iterator var9 = map1.keySet().iterator();

                String idValues;
                while (var9.hasNext()) {
                    idValues = (String) var9.next();
                    maps.put(idValues, map1.get(idValues));
                }

                idValues = "" + maps.get("TREESOFTPRIMARYKEY");
                this.permissionDao.updateTableColumnForPostgreSQL(maps, databaseName, tableName, "column_name", idValues);
            }
        }

        return 0;
    }

    public int deleteTableColumnForPostgreSQL(String databaseName, String tableName, String[] ids) throws Exception {
        return this.permissionDao.deleteTableColumnForPostgreSQL(databaseName, tableName, ids);
    }

    public int updateTableNullAbleForPostgreSQL(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        return this.permissionDao.updateTableNullAbleForPostgreSQL(databaseName, tableName, column_name, is_nullable);
    }

    public int savePrimaryKeyForPostgreSQL(String databaseName, String tableName, String column_name, String column_key) throws Exception {
        return this.permissionDao.savePrimaryKeyForPostgreSQL(databaseName, tableName, column_name, column_key);
    }

    public String getViewSqlForPostgreSQL(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getViewSqlForPostgreSQL(databaseName, tableName);
    }

    public List<Map<String, Object>> getAllDataBaseForMSSQL() throws Exception {
        return this.permissionDao.getAllDataBaseForMSSQL();
    }

    public List<Map<String, Object>> getAllTablesForMSSQL(String dbName) throws Exception {
        return this.permissionDao.getAllTablesForMSSQL(dbName);
    }

    public List<Map<String, Object>> getTableColumns3ForMSSQL(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getTableColumns3ForMSSQL(databaseName, tableName);
    }

    public List<Map<String, Object>> getAllViewsForMSSQL(String dbName) throws Exception {
        return this.permissionDao.getAllViewsForMSSQL(dbName);
    }

    public List<Map<String, Object>> getAllFuntionForMSSQL(String dbName) throws Exception {
        return this.permissionDao.getAllFuntionForMSSQL(dbName);
    }

    public Page<Map<String, Object>> getDataForMSSQL(Page<Map<String, Object>> page, String tableName, String dbName) throws Exception {
        return this.permissionDao.getDataForMSSQL(page, tableName, dbName);
    }

    public Page<Map<String, Object>> executeSqlHaveResForMSSQL(Page<Map<String, Object>> page, String sql, String dbName) throws Exception {
        return this.permissionDao.executeSqlHaveResForMSSQL(page, sql, dbName);
    }

    public int deleteRowsNewForMSSQL(String databaseName, String tableName, String primary_key, List<String> condition) throws Exception {
        return this.permissionDao.deleteRowsNewForMSSQL(databaseName, tableName, primary_key, condition);
    }

    public String getViewSqlForMSSQL(String databaseName, String tableName) throws Exception {
        return this.permissionDao.getViewSqlForMSSQL(databaseName, tableName);
    }

    public int saveRowsForMSSQL(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveRowsForMSSQL(map, databaseName, tableName);
    }

    public int updateRowsNewForMSSQL(String databaseName, String tableName, List<String> strList) throws Exception {
        String sql = "";
        Iterator var6 = strList.iterator();

        while (var6.hasNext()) {
            String str1 = (String) var6.next();
            if (str1 == null || "".equals(str1)) {
                throw new Exception("数据不完整,保存失败!");
            }

            sql = " update  " + tableName + str1;
            this.permissionDao.executeSqlNotRes(sql, databaseName);
        }

        return 0;
    }

    public int saveDesginColumnForMSSQL(Map map, String databaseName, String tableName) throws Exception {
        return this.permissionDao.saveDesginColumnForPostgreSQL(map, databaseName, tableName);
    }

    @Transactional
    public int updateTableColumnForMSSQL(String updated, String databaseName, String tableName) throws Exception {
        if (updated != null) {
            JSONArray updateArray = JSONArray.parseArray(updated);

            for (int i = 0; i < updateArray.size(); ++i) {
                Map<String, Object> map1 = (Map) updateArray.get(i);
                Map<String, Object> maps = new HashMap();
                Iterator var9 = map1.keySet().iterator();

                String idValues;
                while (var9.hasNext()) {
                    idValues = (String) var9.next();
                    maps.put(idValues, map1.get(idValues));
                }

                idValues = "" + maps.get("TREESOFTPRIMARYKEY");
                this.permissionDao.updateTableColumnForMSSQL(maps, databaseName, tableName, "column_name", idValues);
            }
        }

        return 0;
    }

    public int deleteTableColumnForMSSQL(String databaseName, String tableName, String[] ids) throws Exception {
        return this.permissionDao.deleteTableColumnForMSSQL(databaseName, tableName, ids);
    }

    public int updateTableNullAbleForMSSQL(String databaseName, String tableName, String column_name, String is_nullable) throws Exception {
        return this.permissionDao.updateTableNullAbleForMSSQL(databaseName, tableName, column_name, is_nullable);
    }

    public int savePrimaryKeyForMSSQL(String databaseName, String tableName, String column_name, String column_key) throws Exception {
        return this.permissionDao.savePrimaryKeyForMSSQL(databaseName, tableName, column_name, column_key);
    }

    public List<Map<String, Object>> selectBackupList(String path) {
        List<Map<String, Object>> list = new ArrayList();
        List<File> files = getFileSort(path);
        Iterator var5 = files.iterator();

        while (var5.hasNext()) {
            File file = (File) var5.next();
            Map<String, Object> map = new HashMap();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map.put("fileName", file.getName());
            map.put("fileLength", (float) file.length() / 1024.0F + " KB");
            map.put("fileModifiedDate", df.format(file.lastModified()));
            list.add(map);
        }

        return list;
    }

    public static List<File> getFileSort(String path) {
        List<File> list = getFiles(path, new ArrayList());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else {
                        return file.lastModified() == newFile.lastModified() ? 0 : -1;
                    }
                }
            });
        }

        return list;
    }

    public static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            File[] var7 = subfiles;
            int var6 = subfiles.length;

            for (int var5 = 0; var5 < var6; ++var5) {
                File file = var7[var5];
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    public boolean backupDatabaseExecute(String databaseName, String path) throws Exception {
        return this.permissionDao.backupDatabaseExecute(databaseName, path);
    }

    public boolean deleteBackupFile(String[] ids, String path) throws Exception {
        return this.permissionDao.deleteBackupFile(ids, path);
    }

    public List<Map<String, Object>> getAllDataBaseForMemcached(String databaseConfigId) throws Exception {
        new ArrayList();
        new HashMap();
        List<Map<String, Object>> listAll = new ArrayList();
        new ArrayList();
        Map<String, Object> map = this.permissionDao.getConfig(databaseConfigId);
        String dbName = (String) map.get("ip");
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String password = (String) map.get("password");
        Map<String, Object> tempObject = new HashMap();
        new HashMap();

        try {
            int id = 0;
            id = id + 1;
            tempObject.put("id", id);
            tempObject.put("name", dbName);
            tempObject.put("type", "db");
            tempObject.put("icon", "icon-hamburg-database");
            listAll.add(tempObject);
            int pid = id;
            List<String> list1 = this.permissionDao.getAllDataBaseForMemcached(ip, port, password);

            for (int y = 0; y < list1.size(); ++y) {
                Map<String, Object> tempObjectTable2 = new HashMap();
                ++id;
                tempObjectTable2.put("id", id);
                tempObjectTable2.put("pid", pid);
                tempObjectTable2.put("name", "items" + (String) list1.get(y));
                tempObjectTable2.put("icon", "icon-berlin-project");
                tempObjectTable2.put("type", "table");
                listAll.add(tempObjectTable2);
            }

            return listAll;
        } catch (Exception var18) {
            return null;
        }
    }

    public List<Map<String, Object>> getAllDataBaseForReids(String databaseConfigId) throws Exception {
        List<Map<String, Object>> listAll = new ArrayList();
        Map<String, Object> map = this.permissionDao.getConfig(databaseConfigId);
        String dbName = (String) map.get("ip");
        Map<String, Object> tempObject = new HashMap();
        new HashMap();

        try {
            int id = 0;
            id = id + 1;
            tempObject.put("id", id);
            tempObject.put("name", dbName);
            tempObject.put("type", "database");
            tempObject.put("icon", "icon-hamburg-database");
            listAll.add(tempObject);
            int pid = id;
            int dbAmount = this.permissionDao.getDbAmountForRedis(map);

            for (int y = 0; y < dbAmount; ++y) {
                Map<String, Object> tempObjectTable2 = new HashMap();
                ++id;
                tempObjectTable2.put("id", id);
                tempObjectTable2.put("pid", pid);
                tempObjectTable2.put("name", "DB" + y);
                tempObjectTable2.put("icon", "icon-berlin-project");
                tempObjectTable2.put("type", "table");
                listAll.add(tempObjectTable2);
            }

            return listAll;
        } catch (Exception var12) {
            System.out.println("取得reids中数据库的数量出错,可能数据库连接参数配置有误！\n " + var12.getMessage());
            return null;
        }
    }

    public Page<Map<String, Object>> getNoSQLDBForRedis(Page<Map<String, Object>> page, String NoSQLDbName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        return this.permissionDao.getNoSQLDBForRedis(page, NoSQLDbName, databaseConfigId, selectKey, selectValue);
    }

    public Page<Map<String, Object>> getNoSQLDBForMemcached(Page<Map<String, Object>> page, String tableName, String databaseConfigId, String selectKey, String selectValue) throws Exception {
        return this.permissionDao.getNoSQLDBForMemcached(page, tableName, databaseConfigId, selectKey, selectValue);
    }

    public Page<Map<String, Object>> configList(Page<Map<String, Object>> page) throws Exception {
        return this.permissionDao.configList(page);
    }

    public Map<String, Object> getConfig(String id) throws Exception {
        return this.permissionDao.getConfig(id);
    }

    public List<Map<String, Object>> getConfigAllDataBase() throws Exception {
        return this.permissionDao.getConfigAllDataBase();
    }

    public int deleteNoSQLKeyForRedis(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        return this.permissionDao.deleteNoSQLKeyForRedis(databaseConfigId, NoSQLDbName, ids);
    }

    public int deleteNoSQLKeyForMemcached(String databaseConfigId, String NoSQLDbName, String[] ids) throws Exception {
        return this.permissionDao.deleteNoSQLKeyForMemcached(databaseConfigId, NoSQLDbName, ids);
    }

    public boolean saveNotSqlDataForRedis(NotSqlEntity notSqlEntity, String databaseConfigId, String NoSQLDbName) throws Exception {
        return this.permissionDao.saveNotSqlDataForRedis(notSqlEntity, databaseConfigId, NoSQLDbName);
    }

    public boolean saveNotSqlDataForMemcached(NotSqlEntity notSqlEntity, String databaseConfigId) throws Exception {
        return this.permissionDao.saveNotSqlDataForMemcached(notSqlEntity, databaseConfigId);
    }

    public NotSqlEntity selectNotSqlDataForRedis(String key, String NoSQLDbName, String databaseConfigId) throws Exception {
        NotSqlEntity notSqlEntity = new NotSqlEntity();
        new HashMap();
        Map<String, Object> map = this.permissionDao.selectNotSqlDataForRedis(key, NoSQLDbName, databaseConfigId);
        notSqlEntity.setKey(key);
        String type = (String) map.get("type");
        String value = "";
        notSqlEntity.setType((String) map.get("type"));
        notSqlEntity.setExTime((String) map.get("exTime"));
        if (type.equals("string")) {
            notSqlEntity.setValue((String) map.get("value"));
        }

        if (type.equals("list")) {
            notSqlEntity.setList((List) map.get("value"));
        }

        if (type.equals("set")) {
            String temp = (String) map.get("value");
            temp = temp.substring(1, temp.length() - 1);
            String[] arr = temp.split(",");
            notSqlEntity.setList(Arrays.asList(arr));
        }

        if (type.equals("zset")) {
            Object temp = map.get("value");
            Set<Tuple> set = (Set) temp;
            Iterator<Tuple> itt = set.iterator();
            String ss = "";
            ArrayList listMap = new ArrayList();

            while (itt.hasNext()) {
                Tuple str = (Tuple) itt.next();
                Map<String, Object> mm = new HashMap();
                mm.put("valuek", str.getScore());
                mm.put("valuev", str.getElement());
                listMap.add(mm);
            }

            notSqlEntity.setListMap(listMap);
        }

        if (type.equals("hash")) {
            Map<String, String> map5 = (Map) map.get("value");
            List<Map<String, Object>> listMap = new ArrayList();
            Iterator var20 = map5.entrySet().iterator();

            while (var20.hasNext()) {
                Entry<String, String> entry = (Entry) var20.next();
                Map<String, Object> mm = new HashMap();
                mm.put("valuek", entry.getKey());
                mm.put("valuev", entry.getValue());
                listMap.add(mm);
            }

            notSqlEntity.setListMap(listMap);
        }

        return notSqlEntity;
    }

    public NotSqlEntity selectNotSqlDataForMemcached(String key, String NoSQLDbName, String databaseConfigId) throws Exception {
        NotSqlEntity notSqlEntity = new NotSqlEntity();
        new HashMap();
        Map<String, Object> map = this.permissionDao.selectNotSqlDataForMemcached(key, databaseConfigId);
        notSqlEntity.setKey(key);
        String type = (String) map.get("type");
        String value = "";
        notSqlEntity.setType((String) map.get("type"));
        if (type.equals("String")) {
            notSqlEntity.setValue((String) map.get("value"));
        }

        if (type.equals("list")) {
            value = "does not support this type data for display!";
            notSqlEntity.setValue(value);
        }

        if (type.equals("set")) {
            value = "does not support this type data for display!";
            notSqlEntity.setValue(value);
        }

        if (type.equals("zset")) {
            value = "does not support this type data for display!";
            notSqlEntity.setValue(value);
        }

        if (type.equals("hash")) {
            value = "does not support this type data for display!";
            notSqlEntity.setValue(value);
        }

        String temp;
        String[] arr;
        if (type.equals("HashSet")) {
            temp = (String) map.get("value");
            temp = temp.substring(1, temp.length() - 1);
            arr = temp.split(",");
            notSqlEntity.setList(Arrays.asList(arr));
        }

        if (type.equals("HashMap")) {
            temp = (String) map.get("value");
            List<Map<String, Object>> listMap = new ArrayList();
            temp = temp.substring(1, temp.length() - 1);
            arr = temp.split(",");
            String[] var14 = arr;
            int var13 = arr.length;

            for (int var12 = 0; var12 < var13; ++var12) {
                String str = var14[var12];
                Map<String, Object> mm = new HashMap();
                String[] tem = str.split("=");
                mm.put("valuek", tem[0]);
                mm.put("valuev", tem[1]);
                listMap.add(mm);
            }

            notSqlEntity.setListMap(listMap);
        }

        if (type.equals("ArrayList")) {
            temp = (String) map.get("value");
            temp = temp.substring(1, temp.length() - 1);
            arr = temp.split(",");
            notSqlEntity.setList(Arrays.asList(arr));
        }

        return notSqlEntity;
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForRedis(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        return this.permissionDao.selectNoSQLDBStatusForRedis(page, databaseConfigId);
    }

    public Page<Map<String, Object>> selectNoSQLDBStatusForMemcached(Page<Map<String, Object>> page, String databaseConfigId) throws Exception {
        return this.permissionDao.selectNoSQLDBStatusForMemcached(page, databaseConfigId);
    }

    public boolean backupNotSqlDatabaseForRedis(String databaseConfigId, String path) throws Exception {
        return this.permissionDao.backupNotSqlDatabaseForRedis(databaseConfigId, path);
    }

    public Map<String, Object> queryInfoItemForRedis(String databaseConfigId) throws Exception {
        return this.permissionDao.queryInfoItemForRedis(databaseConfigId);
    }

    public boolean flushAllForRedis(String databaseConfigId) throws Exception {
        return this.permissionDao.flushAllForRedis(databaseConfigId);
    }

    public Map<String, Object> queryInfoItemForMemcached(String databaseConfigId) throws Exception {
        return this.permissionDao.queryInfoItemForMemcached(databaseConfigId);
    }

    public String getMemoryConsumptionForRedis(String databaseConfigId) throws Exception {
        return "";
    }

    public boolean deleteConfig(String[] ids) throws Exception {
        return this.permissionDao.deleteConfig(ids);
    }

    public boolean authorize() throws Exception {
        return this.permissionDao.authorize();
    }

    public boolean personUpdate(Person person) throws Exception {
        return this.permissionDao.personUpdate(person);
    }

    public boolean deletePerson(String[] ids) throws Exception {
        return this.permissionDao.deletePerson(ids);
    }

    public Map<String, Object> getPerson(String id) throws Exception {
        return this.permissionDao.getPerson(id);
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page) throws Exception {
        return this.permissionDao.personList(page);
    }

    public boolean resetPersonPass(String[] ids) throws Exception {
        return this.permissionDao.resetPersonPass(ids);
    }

    public boolean saveLog(String sql, String username, String ip) throws Exception {
        return this.permissionDao.saveLog(sql, username, ip);
    }

    public boolean registerUpdate(TempDto tem) throws Exception {
        return this.permissionDao.registerUpdate(tem);
    }

    public Map<String, Object> getRegisterMess() {
        return this.permissionDao.getRegisterMess();
    }

    public boolean identifying() {
        return this.permissionDao.identifying();
    }
}
