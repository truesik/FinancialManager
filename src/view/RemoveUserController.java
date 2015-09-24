package view;

import controller.Controller;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.User;

import java.io.IOException;

/**
 * Created by truesik on 11.08.2015.
 */
public class RemoveUserController {
    public Button cancelButton;
    public Button removeUserButton;
    public PasswordField passwordField;
    public Label exceptionLabel;
    public GridPane contentPane;
    Controller controller;
    Parent root = null;
    Stage stage;
    ColorAdjust colorAdjust;
    MainStageController mainStageController;

    public RemoveUserController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemoveUser.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        passwordField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.isEmpty()) {
                    removeUserButton.setDisable(true);
                } else {
                    removeUserButton.setDisable(false);
                }
            }
        });
    }

    public void launchRemoveUser(MainStageController mainStageController) {
        this.mainStageController = mainStageController;

        stage = new Stage(StageStyle.TRANSPARENT);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Подтверждение удаления пользователя " + controller.getCurrentUser());
        stage.initOwner(mainStageController.stage);
        stage.initModality(Modality.WINDOW_MODAL);

        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.35);

        mainStageController.contentPane.setEffect(colorAdjust);
        stage.show();
        removeUserButton.setDisable(true);
    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
//        mainStageController.contentPane.setDisable(false);
        mainStageController.contentPane.setEffect(null);
    }

    public void removeUserButtonHandle(ActionEvent actionEvent) {
        if (isPasswordCorrect(controller.getCurrentUser(), passwordField.getText())) {
            controller.removeUser(controller.getCurrentUser().getName());
            mainStageController.stage.close();
            AuthorizationController authorizationController = new AuthorizationController(controller);
            authorizationController.launchAuthorization();
            stage.close();
        } else {
            exceptionLabel.setText("Неверный пароль");
        }
    }

    private boolean isPasswordCorrect(User user, String password) {
        return user.getPassword().equals(password);
    }
}
