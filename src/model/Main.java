package model;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import view.AuthorizationController;


/**
 * Created by truesik on 01.08.2015.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        showAuthorization(primaryStage);
    }

    private void showAuthorization(Stage primaryStage) {
        AuthorizationController authorizationController = new AuthorizationController(new Controller());
        authorizationController.launchAuthorization();
    }
}
