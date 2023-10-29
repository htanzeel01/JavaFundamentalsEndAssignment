package com.example.javaend;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static javafx.application.Application.launch;

public class OrderHistory extends Application {
    private User user;
    Database database;
    public OrderHistory(User user, Database database){
        this.user = user;
        this.database = database;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Order History");
        BorderPane orderHistoryView = new BorderPane();

// Create the first ListView for order summaries
        ListView<String> orderSummaryListView = new ListView<>();
        orderHistoryView.setCenter(orderSummaryListView);

// Create the second ListView for order details
        ListView<String> orderDetailsListView = new ListView<>();
        orderHistoryView.setBottom(orderDetailsListView);
        List<Order> orders = database.getOrders();

// Populate the first ListView (order summaries)
        ObservableList<String> orderSummaries = FXCollections.observableArrayList();
        for (Order order : orders) {
            String orderSummary = String.format("Date/Time: %s - Name: %s - Total Price: %.2f",
                    order.getOrdertime(), order.getCustomer().getFirstname(), calculateTotalPrice(orders));
            orderSummaries.add(orderSummary);
        }
        orderSummaryListView.setItems(orderSummaries);
        orderSummaryListView.setOnMouseClicked(event -> {
            String selectedOrderSummary = orderSummaryListView.getSelectionModel().getSelectedItem();
            if (selectedOrderSummary != null) {
                // Parse the selected order to retrieve the corresponding Order object
                int selectedIndex = orderSummaryListView.getSelectionModel().getSelectedIndex();
                Order selectedOrder = orders.get(selectedIndex);


                // Format and display order details in the second ListView
                orderDetailsListView.getItems().clear();
                orderDetailsListView.getItems().addAll(getOrderDetails(selectedOrder));
            }
        });
        CommonButtons commonButtons = new CommonButtons(user,primaryStage,database);
        orderHistoryView.setLeft(commonButtons);
        // Show the primary stage
        Scene scene = new Scene(orderHistoryView, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
    public double calculateTotalPrice(List<Order> orders) {
        double totalPrice = 0.0;
        for (Order orderItem : orders) {
            for (int i = 0; i < orderItem.getProducts().size(); i++) {
                Product product = orderItem.getProducts().get(i);
                int quantity = orderItem.getQauntity().get(i);
                totalPrice += product.getPrice() * quantity;
            }
        }
        return totalPrice;
    }
    public List<String> getOrderDetails(Order order) {
        List<String> details = new ArrayList<>();
        for (int i = 0; i < order.getProducts().size(); i++) {
            Product product = order.getProducts().get(i);
            int quantity = order.getQauntity().get(i);
            String detail = String.format("Name: %s - Quantity: %d - Price: %.2f - Description: %s",
                    product.getName(), quantity, product.getPrice(), product.getDescription());
            details.add(detail);
        }
        return details;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
