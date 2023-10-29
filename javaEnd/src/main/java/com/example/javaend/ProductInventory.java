package com.example.javaend;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Optional;

public class ProductInventory extends Application {
    private ObservableList<Product> products = FXCollections.observableArrayList();
    private User user;
    Database database;

    public ProductInventory(User user, Database database){
        this.user = user;
        this.database = database;
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Edit Inventory");

        VBox root = new VBox(10);

        // Create TableView for displaying products
        TableView<Product> productTableView = new TableView<>();
        populateTable(productTableView);
        // Create buttons for adding and deleting products
        Button addButton = new Button("Add Product");
        setAddButtonAction(addButton, productTableView);


        Button deleteButton = new Button("Delete Product");
        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Deletion");
                alert.setHeaderText("Delete Product");
                alert.setContentText("Are you sure you want to delete this product: " + selectedProduct.getName() + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    database.removeProduct(selectedProduct);

                }
            }
            //products.clear();
            populateTable(productTableView);
        });
        Button editButton = new Button("Edit Product");
        setEditButtonAction(editButton, productTableView);


        CommonButtons commonButtons = new CommonButtons(user,primaryStage,database);
        // Add UI elements to the root layout
        root.getChildren().addAll(productTableView,addButton, deleteButton,editButton,commonButtons);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void populateTable( TableView<Product> productTableView){
        productTableView.getItems().clear();
        productTableView.getColumns().clear();
        products = FXCollections.observableArrayList(database.getProducts());

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        productTableView.getColumns().addAll(nameColumn, priceColumn, stockColumn, descriptionColumn);
        productTableView.setItems(products);

    }
    private void setAddButtonAction(Button addButton, TableView<Product> productTableView) {
        addButton.setOnAction(e -> {
            // Create a new stage for adding a product
            Stage addStage = new Stage();
            addStage.setTitle("Add Product");

            // Create input fields for adding a new product
            TextField nameFieldAdd = new TextField();
            TextField descriptionFieldAdd = new TextField();
            TextField priceFieldAdd = new TextField();
            TextField stockFieldAdd = new TextField();

            nameFieldAdd.setPromptText("Name");
            descriptionFieldAdd.setPromptText("Description");
            priceFieldAdd.setPromptText("Price");
            stockFieldAdd.setPromptText("Stock");

            // Create a "Save" button to save the new product
            Button saveButtonAdd = new Button("Save");
            setSaveButtonAction(saveButtonAdd, nameFieldAdd, descriptionFieldAdd, priceFieldAdd, stockFieldAdd, addStage, productTableView);

            // Create a layout for the adding window
            VBox addLayout = new VBox(10);
            addLayout.getChildren().addAll(nameFieldAdd, descriptionFieldAdd, priceFieldAdd, stockFieldAdd, saveButtonAdd);

            Scene addScene = new Scene(addLayout, 300, 200);
            addStage.setScene(addScene);
            addStage.show();
        });
    }

    private void setSaveButtonAction(Button saveButton, TextField nameField, TextField descriptionField, TextField priceField, TextField stockField, Stage addStage, TableView<Product> productTableView) {
        saveButton.setOnAction(saveEvent -> {
            // Get the new product details from input fields
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            // Create a new product
            Product newProduct = new Product(name, description, price, stock);

            // Add the new product to the database
            database.addProducts(newProduct);

            // Refresh the TableView
            populateTable(productTableView);
            addStage.close(); // Close the adding window
        });
    }
    private void setEditButtonAction(Button editButton, TableView<Product> productTableView) {
        editButton.setOnAction(e -> {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                // Create a new editing window or dialog
                Stage editStage = new Stage();
                editStage.setTitle("Edit Product");

                // Create input fields to display and edit product details
                TextField nameFieldEdit = new TextField(selectedProduct.getName());
                TextField descriptionFieldEdit = new TextField(selectedProduct.getDescription());
                TextField priceFieldEdit = new TextField(String.valueOf(selectedProduct.getPrice()));
                TextField stockFieldEdit = new TextField(String.valueOf(selectedProduct.getStock()));

                // Create a "Save" button to save changes
                Button saveButton = new Button("Save");
                setSaveButtonActionForEdit(saveButton, nameFieldEdit, descriptionFieldEdit, priceFieldEdit, stockFieldEdit, editStage, productTableView, selectedProduct);

                // Create a layout for the editing window
                VBox editLayout = new VBox(10);
                editLayout.getChildren().addAll(nameFieldEdit, descriptionFieldEdit, priceFieldEdit, stockFieldEdit, saveButton);

                Scene editScene = new Scene(editLayout, 300, 200);
                editStage.setScene(editScene);
                editStage.show();
            }
        });
    }

    private void setSaveButtonActionForEdit(Button saveButton, TextField nameField, TextField descriptionField, TextField priceField, TextField stockField, Stage editStage, TableView<Product> productTableView, Product selectedProduct) {
        saveButton.setOnAction(saveEvent -> {
            // Get the updated details from input fields
            String updatedName = nameField.getText();
            String updatedDescription = descriptionField.getText();
            double updatedPrice = Double.parseDouble(priceField.getText());
            int updatedStock = Integer.parseInt(stockField.getText());

            // Create an updated product
            Product updatedProduct = new Product(updatedName, updatedDescription, updatedPrice, updatedStock);

            // Edit the product in the database
            database.editProduct(selectedProduct, updatedProduct);

            // Refresh the TableView
            productTableView.refresh();
            editStage.close(); // Close the editing window
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

