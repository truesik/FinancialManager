package model;

import java.util.Date;

/**
 * Created by truesik on 02.08.2015.
 */
public class Record {
    private String description;
    private double amount;
    private boolean isPut;
    private int id;
    private String date;
    private int accountId;
    private Category category;
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsPut() {
        return isPut;
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

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIsPut(boolean isPut) {
        this.isPut = isPut;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Record{" +
                "description='" + description + '\'' +
                ", amount=" + amount +
                ", isPut=" + isPut +
                ", id=" + id +
                ", date='" + date + '\'' +
                ", category=" + categoryName +
                '}';
    }
}
