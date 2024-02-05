package com.dbcloner.controller;

import com.dbcloner.common.IController;
import com.dbcloner.connection.SourceConnection;
import com.dbcloner.data.ResultDetail;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasteQuery extends IController {
    public TextArea qeryText;
    public ProgressBar progressBar;
    Stage stage;
    static ResultDetail resultDetail;
    static boolean fetchSuccess=false;
    static String errorMesage="";

    public void getColumns(ActionEvent actionEvent) {
        fetchSuccess=false;
        errorMesage="";
        progressBar.setProgress(-1);
        final String query=qeryText.getText().toString();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Submit a task to the ExecutorService
        CompletableFuture<?> future = CompletableFuture.runAsync(()->{
            try {
                ResultDetail detail = SourceConnection.execute(query);
                fetchSuccess=true;
                dashboard.populateSourceColumns(detail);
            } catch (Exception e) {
                errorMesage=e.getMessage();
                e.printStackTrace();
            }
        },executorService);

        // Add an onSuccess event listener
        future.thenRun(() -> {
            Platform.runLater(()->{
                progressBar.setProgress(0);
                if(!fetchSuccess) {
                    showAlert(ERROR,errorMesage);
                }else {
                    stage.close();
                }
            });
        });
        executorService.shutdown();
        /*Platform.runLater(()->{
            try {
                ResultDetail detail = SourceConnection.execute(qeryText.getText().toString());
                //hideLoader();
                progressBar.setProgress(0);
            }catch (Exception e){
                hideLoader();
                showAlert(ERROR,e.getMessage());
            }
        });*/
    }

    @Override
    public void initialise() {

    }

    @Override
    public void initialise(Object[] parameter) {
        stage= (Stage) parameter[1];
    }
}
