package controller;

import model.Account;
import model.Category;
import model.Record;
import model.User;

import java.util.Set;

/**
 * Created by truesik on 03.08.2015.
 */
public class Controller {
    DataStore dataStore = new DataStorage();
    private User currentUser;
    private Account currentAccount;
    private Record currentRecord;
    private Account selectedAccount;

    public void setSelectedAccount(Account selectedAccount) {
        this.selectedAccount = selectedAccount;
    }

    public Record getCurrentRecord() {
        return currentRecord;
    }

    public void setCurrentRecord(Record currentRecord) {
        this.currentRecord = currentRecord;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isPasswordCorrect(User user, String password) {
        return user.getPassword().equals(password);
    }

    public User getUser(String username) {
        return dataStore.getUser(username);
    }

    public boolean isUserExist(String userName) {
        return dataStore.getUser(userName) != null;
    }

    public void addAccount(User owner, Account account) {
        dataStore.addAccount(owner, account);
    }

    public void removeUser(String name) {
        dataStore.removeUser(name);
    }

    public void addUser(User user) {
        dataStore.addUser(user);
    }

    public Set<Account> getAccounts(User owner) {
        return dataStore.getAccounts(owner);
    }

    public Set<Record> getRecords(Account account) {
        return dataStore.getRecords(account);
    }

    public double getBalance(int currentAccount) {
        return dataStore.getBalance(currentAccount);
    }

    public void addRecord(Account account, Record record) {
        dataStore.addRecord(account, record);
    }

    public void addCategory(Category category) {
        dataStore.addCategory(category);
    }

    public Set<Category> getCategories() {
        return dataStore.getCategories();
    }

    public void removeRecord(Record record) {
        dataStore.removeRecord(getCurrentAccount(), record);
    }

    public Account getSelectedAccount() {
        return selectedAccount;
    }

    public void removeAccount(Account account) {
        dataStore.removeAccount(getCurrentUser(), account);
    }
}
