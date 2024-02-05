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
            /*String url = "jdbc:postgresql://10.0.0.56:5432/numr?";
            String urlOracle = "jdbc:oracle:thin:@//10.0.0.40:1521/lflifedb";*/
/*
            Properties props = new Properties();
            props.setProperty("user", "numrlfuser");
            props.setProperty("password", "L$FnuMrP0d");

            Properties propsOracle = new Properties();
            propsOracle.setProperty("user", "LFLIVE_BKP_13JAN");
            propsOracle.setProperty("password", "LFLIVE_BKP_13JAN");

            //props.setProperty("ssl", "true");
            Connection conn = DriverManager.getConnection(url, props);
            Connection connOracle = DriverManager.getConnection(urlOracle, propsOracle);

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("select * from room_types");
            ResultSetMetaData metaData = result.getMetaData();

            System.out.println("Postgress DB");
            int cols = metaData.getColumnCount();
            int rows=0;
            while (result.next()) {
                for(int i=1 ; i<cols ; i++) {
                    try {
                        System.out.print(result.getString(i) + " | ");
                    }catch (Exception e){
                        break;
                    }
                }
                System.out.println("");
                rows++;
            }
            System.out.println("Total Number Of Rows : "+rows);

            System.out.println("Oracle DB");

            Statement statementOracle = connOracle.createStatement();
            ResultSet resultOracle = statementOracle.executeQuery("select * from equipment");
            ResultSetMetaData metaDataOrcl = resultOracle.getMetaData();
            int colsOrcl = metaData.getColumnCount();
            int rowsOrcl=0;
            while (resultOracle.next()) {
                for(int i=1 ; i<colsOrcl ; i++) {
                    try {
                        System.out.print(resultOracle.getString(i) + " | ");
                    }catch (Exception e){
                        break;
                    }
                }
                System.out.println("");
                rowsOrcl++;
            }
            System.out.println("Total Number Of Rows : "+rowsOrcl);*/
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
