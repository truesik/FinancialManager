package view;

import controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Account;
import model.Category;
import model.Record;

import java.io.IOException;

/**
 * Created by truesik on 11.08.2015.
 */
public class AddRecordController {
    public RadioButton isPutRadioButton;
    public TextField dateField;
    public TextField amountField;
    public TextField descrField;
    public ComboBox categoryComboBox;
    public Button addCategoryButton;
    public Button addRecordButton;
    public Button cancelButton;
    Controller controller;
    MainStageController mainStageController;
    Stage stage;
    Parent root = null;
    ColorAdjust colorAdjust;
    ObservableList<Category> categoriesListModel = FXCollections.observableArrayList();

    public AddRecordController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddRecord.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchAddRecord(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        stage = new Stage(StageStyle.TRANSPARENT);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Добавление транзакции");
        stage.initOwner(mainStageController.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        addRecordButton.setDisable(true);
        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.35);
        mainStageController.contentPane.setEffect(colorAdjust);
        collectCategoryComboBox();
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                validate();
            }
        };
        dateField.textProperty().addListener(changeListener);
        amountField.textProperty().addListener(changeListener);
        descrField.textProperty().addListener(changeListener);

        amountField.lengthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if (newValue.intValue() > oldValue.intValue()) {
                    char ch = amountField.getText().charAt(oldValue.intValue());
                    if (!(ch >= '0' && ch <= '9' || ch == '.')) {
                        amountField.setText(amountField.getText().substring(0, amountField.getText().length() - 1));
                    }
                }
            }
        });
    }

    public void collectCategoryComboBox() {
        if (categoriesListModel != null) {
            categoriesListModel.clear();
        }
        for (Category category : controller.getCategories()) {
            categoriesListModel.add(category);
        }
        categoryComboBox.setItems(categoriesListModel);
        categoryComboBox.getSelectionModel().selectFirst();
    }

    public void addCategoryButtonHandle(ActionEvent actionEvent) {
        AddCategoryController addCategoryController = new AddCategoryController(controller);
        addCategoryController.launchAddCategory(this);
    }

    public void addRecordButtonHandle(ActionEvent actionEvent) {
        if (!isFieldsEmpty()) {
            Record record = new Record();
            record.setDescription(descrField.getText());
            record.setAmount(Double.parseDouble(amountField.getText()));
            record.setDate(dateField.getText());
            record.setIsPut(isPutRadioButton.isSelected());
            record.setCategory((Category) categoryComboBox.getSelectionModel().getSelectedItem());
            controller.addRecord(controller.getCurrentAccount(), record);
            stage.close();
            mainStageController.contentPane.setEffect(null);
            mainStageController.showRecords(controller.getCurrentAccount());
            mainStageController.showBalanceLabel();
        }
    }

    private boolean isFieldsEmpty() {
        return descrField.getText().isEmpty() || amountField.getText().isEmpty() || dateField.getText().isEmpty();
    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
        mainStageController.contentPane.setEffect(null);
    }

    public void validate() {
        addRecordButton.disableProperty().set(dateField.getText().trim().isEmpty() || amountField.getText().trim().isEmpty() || descrField.getText().trim().isEmpty());
    }
}
