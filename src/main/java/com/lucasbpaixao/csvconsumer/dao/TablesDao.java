package com.lucasbpaixao.csvconsumer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.lucasbpaixao.csvconsumer.models.ColumnsCreateds;
import com.lucasbpaixao.csvconsumer.models.TablesCreateds;

import org.apache.commons.csv.CSVRecord;
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

    public boolean createTable(String tableName, List<ColumnsCreateds> columns) throws SQLException{

        String columnsSql = createColumnsSql(columns, connection.getClass().toString());

        System.out.println("CREATE TABLE " + tableName + " (" + columnsSql);

        PreparedStatement preparedStatement = this.connection.prepareStatement("CREATE TABLE " + tableName + " (" + columnsSql);

        return preparedStatement.execute();
    }

    private static String createInsertSql(TablesCreateds tablesCreateds, List<CSVRecord> csvRecords){
        String query = "INSERT INTO " + tablesCreateds.getTableName() + " ";
        String columns = "(";
        String data = "";
        List<ColumnsCreateds> columnsCreateds = tablesCreateds.getColumns();

        int i = 1;

        for(ColumnsCreateds columnCreated : columnsCreateds){
            if(!columnCreated.getAutoCreated()){
                columns += columnCreated.getColumnName();

                if(i < columnsCreateds.size()){
                    columns += ", ";
                }else{
                    columns += ")";
                }
            }

            i++;
        }

        i = 1;
        data += "(";
        for(ColumnsCreateds columnCreated : columnsCreateds){
            if(!columnCreated.getAutoCreated() && i <= columnsCreateds.size()){
                data += "?";

                if(i < columnsCreateds.size()){
                    data += ",";
                }else{
                    data += ");";
                }
            }
            i++;
        }

        System.out.println(query + columns + " VALUES " + data);
        return query + columns + " VALUES " + data;
    }

    public boolean saveData(TablesCreateds tablesCreateds, List<CSVRecord> csvRecords) throws SQLException{
        
        List<ColumnsCreateds> columnsCreateds = tablesCreateds.getColumns();

        int quantityColumns = columnsCreateds.size();
        int quantityRecords = csvRecords.size();
        
        for (CSVRecord csvRecord : csvRecords) {
            int i = 1;
            String sql = createInsertSql(tablesCreateds, csvRecords);
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            int j = 0;
            for(ColumnsCreateds columnCreated : columnsCreateds){
                if(!columnCreated.getAutoCreated() && j < quantityColumns && i <= (quantityRecords * quantityColumns)){
                    preparedStatement.setObject(i, csvRecord.get(j));
                    i++;
                    j++;
                }
                
                
            }
            preparedStatement.execute();
        }
        
        return true;
    }

    private static String createColumnsSql(List<ColumnsCreateds> columns, String connectionClass) throws SQLException{
        String columnString = "";
        int i = 1;

        for (ColumnsCreateds column : columns) {
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
