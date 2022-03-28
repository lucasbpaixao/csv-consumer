package com.lucasbpaixao.csvconsumer.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

import com.lucasbpaixao.csvconsumer.models.Column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TablesDao {

    @Autowired
    private DaoFactory daoFactory;

    private Connection connection;
    
    public void startConnection() throws SQLException{
        this.connection = daoFactory.connection();
    }

    public boolean createTable(String tableName, List<Column> columns) throws SQLException{

        String columnsSql = createColumnsSql(columns, connection.getClass().toString());

        System.out.println("CREATE TABLE " + tableName + " (" + columnsSql);

        PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE " + tableName + " (" + columnsSql);

        System.out.println(connection.getClass().toString());

        return preparedStatement.execute();
    }

    private static String createColumnsSql(List<Column> columns, String connectionClass) throws SQLException{
        String columnString = "";
        int i = 1;

        for (Column column : columns) {
            columnString += column.getColumnName() + " " + column.getDataType();
            
            if(column.getAutoIncrement() && connectionClass.equals("class org.postgresql.jdbc.PgConnection")){
                columnString += "SERIAL";
            }else if(column.getAutoIncrement()){
                columnString += " INT AUTO_INCREMENT";
            }
            
            if(column.getPrimaryKey()){
                columnString += " PRIMARY KEY";
            }
            
            if(i < columns.size()){
                columnString += ", ";
            }else{
                columnString += ")";
            }
            i++;
        }

        return columnString;
    } 

    public void closeConnection() throws SQLException{
        this.connection.close();
    }
    
}