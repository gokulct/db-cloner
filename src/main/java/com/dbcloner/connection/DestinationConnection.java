package com.dbcloner.connection;

import com.dbcloner.data.ResultDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oracle.jdbc.driver.OracleDriver;

public class DestinationConnection {
    private static Connection connection=null;
    private DestinationConnection(){

    }
    public static Connection getInstance(String url, String userName, String password) throws SQLException, ClassNotFoundException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        if(connection==null){
            Properties props = new Properties();
            props.setProperty("user", userName);
            props.setProperty("password", password);
            connection = DriverManager.getConnection(url, props);
            return connection;
        }else{
            return connection;
        }
    }
    public static Connection getCurrentInstance() throws Exception{
        if(connection!=null){
            return connection;
        }else {
            throw new Exception("Connection Not Found");
        }
    }

    public static List<String> listTables() throws Exception {
        if(connection!=null) {
            List<String> tablesList=new ArrayList<String>();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tablesList.add(tableName);
            }
            return tablesList;
        }else {
            throw new Exception("Conection Not initialised.");
        }
    }

    public static List<String> listColumns(String tableName) throws Exception {
        if(connection!=null) {
            List<String> columnList=new ArrayList<String>();
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName.toUpperCase(), null);
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                columnList.add(columnName);
            }
            return columnList;
        }else {
            throw new Exception("Connection Not initialised.");
        }
    }
    public static ResultDetail execute(String query) throws Exception{
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        ResultSetMetaData data = resultSet.getMetaData();
        int cols = data.getColumnCount();
        List<Object[]> list = new ArrayList<Object[]>();
        int rows=0;
        boolean columnNamesCollected=false;
        List<String> columns=new ArrayList<String>();
        List<String> clobColumns = new ArrayList<String>();
        while (resultSet.next()){
            Object[] obj = new Object[cols+1];
            for(int i=1;i<=cols;i++){
                obj[i]=resultSet.getString(i);
                if(!columnNamesCollected) {
                    columns.add(data.getColumnName(i));
                    String type=data.getColumnTypeName(i);
                    if(type.equals("text") || type.equals("jsonb") || type.equals("clob")){
                        clobColumns.add(type);
                    }
                }
            }
            columnNamesCollected=true;
            list.add(obj);
            rows++;
        }
        ResultDetail detail = new ResultDetail();
        detail.setList(list);
        detail.setColumnCount(cols);
        detail.setRowCount(rows);
        detail.setColumns(columns);
        detail.setClobColumns(clobColumns);
        return detail;
    }

    public static boolean isConnected(){
        return connection!=null;
    }

    public static String getColumnType(String tableName, String columnName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();

            // Get column types for the specified table and column
            ResultSet resultSet = metaData.getColumns(null, null, tableName, columnName.toUpperCase());

            // Check if the result set is not empty
            if (resultSet.next()) {
                return resultSet.getString("TYPE_NAME");
            } else {
                System.out.println("Column not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return null in case of an error or if the column is not found
        return null;
    }

    public static void closeConnection() throws SQLException {
        if(connection!=null){
            connection.close();
            connection=null;
        }
    }
}
