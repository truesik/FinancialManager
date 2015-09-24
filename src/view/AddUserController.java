package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;

import java.io.IOException;

/**
 * Created by truesik on 10.08.2015.
 */
public class AddUserController {
    public Button cancelButton;
    public Button addNewUserButton;
    public PasswordField confirmNewPasswordField;
    public PasswordField newPasswordField;
    public TextField newLoginField;
    public GridPane contentPane;
    public Label exceptionLabel;
    Controller controller;
    User user;
    Parent root = null;
    Stage addUserStage;

    public AddUserController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddUser.fxml"));
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
        newPasswordField.textProperty().addListener(changeListener);
        newLoginField.textProperty().addListener(changeListener);
        confirmNewPasswordField.textProperty().addListener(changeListener);
    }

    public void cancelButtonHandle(ActionEvent actionEvent) throws Exception {
        onCancel(actionEvent);
    }

    private void onCancel(ActionEvent actionEvent) {
//        Node source = (Node) actionEvent.getSource();
//        Stage stage = (Stage) source.getScene().getWindow();
        addUserStage.close();
    }

    public void addNewUserButtonHandle(ActionEvent actionEvent) {
        if (isPasswordsMatch()) {
            System.out.println(newLoginField.getText());
            if (controller.isUserExist(newLoginField.getText())) {
                exceptionLabel.setText("Такой аккаунт уже существует!");
            } else {
                user = new User();
                user.setName(newLoginField.getText());
                user.setPassword(newPasswordField.getText());
                controller.addUser(user);
                addUserStage.close();
            }
        } else {
            exceptionLabel.setText("Пароли не совпадают");
        }
    }

    private boolean isPasswordsMatch() {
        return newPasswordField.getText().equals(confirmNewPasswordField.getText());
    } // проверка совпадения паролей в полях

    public void launchAddUser(AuthorizationController authorizationController) {
        addUserStage = new Stage();
        if (root != null) {
            addUserStage.setScene(new Scene(root));
        }
        addUserStage.setTitle("Создание нового пользователя");
        addUserStage.setResizable(false);
        addUserStage.initOwner(authorizationController.authorization);
        addUserStage.initModality(Modality.WINDOW_MODAL);
        addUserStage.initStyle(StageStyle.UTILITY);
        addUserStage.show();
        addNewUserButton.setDisable(true);
    }

    public void validate() {
        addNewUserButton.disableProperty().set(newLoginField.getText().trim().isEmpty() || newPasswordField.getText().trim().isEmpty() || confirmNewPasswordField.getText().trim().isEmpty());
    }
}
