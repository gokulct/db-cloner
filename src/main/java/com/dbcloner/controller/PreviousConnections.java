package com.dbcloner.controller;

import com.dbcloner.common.IController;
import com.dbcloner.common.PreviousConnection;
import com.dbcloner.common.PreviousConnectionsManager;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class PreviousConnections extends IController {
    public ListView connectionList;
    private int type=0;//Source
    private Stage stage;

    @Override
    public void initialise() {

    }

    @Override
    public void initialise(Object[] parameter) {
        type = Integer.parseInt(parameter[0].toString());
        stage = (Stage) parameter[1];
        loadPreviousConnections();
    }

    public void loadPreviousConnections(){
        try {
            List<PreviousConnection> list = PreviousConnectionsManager.getPreviousConnections();
            for(PreviousConnection p : list){
                Label l=new Label("Username : "+p.getUsername()+"    URL:"+p.getUrl());
                l.addEventHandler(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        dashboard.loadPreviousConnection(type,p);
                        stage.close();
                    }

                });
                connectionList.getItems().add(l);
            }
        }catch (Exception e){
            showAlert(ERROR,e.getMessage());
        }
    }
}
