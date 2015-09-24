package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Account;

import java.io.IOException;

/**
 * Created by truesik on 11.08.2015.
 */
public class AddAccountController {
    public TextField accountDescrField;
    public Button cancelButton;
    public Button addAccountButton;
    Controller controller;
    MainStageController mainStageController;
    Stage stage;
    Parent root = null;
    ColorAdjust colorAdjust;

    public AddAccountController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddAccount.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        accountDescrField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    addAccountButton.setDisable(true);
                } else {
                    addAccountButton.setDisable(false);
                }
            }
        });
    }

    public void launchAddAccount(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        stage = new Stage(StageStyle.TRANSPARENT);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.initOwner(mainStageController.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Добавление счета");
        stage.show();
        addAccountButton.setDisable(true);
        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.35);
        mainStageController.contentPane.setEffect(colorAdjust);
    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
        mainStageController.contentPane.setEffect(null);
    }

    public void addAccountHandle(ActionEvent actionEvent) {
        Account account = new Account();
        account.setDescription(accountDescrField.getText());
        controller.addAccount(controller.getCurrentUser(), account);
        stage.close();
        mainStageController.contentPane.setEffect(null);
        mainStageController.collectAccountTable();
    }
}
