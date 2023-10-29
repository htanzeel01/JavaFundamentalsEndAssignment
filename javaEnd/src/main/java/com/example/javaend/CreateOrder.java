package com.example.javaend;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CreateOrder extends Application {
    private User user;
    private Database database;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private ObservableList<Product> selectedProducts;
    private TextField quantityField;
    private ObservableList<Integer> quantitiesList;

    public CreateOrder(User user, Database database) {
        this.user = user;
        this.database = database;
        firstNameField = new TextField();
        lastNameField = new TextField();
        emailField = new TextField();
        phoneField = new TextField();
        selectedProducts = FXCollections.observableArrayList();
        quantityField = new TextField();
        quantitiesList = FXCollections.observableArrayList();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Order");

        VBox orderVBox = new VBox(10);
        orderVBox.setMinSize(800, 800);

        // Title
        Label titleLabel = new Label("Create Order");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Customer Information
        Label customerInfoLabel = new Label("Customer Information");
        customerInfoLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Fields for customer information
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label emailLabel = new Label("Email:");
        Label phoneLabel = new Label("Phone Number:");

        // Design the order view with customer info, product list, and buttons
        Button addProductButton = new Button("Add Product");
        Button createOrderButton = new Button("Create Order");
        //TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");


        addProductButton.setOnAction(e -> {
            // Display a pop-up window for selecting products
            Stage productSelectionStage = new Stage();
            productSelectionStage.setTitle("Select Products");


            ListView<Product> productListView = new ListView<>();
            productListView.getItems().addAll(database.getProducts());
            // Create a custom cell factory to display product information
            productListView.setCellFactory(createProductCellFactory());


            Button addToOrderButton = new Button("Add to Order");
            Button closeWindowButton = new Button("Close");

            addToOrderButton.setOnAction(event -> {
                Product selectedProduct = productListView.getSelectionModel().getSelectedItem();
                String quantityText = quantityField.getText();
                addtoOrder(selectedProduct,quantityText);
            });

            closeWindowButton.setOnAction(event -> productSelectionStage.close());


            VBox productSelectionVBox = new VBox(10);
            productSelectionVBox.getChildren().addAll(productListView,quantityField, addToOrderButton, closeWindowButton);

            Scene productSelectionScene = new Scene(productSelectionVBox, 300, 300);
            productSelectionStage.setScene(productSelectionScene);
            productSelectionStage.show();
        });
        // Create a ListView to display the selected products
        ListView<Product> selectedProductsListView = new ListView<>(selectedProducts);
        selectedProductsListView.setMinSize(400, 200);
        selectedProductsListView.setCellFactory(createProductCellFactory());


        createOrderButton.setOnAction(e -> {
           createOrder();
        });
        CommonButtons commonButtons = new CommonButtons(user,primaryStage,database);
        orderVBox.getChildren().addAll(
                titleLabel,
                customerInfoLabel,
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                addProductButton,
                createOrderButton,
                selectedProductsListView,
                commonButtons
        );

        BorderPane root = new BorderPane();

        BorderPane topContainer = new BorderPane();
        topContainer.setLeft(commonButtons);

        root.setTop(topContainer);
        root.setCenter(orderVBox);

        Scene orderScene = new Scene(root);
        primaryStage.setScene(orderScene);
        primaryStage.show();
    }

    // Create a method to create a custom cell factory for displaying products
    private Callback<ListView<Product>, ListCell<Product>> createProductCellFactory() {
        return param -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                if (empty || product == null) {
                    setText(null);
                } else {
                    // Display product information
                    String productInfo = String.format(
                            "%s - Price: %.2f - Stock: %d - Description: %s",
                            product.getName(), product.getPrice(), product.getStock(), product.getDescription()
                    );
                    setText(productInfo);
                }
            }
        };
    }
    private void createOrder(){
        // Gather customer information from text fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();

        if (isValidCustomerInformation(firstName, lastName, email, phone)) {
            // Create a Customer object based on the information
            Customer customer = new Customer(firstName, lastName, email, phone);

            // Create an Order object with the customer and selected products
            Order order = new Order(customer, selectedProducts, quantitiesList);

            // Add the order to the database
            database.addOrders(order);

            // Close the order view
            //primaryStage.close();
            database.viewOrders();
        }
    }
    private void addtoOrder(Product selectedProduct,String quantityText){
        if (selectedProduct != null && !quantityText.isEmpty()) {
            try {
                int quantity = Integer.parseInt(quantityText);
                if (quantity > 0) {
                    // Add the selected product to the list of selected products
                    selectedProducts.add(selectedProduct);

                    // Add the corresponding quantity to a separate list
                    quantitiesList.add(quantity);

                    // Clear the quantity field
                    quantityField.clear();
                } else {
                    System.out.println("Quantity must be a positive number.");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid quantity input. Please enter a number.");
            }
        } else {
            System.out.println("Please select a product and enter a quantity.");
        }
    }
    private boolean isValidCustomerInformation(String firstName, String lastName, String email, String phone) {
        boolean isValid = true;

        // Check first name
        if (firstName.isEmpty()) {
            firstNameField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            firstNameField.setStyle(null);
        }

        // Check last name
        if (lastName.isEmpty()) {
            lastNameField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            lastNameField.setStyle(null);
        }

        // Check email format
        if (!isValidEmail(email)) {
            emailField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            emailField.setStyle(null);
        }

        // Check phone number
        if (phone.isEmpty()) {
            phoneField.setStyle("-fx-border-color: red;");
            isValid = false;
        } else {
            phoneField.setStyle(null);
        }

        return isValid;
    }
    // Method to validate email format
    private boolean isValidEmail(String email) {
        // You can add custom email validation logic here
        // For a simple check, we're verifying that the email contains "@" and "."
        return email.contains("@") && email.contains(".");
    }


    public static void main(String[] args) {
        // You can use this method to launch the Create Order view
        launch(args);
    }
}

