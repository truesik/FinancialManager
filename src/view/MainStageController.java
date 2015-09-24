package view;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Account;
import model.Record;

import java.io.IOException;

/**
 * Created by truesik on 11.08.2015.
 */
public class MainStageController {
    public MenuItem miExit;
    public Button addAccountButton;
    public Label balanceLabel;
    public Button addRecordButton;
    public TableView<Record> recordsTable;
    public TableColumn<Record, String> dateColumn;
    public TableColumn<Record, Double> amountColumn;
    public TableColumn<Record, String> descrColumn;
    public TableColumn<Record, Boolean> isPutColumn;
    public TableColumn<Record, String> categoryNameColumn;
    public MenuItem switchUserMenuItem;
    public MenuItem removeUserMenuItem;
    public VBox contentPane;
    public MenuButton currentUserMenuButton;
    public TableColumn<Record, String> removeRecordButtonColumn;
    public TableView<Account> accountsTable;
    public TableColumn<Account, String> accountsColumn;
    public TableColumn<Account, Button> removeAccountsButtonColumn;
    Button removeAccountButton;
    Button removeRecordButton;
    Stage stage;
    Controller controller;
    Parent root = null;
    ObservableList<Account> accountsTableModel = FXCollections.observableArrayList();
    ObservableList<Record> recordsListModel = FXCollections.observableArrayList();

    public MainStageController(Controller controller) {
        this.controller = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainStage.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentUserMenuButton.setText(controller.getCurrentUser().getName());
        collectAccountTable();
        accountsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                controller.setCurrentAccount(accountsTable.getSelectionModel().getSelectedItem());
                showRecords(controller.getCurrentAccount());
                showBalanceLabel();
            }
        });

        showRecords(controller.getCurrentAccount());
        showBalanceLabel();
    }

    public void showBalanceLabel() {
        if (controller.getCurrentAccount() != null) {
            balanceLabel.setText(controller.getCurrentAccount().getDescription() + ": " + String.valueOf(controller.getBalance(controller.getCurrentAccount().getId())) + " руб.");
        }
    }

    public void showRecords(Account account) {
        if (recordsListModel != null) {
            recordsListModel.clear();
        }
        if (controller.getCurrentAccount() != null) {
            for (Record record : controller.getRecords(account)) {
                recordsListModel.add(record);
            }
            recordsTable.setItems(recordsListModel);

            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            descrColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            isPutColumn.setCellValueFactory(new PropertyValueFactory<>("isPut"));
            categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

            removeRecordButtonColumn.setCellFactory(new Callback<TableColumn<Record, String>, TableCell<Record, String>>() {
                @Override
                public TableCell<Record, String> call(TableColumn<Record, String> param) {
                    return new TableCell<Record, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            TableRow currentRow = getTableRow();
                            if (empty) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                currentRow.hoverProperty().addListener(new ChangeListener<Boolean>() {
                                    @Override
                                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                        if (currentRow.isHover()) {
                                            removeRecordButton = new Button();
                                            removeRecordButton.getStylesheets().add(this.getClass().getResource("css/removerecbutton.css").toExternalForm());
                                            removeRecordButton.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent event) {
                                                    param.getTableView().getSelectionModel().select(getIndex());
                                                    Record item = recordsTable.getSelectionModel().getSelectedItem();
                                                    if (item != null) {
                                                        controller.setCurrentRecord(item);
                                                        showRemoveRecordStage();
                                                    }
                                                }
                                            }); //
                                            setGraphic(removeRecordButton);
                                            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                        } else {
                                            setGraphic(null);
                                        }
                                    }
                                });
                            }
                        }
                    };
                }
            });
            removeRecordButtonColumn.setStyle("-fx-alignment: CENTER;");
            isPutColumn.setCellFactory(new Callback<TableColumn<Record, Boolean>, TableCell<Record, Boolean>>() {
                @Override
                public TableCell<Record, Boolean> call(TableColumn<Record, Boolean> column) {
                    return new TableCell<Record, Boolean>() {
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            TableRow currentRow = getTableRow();
                            if (item != null) {
                                if (item) {
                                    setText("Пополнение");
                                    if (currentRow.isHover()) {
                                        currentRow.setStyle("-fx-background-color: cyan");
                                    }
                                } else {
                                    setText("Снятие");
//                                    currentRow.setStyle("-fx-background-color: pink");
                                }
                            }
                        }
                    };
                }
            });


