package com.example.project1722.Domain;

import java.util.ArrayList;

public class Invoice {
    private String id;
    private String customerEmail;
    private ArrayList<ItemsDomain> items;
    private double subtotal;
    private double tax;
    private double delivery;
    private double total;

    // Constructor
    public Invoice(String id, String customerEmail, ArrayList<ItemsDomain> items, double subtotal, double tax, double delivery, double total) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.items = items;
        this.subtotal = subtotal;
        this.tax = tax;
        this.delivery = delivery;
        this.total = total;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public ArrayList<ItemsDomain> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getDelivery() {
        return delivery;
    }

    public void setDelivery(double delivery) {
        this.delivery = delivery;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
