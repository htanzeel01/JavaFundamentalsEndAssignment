package com.example.javaend;

import com.example.javaend.Database;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application {
    private Database database = new Database();
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tanzeels R&B Store");

        VBox vbox = new VBox(10);
        vbox.setMinSize(300, 200);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Button loginButton = new Button("Login");
        errorLabel = new Label(); // Label to display error messages

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (authenticateUser(username, password)) {
                // Successful login, open main application window
                // Replace this with the code to open your main application window
                System.out.println("Login successful for user: " + username);
                // Close the login window
                primaryStage.close();
                User user = getUser(username);
                new Dashboard(user).start(primaryStage);
            } else {
                // Display an error message for incorrect login
                errorLabel.setText("Invalid username or password.");
            }
        });

        vbox.getChildren().addAll(usernameField, passwordField, loginButton, errorLabel);

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticateUser(String username, String password) {
        for (User user : database.getUsers()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
    private User getUser(String Username){
        return database.getUserbyUsername(Username);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
