package com.lucasbpaixao.csvconsumer.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class TablesCreateds {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tableName;

    private String primaryKey;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ColumnsCreateds> columns;

    public TablesCreateds(){
    
    }

    public TablesCreateds(String tableName, String primaryKey, List<ColumnsCreateds> columnsCreateds){
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columns = columnsCreateds;
    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnsCreateds> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnsCreateds> columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    
}
