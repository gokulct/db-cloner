package com.dbcloner.controller;

import com.dbcloner.common.IController;
import com.dbcloner.data.ColumnMap;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

public class CopyColumns extends IController {
    public Label sourceColumn;
    public ComboBox targetColumn;
    public Button removeBtn;
    public CheckBox isClob;
    private ColumnMap columnMap;

    public void remove(ActionEvent actionEvent) {
        dashboard.removeCopyColumn(columnMap);
    }

    @Override
    public void initialise() {

    }

    @Override
    public void initialise(Object[] parameter) {
        columnMap= (ColumnMap) parameter[0];
        sourceColumn.setText(columnMap.getSourceColumn());
        List<String> list = (List<String>) parameter[1];
        targetColumn.getItems().addAll(list);
    }

    public ColumnMap getColumnMap() {
        return columnMap;
    }

    public void setColumnMap(ColumnMap columnMap) {
        this.columnMap = columnMap;
    }

    public void onchangeTarget(ActionEvent actionEvent) {
        columnMap.setTargetColumn(targetColumn.getValue().toString());
    }

    public void clobChanged(ActionEvent actionEvent) {
        columnMap.setIsClob(isClob.isSelected());
    }
}
