//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zhuxl.redis.visual.system.web;

public class IdsDto {
    private String[] ids;
    private String tableName;
    private String databaseName;
    private String NoSQLDbName;
    private String primary_key;
    private String column_name;
    private String is_nullable;
    private String column_key;
    private String column_name2;
    private String checkedItems;
    private String databaseConfigId;
    private String keyName;

    public IdsDto() {
    }

    public String getDatabaseConfigId() {
        return this.databaseConfigId;
    }

    public void setDatabaseConfigId(String databaseConfigId) {
        this.databaseConfigId = databaseConfigId;
    }

    public String getNoSQLDbName() {
        return this.NoSQLDbName;
    }

    public void setNoSQLDbName(String noSQLDbName) {
        this.NoSQLDbName = noSQLDbName;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    String getCheckedItems() {
        return this.checkedItems;
    }

    public void setCheckedItems(String checkedItems) {
        this.checkedItems = checkedItems;
    }

    public String getColumn_name2() {
        return this.column_name2;
    }

    public void setColumn_name2(String columnName2) {
        this.column_name2 = columnName2;
    }

    public String getColumn_name() {
        return this.column_name;
    }

    public void setColumn_name(String columnName) {
        this.column_name = columnName;
    }

    public String getIs_nullable() {
        return this.is_nullable;
    }

    public void setIs_nullable(String isNullable) {
        this.is_nullable = isNullable;
    }

    public String getColumn_key() {
        return this.column_key;
    }

    public void setColumn_key(String columnKey) {
        this.column_key = columnKey;
    }

    public String getPrimary_key() {
        return this.primary_key;
    }

    public void setPrimary_key(String primaryKey) {
        this.primary_key = primaryKey;
    }

    public String[] getIds() {
        return this.ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
}
