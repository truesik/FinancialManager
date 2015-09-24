package controller;

import model.Category;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by truesik on 09.08.2015.
 */
public class DataStorageTest {
    Controller controller = new Controller();
    DataStore dataStore = new DataStorage();
    Category category = new Category();

    @Test
    public void testGetBalance() throws Exception {
        System.out.println(dataStore.getBalance(1));
    }

    @Test
    public void testAddCategory() throws Exception {
        category.setName("test2");
        controller.addCategory(category);
        System.out.println(controller.getCategories());
    }
}