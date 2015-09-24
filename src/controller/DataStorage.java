package controller;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by truesik on 02.08.2015.
 */
public class DataStorage implements DataStore {
    DBHelper dbHelper = DBHelper.getInstance();
    private PreparedStatement pStmt = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    @Override
    public User getUser(String name) {
        User user = null;
        try {
            pStmt = dbHelper.getConn().prepareStatement("SELECT * FROM USERS WHERE NAME = ?");
            pStmt.setString(1, name);
            rs = pStmt.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setName(rs.getString("NAME"));
                user.setPassword(rs.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return user;
    }

    @Override
    public Set<String> getUserNames() {
        Set<String> userNames = new HashSet<>();
        try {
            stmt = dbHelper.getConn().createStatement();
            rs = stmt.executeQuery("SELECT NAME FROM USERS");
            while (rs.next()) {
                userNames.add(rs.getString("NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(stmt);
            DBHelper.closeConn();
        }
        return userNames;
    }

    @Override
    public Set<Account> getAccounts(User owner) {
        Set<Account> accounts = new HashSet<>();
        try {
            pStmt = dbHelper.getConn().prepareStatement("SELECT ID, DESCR FROM ACCOUNTS WHERE USER_NAME = ?");
            pStmt.setString(1, owner.getName());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Account account = new Account();
                account.setId(rs.getInt("ID"));
                account.setDescription(rs.getString("DESCR"));
//                account.setUserName(rs.getString("USER_NAME"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return accounts;
    }

    @Override
    public Set<Record> getRecords(Account account) {
        Set<Record> records = new HashSet<>();
        try {
            pStmt = dbHelper.getConn().prepareStatement("SELECT * FROM RECORDS LEFT JOIN CATEGORIES ON CATEGORY_ID = CATEGORIES.ID WHERE ACCOUNT_ID = ?");
            pStmt.setInt(1, account.getId());
            rs = pStmt.executeQuery();
            while (rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt(1));
                record.setDescription(rs.getString("DESCR"));
                record.setAmount(rs.getDouble("AMOUNT"));
                record.setDate(rs.getString("DATE_REC"));
                record.setIsPut(rs.getBoolean("IS_PUT"));
                record.setCategoryName(rs.getString("NAME"));
                record.setAccountId(rs.getInt("ACCOUNT_ID"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return records;
    }

    @Override
    public Set<Category> getCategories() {
        Set<Category> categories = new HashSet<>();
        try {
            stmt = dbHelper.getConn().createStatement();
            rs = stmt.executeQuery("SELECT * FROM CATEGORIES");
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("ID"));
                category.setName(rs.getString("NAME"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(stmt);
            DBHelper.closeConn();
        }
        return categories;
    }

    @Override
    public void addUser(User user) {
        try {
            pStmt = dbHelper.getConn().prepareStatement("INSERT INTO USERS (NAME, PASSWORD) VALUES (?, ?)");
            pStmt.setString(1, user.getName());
            pStmt.setString(2, user.getPassword());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
    }

    @Override
    public void addAccount(User user, Account account) {
        try {
            pStmt = dbHelper.getConn().prepareStatement("INSERT INTO ACCOUNTS (DESCR, USER_NAME) VALUES (?, ?)");
            pStmt.setString(1, account.getDescription());
            pStmt.setString(2, user.getName());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
    }

    @Override
    public void addRecord(Account account, Record record) {
        try {
            pStmt = dbHelper.getConn().prepareStatement("INSERT INTO RECORDS (DESCR, AMOUNT, DATE_REC, IS_PUT, CATEGORY_ID, ACCOUNT_ID) VALUES (?, ?, ?, ?, ?, ?)");
            pStmt.setString(1, record.getDescription());
            pStmt.setDouble(2, record.getAmount());
            pStmt.setString(3, record.getDate());
            pStmt.setBoolean(4, record.getIsPut());
            pStmt.setInt(5, record.getCategory().getId());
            pStmt.setInt(6, account.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
    }

    @Override
    public void addCategory(Category category) {
//        pStmt = null;
        try {
            pStmt = dbHelper.getConn().prepareStatement("INSERT INTO CATEGORIES (NAME) VALUES (?)");
            pStmt.setString(1, category.getName());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
    }

    //todo разобраться
    @Override
    public User removeUser(String name) {
        User user = null;
        try {
            pStmt = dbHelper.getConn().prepareStatement("DELETE FROM USERS WHERE NAME = ?");
            pStmt.setString(1, name);
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return user;
    }

    @Override
    public Account removeAccount(User owner, Account account) {
        //owner.removeAccount(account);
        try {
            pStmt = dbHelper.getConn().prepareStatement("DELETE FROM ACCOUNTS WHERE ID = ?");
            pStmt.setInt(1, account.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return account;
    }

    @Override
    public Record removeRecord(Account from, Record record) {

        try {
            pStmt = dbHelper.getConn().prepareStatement("DELETE FROM RECORDS WHERE ACCOUNT_ID = ? AND ID = ?");
            pStmt.setInt(1, from.getId());
            pStmt.setInt(2, record.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return record;
    }

    @Override
    public Category removeCategory(Category category) {

        try {
            pStmt = dbHelper.getConn().prepareStatement("DELETE FROM CATEGORIES WHERE ID = ?");
            pStmt.setInt(1, category.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return category;
    }

    @Override
    public void editCategory(Category category) {
        try {
            pStmt = dbHelper.getConn().prepareStatement("UPDATE CATEGORIES SET NAME = ? WHERE ID = ?");
            pStmt.setString(1, category.getName());
            pStmt.setInt(2, category.getId());
            pStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
    }

    public double getBalance(int id) {
//        pStmt = null;
//        rs = null;
//        Account account = new Account();
        double sum = 0;
        try {
            pStmt = dbHelper.getConn().prepareStatement("SELECT SUM(AMOUNT) AS SUM FROM RECORDS WHERE IS_PUT = 0 AND ACCOUNT_ID = ?");
            pStmt.setInt(1, id);
            rs = pStmt.executeQuery();
            double sumNegativeAmount = 0;
            sumNegativeAmount = sumNegativeAmount + rs.getDouble("SUM");
//            System.out.println(sumNegativeAmount);
            pStmt = dbHelper.getConn().prepareStatement("SELECT SUM(AMOUNT) AS SUM FROM RECORDS WHERE IS_PUT = 1 AND ACCOUNT_ID = ?");
            pStmt.setInt(1, id);
            rs = pStmt.executeQuery();
            double sumPositiveAmount = 0;
            sumPositiveAmount = sumPositiveAmount + rs.getDouble("SUM");
//            System.out.println(sumPositiveAmount);
            sum = sumPositiveAmount - sumNegativeAmount;
//            System.out.println(sum);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelper.closeRes(rs);
            DBHelper.closeRes(pStmt);
            DBHelper.closeConn();
        }
        return sum;
    }
}
