package org.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import org.animations.FadeInUpTransition;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.databaseAccess.DAUser;
import org.model.Teacher;
import org.model.User;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */


public class UserManageController implements Initializable {
    @FXML
    private AnchorPane paneData;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<User> tableData;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TextField tfUsername;
    @FXML
    private Button btAdd;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private RadioButton rd1;
    @FXML
    private RadioButton rd2;
    @FXML
    private RadioButton rd3;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, Integer> colLevel;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private Button btDelete;
    @FXML
    private Button btUpdate;
    @FXML
    private AnchorPane inputPane;
    @FXML
    private TextField tfEmail;
    @FXML
    private ToggleGroup groupLevel;
    private ObservableList<User> listData = FXCollections.observableArrayList();
    private int level = 3;
    private DAUser daUser = new DAUser();
    private int currentUserLevel = LoginController.currentUser.getLevel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        initRadioGroup();
        cbSearch.getItems().addAll("Username", "Email", "Level");
        new FadeInUpTransition(paneTable).play();
        new FadeInUpTransition(inputPane).play();
        listData.addAll(daUser.getAll());
        tableData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            User u = tableData.getSelectionModel().getSelectedItem();
            showUserDetails(u);
        });
    }

    @FXML
    private void actionDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Warning");
        alert.setContentText("Are you sure to Delete?");
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            int selectedIndex = tableData.getSelectionModel().getSelectedIndex();
            listData.remove(selectedIndex);
            daUser.delete(tableData.getSelectionModel().getSelectedItem().getUsername());
        }
    }

    @FXML
    private void actionAdd(ActionEvent event) {
        if (tfUsername.getText().equals("")
                || tfEmail.getText().equals("") || tfPassword.getText().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else if (currentUserLevel == 2 && (level == 1 || level == 2)) {
            Notifications.create().title("Add Error")
                    .text("You cannot add super admin or staff!")
                    .showError();
        } else {
            User user = new User();
            user.setUsername(tfUsername.getText());
            user.setPassword(tfPassword.getText());
            user.setEmail(tfEmail.getText());
            user.setLevel(level);

            if (isDuplicate(user) == 1) {
                Notifications.create().title("Add Error").text("Duplicate Username").showError();
            } else if (isDuplicate(user) == 2) {
                Notifications.create().title("Add Error").text("Duplicate Email").showError();
            } else if (user.getLevel() == 3 && !isCorrectEmail(tfEmail.getText())) {
                Notifications.create().title("Add Error").text("Teacher Email not found").showError();
            } else {
                listData.add(user);
                daUser.add(user);
                controlSplash.userList.add(user);
                Notifications.create().title("").text("Add Successful").showInformation();
                clear();
            }
        }
    }

    @FXML
    private void actionUpdate(ActionEvent event) {
        if (tfUsername.getText().equals("")
                || tfEmail.getText().equals("") || tfPassword.getText().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else if (currentUserLevel == 2 && (level == 1 || level == 2)) {
            Notifications.create().title("Add Error")
                    .text("You cannot add super admin or staff!")
                    .showError();
        } else {
            User user = tableData.getSelectionModel().getSelectedItem();
            User tmpUser = user;

            user.setUsername(tfUsername.getText());
            user.setPassword(tfPassword.getText());
            user.setEmail(tfEmail.getText());
            user.setLevel(level);
            if (isDuplicateUsername(user.getUsername())) {
                Notifications.create().title("Edit Error").text("Duplicate User ID").showError();
                user = tmpUser;
                undoEdit();
            } else if (user.getLevel() == 3 && !isCorrectEmail(tfEmail.getText())) {
                Notifications.create().title("Edit Error").text("Teacher Email not found").showError();
                user = tmpUser;
                undoEdit();
            } else if (isDuplicateEmail(user.getEmail())) {
                Notifications.create().title("Edit Error").text("Duplicate Email").showError();
                user = tmpUser;
                undoEdit();
            } else {
                daUser.edit(user);
                undoEdit();
            }
        }
    }

    private void initTable() {
        listData = FXCollections.observableArrayList();
        Config config = new Config();
        config.setModelColumn(colUsername, "username");
        config.setModelColumn(colEmail, "email");
        config.setModelColumn(colLevel, "level");
        tableData.setItems(listData);
        FilteredList<User> filteredData = new FilteredList<>(listData, p -> true);
        tableData.setItems(filteredData);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty() || cbSearch.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "Username":
                        if (user.getUsername().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Level":
                        if (user.getLevel().equals(Integer.parseInt(newValue))) {
                            return true;
                        }
                        break;
                    case "Email":
                        if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
    }

    private void initRadioGroup() {
        groupLevel.selectedToggleProperty().addListener(ov -> {
            if (rd1.isSelected()) {
                level = 1;
            } else if (rd2.isSelected()) {
                level = 2;
            } else {
                level = 3;
            }
        });
    }

    private void undoEdit() {
        Platform.runLater(() -> {
            tableData.getColumns().get(0).setVisible(false);
            tableData.getColumns().get(0).setVisible(true);
        });
    }

    private int isDuplicate(User u) {
        for (User user : listData) {
            if (user.getUsername().equals(u.getUsername())) {
                return 1;
            } else if (user.getEmail().equals(u.getUsername())) {
                return 2;
            }
        }
        return 0;
    }

    private boolean isDuplicateUsername(String userName) {
        return listData.stream().filter(user -> user.getUsername().equals(userName)).count() == 2 ? true : false;
    }

    private boolean isDuplicateEmail(String email) {
        return listData.stream().filter(user -> user.getEmail().equals(email)).count() == 2 ? true : false;
    }

    private boolean isCorrectEmail(String teacherEmail) {
        List<Teacher> teacherList = controlSplash.teacherList;
        for (Teacher teacher : teacherList) {
            if (teacher.getEmail().equals(teacherEmail)) {
                return true;
            }
        }
        return false;
    }

    private void clear() {
        tfUsername.clear();
        tfPassword.clear();
        tfEmail.clear();
    }

    private void showUserDetails(User u) {
        tfUsername.setText(u.getUsername());
        tfEmail.setText(u.getEmail());
        tfPassword.setText(u.getPassword());
        if (u.getLevel() == 1) {
            rd1.setSelected(true);
        } else if (u.getLevel() == 2) {
            rd2.setSelected(true);
        } else rd3.setSelected(true);
    }
}
