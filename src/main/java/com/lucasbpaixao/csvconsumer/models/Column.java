package com.lucasbpaixao.csvconsumer.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Column {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String columnName;

    private String dataType;

    private boolean primaryKey;

    private boolean autoIncrement;

    public Column(){}

    public Column(String columnName, String dataType, boolean primaryKey, boolean autoIncrement){
        this.columnName = columnName;
        this.dataType = dataType;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean getAutoIncrement() {
        return this.autoIncrement;
    }

    public Long getId() {
        return this.id;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public String getDataType() {
        return this.dataType;
    }

    public boolean getPrimaryKey(){
        return this.primaryKey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

}
