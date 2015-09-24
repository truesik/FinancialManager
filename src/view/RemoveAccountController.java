package view;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by truesik on 19.08.2015.
 */
public class RemoveAccountController {
    public Label messageLabel;
    public Label detailsLabel;
    public HBox actionParent;
    public Button removeAccountButton;
    public HBox okParent;
    public Button cancelButton;
    Controller controller;
    MainStageController mainStageController;
    Stage stage;
    Parent root = null;
    ColorAdjust colorAdjust;

    public RemoveAccountController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RemoveAccount.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchRemoveAccount(MainStageController mainStageController) {
        this.mainStageController = mainStageController;
        stage = new Stage(StageStyle.TRANSPARENT);
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Удаление счета");
        stage.initOwner(mainStageController.stage);
        stage.initModality(Modality.WINDOW_MODAL);
        colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.35);
        mainStageController.contentPane.setEffect(colorAdjust);
        stage.show();
    }

    public void removeAccountButtonHandle(ActionEvent actionEvent) {
        controller.removeAccount(controller.getSelectedAccount());
        stage.close();
        mainStageController.contentPane.setEffect(null);
        mainStageController.collectAccountTable();
//        mainStageController.showBalanceLabel();
    }

    public void cancelButtonHandle(ActionEvent actionEvent) {
        stage.close();
        mainStageController.contentPane.setEffect(null);
    }
}
