package com.dbcloner.controller;

import com.dbcloner.common.IController;
import com.dbcloner.common.PreviousConnection;
import com.dbcloner.common.PreviousConnectionsManager;
import com.dbcloner.components.AutoCompleteTextField;
import com.dbcloner.connection.CopyUtils;
import com.dbcloner.connection.DestinationConnection;
import com.dbcloner.connection.SourceConnection;
import com.dbcloner.data.ColumnMap;
import com.dbcloner.data.ResultDetail;
import com.dbcloner.main.Start;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dashboard extends IController {
    public TextField sourceUrl;
    public TextField sourceUser;
    public TextField sourcePassword;
    public Button sourceConnectBtn;
    public Label sourceConnectionStatus;
    public TextField destinationUrl;
    public TextField destinationUser;
    public Button destinationConnectBtn;
    public Label destinationConnectionStatus;
    public TextField destinationPassword;
    public ProgressBar progress;
    public Button previousSourcesBtn;
    public Button previousDestinationBtn;
    public ListView sourceColumnsList;
    public Button addQueryBtn;
    public Button startCopyBtn;
    public ListView copyColumnsList;
    public List<ColumnMap> columnMaps = new ArrayList<ColumnMap>();
    public AutoCompleteTextField targetTable;
    public BorderPane targetTablePanel;
    public Label status;
    public Button refreshBtn;
    ResultDetail resultDetail;
    private static boolean copySuccess=false;
    private SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm");
    private String startTime;

    public void sourceConnect(ActionEvent actionEvent) {
        Platform.runLater(()->{
            showLoader();
            try{
                if(sourceUrl.getText().toString().isEmpty() ||
                        sourceUser.getText().toString().isEmpty() ||
                        sourcePassword.getText().toString().isEmpty()){
                    showAlert(ERROR,"Please Fill Required Fields.");
                }
                Connection connection = SourceConnection.getInstance(sourceUrl.getText().toString(),
                        sourceUser.getText().toString(),sourcePassword.getText().toString());
                if(connection!=null) {
                    sourceConnectionStatus.setText("Connected");
                    sourceConnectionStatus.setStyle("-fx-text-fill:green;");
                    PreviousConnectionsManager.saveConnection(sourceUrl.getText().toString(),sourceUser.getText().toString(),
                            sourcePassword.getText().toString());
                }else{
                    sourceConnectionStatus.setText("Failed to connect");
                    sourceConnectionStatus.setStyle("-fx-text-fill:red;");
                }
            }catch (Exception e){
                showAlert(ERROR,"Error Occured : "+e.getMessage());
                sourceConnectionStatus.setText("Failed to connect");
                sourceConnectionStatus.setStyle("-fx-text-fill:red;");
            }
            hideLoader();
        });

    }

    public void destinationConnect(ActionEvent actionEvent) {
        Platform.runLater(()->{
            showLoader();
            try{
                if(destinationUrl.getText().toString().isEmpty() ||
                        destinationUser.getText().toString().isEmpty() ||
                        destinationPassword.getText().toString().isEmpty()){
                    showAlert(ERROR,"Please Fill Required Fields.");
                }
                Connection connection = DestinationConnection.getInstance(destinationUrl.getText().toString(),
                        destinationUser.getText().toString(),destinationPassword.getText().toString());
                if(connection!=null) {
                    destinationConnectionStatus.setText("Connected");
                    destinationConnectionStatus.setStyle("-fx-text-fill:green;");
                    PreviousConnectionsManager.saveConnection(destinationUrl.getText().toString(),destinationUser.getText().toString(),
                            destinationPassword.getText().toString());
                    populateDestinationTables();
                }else{
                    destinationConnectionStatus.setText("Failed to connect");
                    destinationConnectionStatus.setStyle("-fx-text-fill:red;");
                }
            }catch (Exception e){
                showAlert(ERROR,"Error Occured : "+e.getMessage());
                destinationConnectionStatus.setText("Failed to connect");
                destinationConnectionStatus.setStyle("-fx-text-fill:red;");
            }
            hideLoader();
        });
    }
    private void populateDestinationTables(){
        try {
            List<String> tables = DestinationConnection.listTables();
            targetTable.getEntries().clear();
            for(String s : tables) {
                targetTable.getEntries().add(s.toLowerCase());
            }
        }catch (Exception e){
            e.printStackTrace();
            showAlert(ERROR,e.getMessage());
        }
    }
    @Override
    public void initialise() {
        dashboard=this;
        sourceConnectionStatus.setText("");
        destinationConnectionStatus.setText("");
        targetTable=new AutoCompleteTextField();
        targetTablePanel.setCenter(targetTable);
        hideLoader();
    }

    @Override
    public void initialise(Object[] parameter) {
        dashboard=this;
        sourceConnectionStatus.setText("");
        destinationConnectionStatus.setText("");
        targetTable=new AutoCompleteTextField();
        targetTablePanel.setCenter(targetTable);
        hideLoader();
    }

    public void showPreviousSources(ActionEvent actionEvent) {
        try {
            Stage previous = new Stage();
            Scene s = new Scene(loadFXML("previousConnections", new Object[]{0,previous}, previous));
            previous.setScene(s);
            previous.setAlwaysOnTop(true);
            previous.show();
        }catch (Exception e){
            showAlert(ERROR,e.getMessage());
        }
    }

    public void showPreviousDestinations(ActionEvent actionEvent) {
        try {
            Stage previous = new Stage();
            Scene s = new Scene(loadFXML("previousConnections", new Object[]{1,previous}, previous));
            previous.setScene(s);
            previous.setAlwaysOnTop(true);
            previous.show();
        }catch (Exception e){
            showAlert(ERROR,e.getMessage());
        }
    }

    public void loadPreviousConnection(int type, PreviousConnection previous){
        if(type==0){
            sourceUrl.setText(previous.getUrl());
            sourceUser.setText(previous.getUsername());
            sourcePassword.setText(previous.getPassword());
        } else if (type==1) {
            destinationUrl.setText(previous.getUrl());
            destinationUser.setText(previous.getUsername());
            destinationPassword.setText(previous.getPassword());
        }
    }

    public void addQuery(ActionEvent actionEvent) {
        try {
            if(!DestinationConnection.isConnected()) {
                showAlertPopup(ERROR,"Please Connect Source Database First");
                return;
            }
            Stage stage = new Stage();
            Scene s = new Scene(loadFXML("pasteQuery", new Object[]{this,stage}, stage));
            stage.setScene(s);
            //stage.setAlwaysOnTop(true);
            stage.show();
        }catch (Exception e){
            showAlert(ERROR,e.getMessage());
        }
    }

    private void createCopyColumns(){
        try {
            copyColumnsList.getItems().clear();
            List<String> columns = DestinationConnection.listColumns(targetTable.getText().toString().toUpperCase());
            for (ColumnMap c : columnMaps) {
                FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/ui/" + "copyColumns" + ".fxml"));
                Parent p = fxmlLoader.load();
                BorderPane view = new BorderPane();
                CopyColumns item = fxmlLoader.getController();
                item.initialise(new Object[]{c,columns});
                view.setCenter(p);
                copyColumnsList.getItems().add(view);
            }
        }catch (Exception e){
            e.printStackTrace();
            showAlert(ERROR,e.getMessage());
        }
    }
    public void removeCopyColumn(ColumnMap columnMap){
        columnMaps.remove(columnMap);
        Platform.runLater(()->{
            copyColumnsList.getItems().clear();
            createCopyColumns();
        });
    }
    public void addCopyColumns(String source){
        Platform.runLater(()->{
            try {
                for(ColumnMap c : columnMaps){
                    if(c.getSourceColumn().equals(source)){
                        return;
                    }
                }
                if(targetTable.getText().isEmpty()){
                    showConfirmationDialog("Please Select Target Table.");
                    return;
                }
                columnMaps.add(new ColumnMap(source));
                createCopyColumns();
            }catch (Exception e){
                e.printStackTrace();
                showAlert(ERROR,e.getMessage());
            }
        });
    }
    public void populateSourceColumns(ResultDetail detail){
        resultDetail=detail;
        Platform.runLater(()->{
            columnMaps.clear();
            sourceColumnsList.getItems().clear();
            for(String s :detail.getColumns()){
                Label label = new Label(s);
                label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    addCopyColumns(s);
                });
                sourceColumnsList.getItems().add(label);
            }
            createCopyColumns();
        });
    }

    public void startCopying(ActionEvent actionEvent) {
        if(columnMaps.isEmpty()){
            showAlert(ERROR,"No Columns Selected To Copy.");
            return;
        }
        final ResultDetail detail = resultDetail;
        final List<ColumnMap> maps = columnMaps;
        final String table=targetTable.getText().toUpperCase();
        showLoader();
        System.out.println("Process Started at "+sdf.format(new Date()));
        startTime=sdf.format(new Date());
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Platform.runLater(()->{
            sourceConnectBtn.setDisable(true);
            destinationConnectBtn.setDisable(true);
            previousSourcesBtn.setDisable(true);
            previousDestinationBtn.setDisable(true);
            startCopyBtn.setDisable(true);
            addQueryBtn.setDisable(true);
            refreshBtn.setDisable(true);
        });
        // Submit a task to the ExecutorService
        CompletableFuture<?> future = CompletableFuture.runAsync(()->{
            try {
                copySuccess=CopyUtils.startCopying(detail,maps,table);
            } catch (Exception e) {
                e.printStackTrace();
            }
        },executorService);

        // Add an onSuccess event listener
        future.thenRun(() -> {
            if(copySuccess){
                System.out.println("Process Ended at "+sdf.format(new Date()));
                status.setText("Process Complete. Started on "+startTime+" and ended on "+sdf.format(new Date()));
                showAlertPopup(INFORMATION,"Process Complete. Started on "+startTime+" and ended on "+sdf.format(new Date()));
            }else {
                showAlertPopup(ERROR,"Process Failed To Complete.");
            }
            Platform.runLater(()->{
                sourceConnectBtn.setDisable(false);
                destinationConnectBtn.setDisable(false);
                previousSourcesBtn.setDisable(false);
                previousDestinationBtn.setDisable(false);
                startCopyBtn.setDisable(false);
                addQueryBtn.setDisable(false);
                refreshBtn.setDisable(false);
            });
            hideLoader();
        });
        executorService.shutdown();

    }

    public void onchangeTargetTable(ActionEvent actionEvent) {
    }

    public void refreshAll(ActionEvent actionEvent) {
        try {
            SourceConnection.closeConnection();
            DestinationConnection.closeConnection();
            Platform.runLater(()->{
                sourceConnectionStatus.setText("Disconnected");
                destinationConnectionStatus.setText("Disconnected");
                columnMaps.clear();
                createCopyColumns();
                targetTable.getEntries().clear();
                sourceColumnsList.getItems().clear();
                status.setText("");
                sourceConnectBtn.setDisable(false);
                destinationConnectBtn.setDisable(false);
                previousSourcesBtn.setDisable(false);
                previousDestinationBtn.setDisable(false);
                startCopyBtn.setDisable(false);
                addQueryBtn.setDisable(false);
                refreshBtn.setDisable(false);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
