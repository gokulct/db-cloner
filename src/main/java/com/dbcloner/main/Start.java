package com.dbcloner.main;

import com.dbcloner.common.BaseController;
import com.dbcloner.context.ApplicationContext;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Start extends Application {
    private static Stage mainStage;
    public static void main(String[] args) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            File logs=new File(System.getProperty("user.dir")+"/logs");
            if(!logs.exists()){
                logs.mkdirs();
            }
            try {
                PrintStream out = new PrintStream(new FileOutputStream(System.getProperty("user.dir") + "/logs/log_"+sdf.format(new Date())+".txt", true));
                System.setErr(out);
            }catch (Exception e){
                e.printStackTrace();
            }
            launch();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        if(mainStage!=null){
            if(mainStage.isShowing()){
                return;
            }
        }
        ApplicationContext.primaryStage=primaryStage;
        mainStage=primaryStage;

        BaseController baseController = new BaseController();
        Scene s = new Scene(baseController.loadFXML("dashboard", new Object[]{primaryStage},primaryStage));
        primaryStage.setScene(s);
        primaryStage.setMaximized(true);
        primaryStage.setTitle("DB Cloner");
        //primaryStage.getIcons().add(new Image("/images/logo.png"));
        primaryStage.show();
    }
}
