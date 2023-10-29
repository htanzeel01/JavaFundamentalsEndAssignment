package com.example.javaend;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Dashboard extends Application {
    private User user;
    private Database database;

    public Dashboard(User user) {
        this.user = user;
        this.database = new Database();

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");

        VBox mainAppVBox = new VBox(10);
        mainAppVBox.setMinSize(400, 300);

        Label userLabel = new Label("User: " + user.getUsername());
        Label roleLabel = new Label("Role: " + user.getRole());
        Label dateTimeLabel = new Label("Date and Time: " + getCurrentDateTime());

        // Create an instance of the CommonButtons class and pass the User object
        CommonButtons commonButtons = new CommonButtons(user,primaryStage,database);

        mainAppVBox.getChildren().addAll(userLabel, roleLabel, dateTimeLabel, commonButtons);


        Scene mainAppScene = new Scene(mainAppVBox);
        primaryStage.setScene(mainAppScene);
        primaryStage.show();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
