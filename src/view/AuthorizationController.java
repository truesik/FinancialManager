package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.awt.*;
import java.io.IOException;

/**
 * Created by truesik on 10.08.2015.
 */
public class AuthorizationController {
    public TextField loginField;
    public PasswordField passwordField;
    public Button cancelButton;
    public Button enterButton;
    public Button openAddUserDialogButton;
    public Label exceptionLabel;
    public GridPane controlPane;
    Controller controller;
    Parent root = null;
    Stage authorization;

    public AuthorizationController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Authorization.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                validate();
            }
        };
        loginField.textProperty().addListener(changeListener);
        passwordField.textProperty().addListener(changeListener);
    }

    private void onCancel() {
        System.exit(0);
    }

    private void openMainFrame() {
        MainStageController mainStageController = new MainStageController(controller);
        mainStageController.launchMainStage();
    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        onCancel();
    }

    public void enterButtonHandle(ActionEvent actionEvent) {
        if (controller.isUserExist(loginField.getText())) {
            if (controller.isPasswordCorrect(controller.getUser(loginField.getText()), passwordField.getText())) {
                controller.setCurrentUser(controller.getUser(loginField.getText()));
                openMainFrame();
                authorization.close();
            } else {
                exceptionLabel.setText("Пароль не верен!");
            }
        } else {
            exceptionLabel.setText("Имя аккаунта не верно!");
        }
    }

    public void openAddUserFormHandle(ActionEvent actionEvent) {
        AddUserController addUserController = new AddUserController(controller);
        addUserController.launchAddUser(this);
    }

    public void launchAuthorization() {
        authorization = new Stage();
        if (root != null) {
            authorization.setScene(new Scene(root));
        }
        authorization.setTitle("Вход в Financial Manager");
        authorization.setResizable(false);
        authorization.show();
        enterButton.setDisable(true);
    }

    public void validate() {
        enterButton.disableProperty().set(loginField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty());
    }
}
