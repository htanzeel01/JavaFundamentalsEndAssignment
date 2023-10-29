package com.example.javaend;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database implements Serializable {
    private List<User> users;
    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();

    public Database() {
        users = new ArrayList<>();
        users.add(new User("admin", "admin123", UserRole.Admin));
        users.add(new User("user", "user123", UserRole.User));
        products.add(new Product( "Piano", "Casio Piano", 199.99, 50));
        products.add(new Product( "Guitar", "King Guitar", 299.99, 50));
        Customer customer = new Customer("Tanzeel","Rehman","t@gmail.com","067489282");
        List<Integer> quantityList = Arrays.asList(5, 10);
        orders.add(new Order(customer,products,quantityList));
    }
    public List<User> getUsers(){
        return users;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void addProducts(Product product){
        products.add(product);
    }
    public void removeProduct(Product product){
        products.remove(product);
    }
    public void editProduct(Product oldProduct, Product newProduct){
        newProduct.setId(oldProduct.getId());
        for (Product product : products) {
            if (product.getId() == newProduct.getId()) {
                // Update the product's attributes with the new values
                product.setName(newProduct.getName());
                product.setPrice(newProduct.getPrice());
                product.setStock(newProduct.getStock());
                product.setDescription(newProduct.getDescription());
                break; // Product found and updated, exit the loop
            }
        }
    }
    public void addOrders(Order order){
        List<Integer> quantities = order.getQauntity(); // Get the quantities from the order
        List<Product> products = order.getProducts(); // Get the products from the order

// Iterate over both quantities and products
        for (int i = 0; i < quantities.size(); i++) {
            int quantity = quantities.get(i); // Get the quantity for the current product
            Product product = products.get(i); // Get the corresponding product

            // Update the stock of the product
            int currentStock = product.getStock();
            product.setStock(currentStock - quantity);
        }
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }
    public void viewOrders() {
        System.out.println("List of Orders:");

        for (Order order : orders) {
            System.out.println(order);
        }
    }


    public User getUserbyUsername(String Username){
        for (User user : users) {
            if (user.getUsername().equals(Username)) {
                return user;
            }
        }
        return null;
    }
    public void loadDatabaseState() {
        File serializedFile = new File("database.ser");
        Database database;
        if (serializedFile.exists()) {
            // Deserialize the database
            try (FileInputStream fileIn = new FileInputStream("database.ser");
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                database = (Database) in.readObject(); // Deserialize the database object
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions that may occur during deserialization
                System.err.println("Error loading the database state. Initializing with an empty database.");
                database = new Database(); // Create a new database instance
            }
        } else {
            System.err.println("Serialized file not found. Initializing with an empty database.");
            database = new Database(); // Create a new database instance
        }
    }
    public void saveDatabaseState() {
        Database database = new Database();
        try (FileOutputStream fileOut = new FileOutputStream("database.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(database);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving the database state.");
        }
    }
}
