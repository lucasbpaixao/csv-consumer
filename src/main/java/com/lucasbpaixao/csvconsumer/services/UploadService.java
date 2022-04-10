package com.lucasbpaixao.csvconsumer.services;

import java.util.ArrayList;
import java.util.List;

import com.lucasbpaixao.csvconsumer.models.ColumnsCreateds;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class UploadService {

    public List<String> standardizeColumnNames(List<String> columnNames){
        List<String> newColumnNames = new ArrayList<>();
        for (String columnName : columnNames) {
            columnName = columnName.toLowerCase();
            columnName = columnName.replaceAll(" ", "_");
            newColumnNames.add(columnName);
        }
        return newColumnNames;
    }

    public List<ColumnsCreateds> createColumns(List<String> columnNames, List<CSVRecord> records, String primaryKey){
        List<ColumnsCreateds> columns = new ArrayList<>();

        ColumnsCreateds primaryKeyColumn = new ColumnsCreateds();

        if(!primaryKey.isBlank()){
            primaryKeyColumn = createPrimaryKey(primaryKey, records);
        }else{
            primaryKeyColumn = containsId(columnNames, records);
        }

        columns.add(primaryKeyColumn);

        for (String columnName : columnNames) {
            if(!columns.get(0).getColumnName().equals(columnName)){
                //TODO: create a data type identifier
                // VARCHAR is the default data type
                ColumnsCreateds column = new ColumnsCreateds(columnName, "VARCHAR(255)", false, false);

                columns.add(column);
            }
        }

        return columns;
    }

    private static ColumnsCreateds createPrimaryKey(String primaryKey, List<CSVRecord> records){
        ColumnsCreateds column = new ColumnsCreateds();

        column.setColumnName(primaryKey);

        //identifies whether the data type is String or Long
        try {
            Integer.parseInt(records.get(0).get(0));
            column.setDataType("INT");
        } catch (Exception e) {
            column.setDataType("VARCHAR(255)");
        }

        column.setPrimaryKey(true);
        column.setAutoIncrement(false);

        return column;
    }

    private static ColumnsCreateds containsId(List<String> columnNames, List<CSVRecord> records){
        ColumnsCreateds column = new ColumnsCreateds();

        if(columnNames.contains("id")){
            return createPrimaryKey("id", records);
        }else{
            //Create a default id column

            column.setColumnName("id");
            column.setDataType("");
            column.setPrimaryKey(true);
            column.setAutoIncrement(true);
            
            return column;
        }
    }

}
