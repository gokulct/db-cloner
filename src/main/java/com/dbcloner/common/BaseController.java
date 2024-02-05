package com.dbcloner.common;

import com.dbcloner.context.ApplicationContext;
import com.dbcloner.controller.Dashboard;
import com.dbcloner.main.Start;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * Author Gokul CT
 */

public class BaseController {
    public static String ERROR = "ERROR";
    public static String INFORMATION = "INFORMATION";
    public static Dashboard dashboard;
    JSONParser parser = new JSONParser();
    public Parent loadFXML(String fxml,Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/ui/" + fxml + ".fxml"));
        Parent p = fxmlLoader.load();
        try {
            Object ob = fxmlLoader.getController();
            if (ob instanceof IController) {
                ((IController) ob).initialise();
            }
            ApplicationContext.stages.add(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return p;
    }
    public Parent loadFXML(String fxml, Object[] parameter, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Start.class.getResource("/ui/" + fxml + ".fxml"));
        Parent p = fxmlLoader.load();
        try {
            Object ob = fxmlLoader.getController();
            if (ob instanceof IController) {
                ((IController) ob).initialise(parameter);
            }
            ApplicationContext.stages.add(stage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return p;
    }

    public Boolean isPdfOrTxt(File f){
        try {
            int i = f.getName().toString().toLowerCase().lastIndexOf(".");
            return (f.getName().toString().substring(i, f.getName().toString().length()).toLowerCase().equals(".pdf") ||
                    f.getName().toString().substring(i, f.getName().toString().length()).toLowerCase().equals(".txt"));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getFileType(File f){
        try {
            int i = f.getName().toString().toLowerCase().lastIndexOf(".");
            return f.getName().toString().substring(i, f.getName().toString().length()).toLowerCase();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public boolean showConfirmationDialog(String question){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, question, ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-border-color: grey; -fx-border-width: 1; -fx-border-radius: 0;");
        alert.setHeaderText("Confirm");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            return true;
        }else{
            return false;
        }
    }
    public void showAlertPopup(String type, final String message) {
        final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        if (type.equals(ERROR)) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.getDialogPane().getScene().setFill(Color.GREEN);
                    alert.getDialogPane().setStyle("-fx-border-color: grey; -fx-border-width: 1; -fx-border-radius: 0;");
                    alert.setTitle("Error");
                    alert.setHeaderText("Error occured");
                    alert.setContentText(message);
                    alert.setX(bounds.getMaxX() - 370);
                    alert.setY(bounds.getMaxY() - 200);
                    // Creating a DropShadow effect
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(3);
                    dropShadow.setOffsetX(5);
                    dropShadow.setOffsetY(5);
                    dropShadow.setColor(Color.GRAY);

                    // Applying the DropShadow effect to the Alert dialog pane
                    alert.getDialogPane().setEffect(dropShadow);
                    alert.show();
                    //alert.hide();
                }
            });
        } else if (type.equals(INFORMATION)) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.initStyle(StageStyle.UNDECORATED);
                    alert.getDialogPane().getScene().setFill(Color.GREEN);
                    alert.getDialogPane().setStyle("-fx-border-color: grey; -fx-border-width: 1; -fx-border-radius: 0;");
                    alert.setTitle("Information");
                    alert.setHeaderText("Information");
                    alert.setContentText(message);
                    alert.setX(bounds.getMaxX() - 370);
                    alert.setY(bounds.getMaxY() - 200);

                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(3);
                    dropShadow.setOffsetX(5);
                    dropShadow.setOffsetY(5);
                    dropShadow.setColor(Color.GRAY);

                    // Applying the DropShadow effect to the Alert dialog pane
                    alert.getDialogPane().setEffect(dropShadow);
                    alert.show();
                    //alert.hide();
                }
            });
        }
    }

    public void showAlert(String type, String message) {
        try {
            if(message!=null && message!="") {
                if (type.equals(ERROR)) {
                    Popup popup = new Popup();
                    popup.setAutoFix(true);
                    popup.setAutoHide(true);
                    popup.setHideOnEscape(true);

                    BorderPane container = new BorderPane();
                    container.setPrefWidth(300);
                    container.setPrefHeight(60);
                    Text text = new Text(message);
                    text.setStyle("-fx-fill:white;-fx-font-weight:bold;");
                    container.setCenter(text);
                    container.setStyle("-fx-background-color: #ef5350;-fx-padding:10px 10px 10px 10px;");
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(3);
                    dropShadow.setOffsetX(3);
                    dropShadow.setOffsetY(3);
                    dropShadow.setColor(Color.GREY);
                    container.setEffect(dropShadow);

                    popup.getContent().add(container);
                    popup.setY(ApplicationContext.primaryStage.getY()+ApplicationContext.primaryStage.getHeight() - 80);
                    popup.setX(ApplicationContext.primaryStage.getX() + 10);
                    popup.show(ApplicationContext.primaryStage);
                } else if (type.equals(INFORMATION)) {
                    Popup popup = new Popup();
                    popup.setAutoFix(true);
                    popup.setAutoHide(true);
                    popup.setHideOnEscape(true);

                    BorderPane container = new BorderPane();
                    container.setPrefWidth(300);
                    container.setPrefHeight(60);
                    Text text = new Text(message);
                    text.setStyle("-fx-fill:white;-fx-font-weight:bold;");
                    container.setCenter(text);
                    container.setStyle("-fx-background-color: #009688;-fx-padding:10px 10px 10px 10px;");
                    DropShadow dropShadow = new DropShadow();
                    dropShadow.setRadius(3);
                    dropShadow.setOffsetX(3);
                    dropShadow.setOffsetY(3);
                    dropShadow.setColor(Color.GREY);
                    container.setEffect(dropShadow);

                    popup.getContent().add(container);
                    popup.setY(ApplicationContext.primaryStage.getY()+ApplicationContext.primaryStage.getHeight() - 80);
                    popup.setX(ApplicationContext.primaryStage.getX() + 10);
                    popup.show(ApplicationContext.primaryStage);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public BorderPane getPage(Parent p){
        BorderPane view=new BorderPane();
        try{
            view= (BorderPane) p;
        }catch(Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public void showLoader(){
        dashboard.progress.setProgress(-1);
        dashboard.progress.setVisible(true);
    }

    public void hideLoader(){
        dashboard.progress.setVisible(false);
    }
    public String readFileToString(File f){
        try {
            String json = parser.parse(new FileReader(f.getAbsolutePath())).toString();
            return json;
        }catch (Exception e){
            return null;
        }
    }

}
