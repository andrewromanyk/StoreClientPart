package ua.edu.ukma.storeapp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class goods implements Serializable {
    @Id
    private int id_good;

    private String name;
    private String description;
    private String manufacturer;
    private int amount;
    private double price;
    private int id_group;

    public goods(int id_good, String name, String description, String manufacturer, int amount, double price, int id_group) {
        this.id_good = id_good;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.amount = amount;
        this.price = price;
        this.id_group = id_group;
    }

    public goods() {
        this.id_good = 0;
        this.name = "none";
        this.description = "none";
        this.manufacturer = "none";
        this.amount = 0;
        this.price = 0;
        this.id_group = 0;
    }

    @Override
    public String toString() {
        return "{" + id_good + ", " + name + ", " + description + ", " + manufacturer + ", " + amount + ", " + price + ", " + id_group + "}";
    }

    public int getId_good() {
        return id_good;
    }

    public void setId_good(int id_good) {
        this.id_good = id_good;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId_group() {
        return id_group;
    }

    public void setId_group(int id_group) {
        this.id_group = id_group;
    }


}
