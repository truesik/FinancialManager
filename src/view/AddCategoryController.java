package view;

import controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Category;

import java.io.IOException;

/**
 * Created by truesik on 11.08.2015.
 */
public class AddCategoryController {
    public TextField categoryNameField;
    public Button cancelButton;
    public Button addCategoryButton;
    Controller controller;
    AddRecordController addRecordController;
    Stage stage;
    Parent root = null;

    public AddCategoryController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddCategory.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        categoryNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    addCategoryButton.setDisable(true);
                } else {
                    addCategoryButton.setDisable(false);
                }
            }
        });
    }

    public void launchAddCategory(AddRecordController addRecordController) {
        this.addRecordController = addRecordController;
        stage = new Stage(StageStyle.TRANSPARENT);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Добавление категории");
        addRecordController.stage.hide();
        stage.initOwner(addRecordController.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();

    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
        addRecordController.stage.show();
    }

    public void addCategoryHandle(ActionEvent actionEvent) {
        Category category = new Category();
        category.setName(categoryNameField.getText());
        controller.addCategory(category);
        stage.close();
        addRecordController.collectCategoryComboBox();
        addRecordController.stage.show();
    }
}
