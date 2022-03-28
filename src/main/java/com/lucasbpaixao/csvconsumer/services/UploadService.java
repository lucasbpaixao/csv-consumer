package com.lucasbpaixao.csvconsumer.services;

import java.util.ArrayList;
import java.util.List;

import com.lucasbpaixao.csvconsumer.models.Column;

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

    public List<Column> creatColumns(List<String> columnNames, List<CSVRecord> records){
        List<Column> columns = new ArrayList<>();

        columns.add(containsId(columnNames, records));
        for (String columnName : columnNames) {
            if(!columns.get(0).getColumnName().equals(columnName)){
                //TODO: create a data type identifier
                // VARCHAR is the default data type
                Column column = new Column(columnName, "VARCHAR(255)", false, false);

                columns.add(column);
            }
        }

        return columns;
    }

    public Column containsId(List<String> columnNames, List<CSVRecord> records){
        Column column = new Column();

        if(columnNames.contains("id")){
            column.setColumnName("id");

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
