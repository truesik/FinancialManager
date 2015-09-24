package model;

import javax.swing.*;

/**
 * Created by truesik on 02.08.2015.
 */
public class Account {
    private String description;
    private double balance;
    private int id;
//    private String userName;


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