//            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
//            amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
//            descrColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
//            isPutColumn.setCellValueFactory(cellData -> cellData.getValue().isPutProperty());
//            categoryNameColumn.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());
        }
    }

    private EventHandler<ActionEvent> handler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Record item = recordsTable.getSelectionModel().getSelectedItem();
                if (item != null) {
                    controller.setCurrentRecord(item);
                    showRemoveRecordStage();
                }
            }
        };
    }

    private void showRemoveRecordStage() {
        RemoveRecordController removeRecordController = new RemoveRecordController(controller);
        removeRecordController.launchRemoveRecord(this);
    }

    public void collectAccountTable() {
        if (accountsTableModel != null) {
            accountsTableModel.clear();
        }
        for (Account account : controller.getAccounts(controller.getCurrentUser())) {
            accountsTableModel.add(account);
        }
        accountsTable.setItems(accountsTableModel);
        accountsTable.getSelectionModel().selectFirst();
        if (accountsTable.getSelectionModel().getSelectedItem() != null) {
            controller.setCurrentAccount(accountsTable.getSelectionModel().getSelectedItem());
        }
        accountsColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("description"));
        removeAccountsButtonColumn.setCellFactory(new Callback<TableColumn<Account, Button>, TableCell<Account, Button>>() {
            @Override
            public TableCell<Account, Button> call(TableColumn<Account, Button> param) {
                return new TableCell<Account, Button>() {
                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        TableRow currentRow = getTableRow();
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            currentRow.hoverProperty().addListener(new ChangeListener<Boolean>() {
                                @Override
                                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                    if (currentRow.isHover()) {
                                        removeAccountButton = new Button();
                                        removeAccountButton.getStylesheets().add(this.getClass().getResource("css/removeaccbutton.css").toExternalForm());
                                        removeAccountButton.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent event) {
                                                param.getTableView().getSelectionModel().select(getIndex());
                                                Account item = accountsTable.getSelectionModel().getSelectedItem();
                                                if (item != null) {
                                                    controller.setSelectedAccount(item);
                                                    showRemoveAccountStage();
                                                }
                                            }
                                        }); //
                                        setGraphic(removeAccountButton);
                                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                                    } else {
                                        setGraphic(null);
                                    }
                                }
                            });
                        }
                    }
                };
            }
        });
        removeAccountsButtonColumn.setStyle("-fx-alignment: CENTER;");
    }

    private void showRemoveAccountStage() {
        RemoveAccountController removeAccountController = new RemoveAccountController(controller);
        removeAccountController.launchRemoveAccount(this);
    }

    public void launchMainStage() {
        stage = new Stage();
        if (root != null) {
            stage.setScene(new Scene(root));
        }
        stage.setTitle("Financial Manager");
        stage.show();
    }

    public void miExitHandle(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void addAccountButtonHandle(ActionEvent actionEvent) {
        AddAccountController addAccountController = new AddAccountController(controller);
        addAccountController.launchAddAccount(this);

    }

    public void addRecordButtonHandle(ActionEvent actionEvent) {
        AddRecordController addRecordController = new AddRecordController(controller);
        addRecordController.launchAddRecord(this);
    }

    public void switchUserMenuItemHandle(ActionEvent actionEvent) {
        AuthorizationController authorizationController = new AuthorizationController(controller);
        authorizationController.launchAuthorization();
        stage.close();
        controller.setCurrentAccount(null);
    }

    public void removeUserMenuItemHandle(ActionEvent actionEvent) {
        RemoveUserController removeUserController = new RemoveUserController(controller);
        removeUserController.launchRemoveUser(this);
    }
}
