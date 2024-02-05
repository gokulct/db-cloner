package com.dbcloner.data;

import java.util.ArrayList;
import java.util.List;

public class ResultDetail {
    private String query;
    private List<Object[]> list=new ArrayList<Object[]>();
    private int columnCount=0;
    private int rowCount=0;
    private List<String> columns=new ArrayList<>();
    private List<String> clobColumns=new ArrayList<>();

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getClobColumns() {
        return clobColumns;
    }

    public void setClobColumns(List<String> clobColumns) {
        this.clobColumns = clobColumns;
    }
}
