package com.example.javaend;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Customer customer;
    private List<Product> products;
    private List<Integer> qauntity;
    private LocalDateTime ordertime;

    public Order(Customer customer, List<Product> products, List<Integer> qauntity) {
        this.customer = customer;
        this.products = products;
        this.qauntity = qauntity;
        this.ordertime = LocalDateTime.now();
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getQauntity() {
        return qauntity;
    }

    public void setQauntity(List<Integer> qauntity) {
        this.qauntity = qauntity;
    }

    public LocalDateTime getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(LocalDateTime ordertime) {
        this.ordertime = ordertime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "customer=" + customer +
                ", products=" + products +
                ", qauntity=" + qauntity +
                '}';
    }
}
