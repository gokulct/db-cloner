package com.dbcloner.connection;

import com.dbcloner.data.ColumnMap;
import com.dbcloner.data.ResultDetail;

import java.io.StringReader;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.SQLType;
import java.util.ArrayList;
import java.util.List;

public class CopyUtils {
    public static boolean success=false;

    public static Boolean startCopying(ResultDetail resultDetail, List<ColumnMap> columnMaps,String table) throws Exception{
        List<String> insertQueries = new ArrayList<String>();
        for(int i = 0; i< resultDetail.getRowCount() ; i++){
            String query = "INSERT INTO "+table+" (";
            boolean first=true;
            String params ="";
            List<Integer> colIndices = new ArrayList<Integer>();
            for(ColumnMap s : columnMaps){
                if(first) {
                    query+=s.getTargetColumn().toUpperCase();
                    params+="?";
                    first=false;
                }else{
                    query+=","+s.getTargetColumn().toUpperCase();
                    params+=",?";
                }
                int colIndex = findColumnIndex(resultDetail, s.getTargetColumn());
                colIndices.add(colIndex);
            }
            query+=") VALUES ("+params+")";
            insertQueries.add(query);
            PreparedStatement preparedStatement = DestinationConnection.getCurrentInstance().prepareStatement(query);
            Object[] row = resultDetail.getList().get(i);
            List<Object> targetData=new ArrayList<Object>();
            for(Integer in : colIndices){
                targetData.add(row[in.intValue()]);
            }
            int j=1;
            for(Object data : targetData){
                if (data != null) {
                    if (columnMaps.get(j-1).getIsClob()) {
                        preparedStatement.setClob(j, new StringReader(data.toString()));
                    } else {
                        preparedStatement.setObject(j, data);
                    }
                } else {
                    preparedStatement.setObject(j, null);
                }
                j++;
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }
        return true;
    }
    private static Boolean isClob(String table,String columnName){
        String type=DestinationConnection.getColumnType(table, columnName);
        if(type!=null && (type.equals("CLOB"))){
            return true;
        }
        return false;
    }

    public static int findColumnIndex(ResultDetail resultDetail,String column){
        int i=1;
        for(String col : resultDetail.getColumns()){
            if(col.toUpperCase().equals(column.toUpperCase())){
                return i;
            }
            i++;
        }
        return 0;
    }
}
