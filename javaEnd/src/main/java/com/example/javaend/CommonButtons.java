package com.example.javaend;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CommonButtons extends VBox {
    public CommonButtons(User user, Stage primaryStage, Database database) {
        Button createOrderButton = new Button("Create Order");
        Button orderHistoryButton = new Button("Order History");
        Button productInventoryButton = new Button("Product Inventory");
        Button returnToMainWindowButton = new Button("Main Window");

        createOrderButton.setOnAction(e -> {
            // Check if the user is an admin
            if (user.getRole().equals(UserRole.Admin)) {
                // Close the Dashboard view
                primaryStage.close();
                // Open the CreateOrderView for admin
                new CreateOrder(user, database).start(primaryStage);
            } else {
                System.out.println("oops");
            }
        });


        orderHistoryButton.setOnAction(e -> {
            // Implement order history functionality
            // Check if the user is an admin
            if (user.getRole().equals(UserRole.Admin)) {
                // Close the Dashboard view
                primaryStage.close();
                // Open the CreateOrderView for admin
                new OrderHistory(user, database).start(primaryStage);
            } else {
                System.out.println("oops");
            }
        });

        productInventoryButton.setOnAction(e -> {
            if (user.getRole().equals(UserRole.Admin)) {
                // Close the Dashboard view
                primaryStage.close();
                // Open the CreateOrderView for admin
                new ProductInventory(user, database).start(primaryStage);
            } else {
                System.out.println("oops");
            }
        });

        returnToMainWindowButton.setOnAction(e -> {
            // Return to the main window
            new Dashboard(user).start(primaryStage);
        });
        getChildren().addAll(createOrderButton, orderHistoryButton, productInventoryButton, returnToMainWindowButton);
    }
}

