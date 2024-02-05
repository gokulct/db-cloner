package com.dbcloner.data;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class ColumnMap {

    public ColumnMap(){
        super();
    }
    public ColumnMap(String sourceColumn){
        this.sourceColumn=sourceColumn;
    }
    private String sourceColumn;
    private String targetColumn;
    private Boolean isClob = Boolean.FALSE;

    public String getSourceColumn() {
        return sourceColumn;
    }

    public void setSourceColumn(String sourceColumn) {
        this.sourceColumn = sourceColumn;
    }

    public String getTargetColumn() {
        return targetColumn;
    }

    public void setTargetColumn(String targetColumn) {
        this.targetColumn = targetColumn;
    }

    public Boolean getIsClob() {
        return isClob;
    }

    public void setIsClob(Boolean clob) {
        isClob = clob;
    }
}
