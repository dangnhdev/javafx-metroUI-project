package org.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.config.Config;

import java.net.URL;
import java.util.ResourceBundle;

public class menu implements Initializable {
    Stage stage;
    Config con = new Config();
    @FXML
    private ListView<String> listMenu;
    @FXML
    private Button btLogout;
    @FXML
    private AnchorPane paneData;
    private int userLevel = LoginController.currentUser.getLevel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (userLevel == 1) {
            listMenu.getItems().addAll("  User");
            Platform.runLater(() -> {
                stage = (Stage) btLogout.getScene().getWindow();
                con.loadAnchorPane(paneData, "UserManage.fxml");
                listMenu.requestFocus();
            });
        } else if (userLevel == 2) {
            listMenu.getItems().addAll("  Dashboard", "  Attendance History", "  Student",
                    "  Teacher", "  Course", "  Subject", "  User");
            Platform.runLater(() -> {
                stage = (Stage) btLogout.getScene().getWindow();
                con.loadAnchorPane(paneData, "DashBoard.fxml");
                listMenu.requestFocus();
            });
        } else {
            listMenu.getItems().addAll("  Dashboard", "  Attendance Report", "  Attendance History", "  Student",
                    "  Course", "  Subject");
            Platform.runLater(() -> {
                stage = (Stage) btLogout.getScene().getWindow();
                con.loadAnchorPane(paneData, "DashBoard.fxml");
                listMenu.requestFocus();
            });
        }
    }

    @FXML
    private void actionListMenu(MouseEvent event) {
        if (userLevel == 1) {
            switch (listMenu.getSelectionModel().getSelectedIndex()) {
                case 0: {
                    con.loadAnchorPane(paneData, "UserManage.fxml");
                }
                break;
            }
        } else if (userLevel == 2) {
            switch (listMenu.getSelectionModel().getSelectedIndex()) {
                case 0: {
                    con.loadAnchorPane(paneData, "DashBoard.fxml");
                }
                break;
                case 1: {
                    con.loadAnchorPane(paneData, "AttendanceHistory.fxml");
                }
                break;
                case 2: {
                    con.loadAnchorPane(paneData, "StudentManage.fxml");
                }
                break;
                case 3: {
                    con.loadAnchorPane(paneData, "TeacherManage.fxml");
                }
                break;
                case 4: {
                    con.loadAnchorPane(paneData, "Course.fxml");
                }
                break;
                case 5: {
                    con.loadAnchorPane(paneData, "SubjectManage.fxml");
                }
                break;
                case 6: {
                    con.loadAnchorPane(paneData, "UserManage.fxml");
                }
                break;
            }
        } else {
            switch (listMenu.getSelectionModel().getSelectedIndex()) {
                case 0: {
                    con.loadAnchorPane(paneData, "DashBoard.fxml");
                }
                break;
                case 1: {
                    con.loadAnchorPane(paneData, "AttendanceReport.fxml");
                }
                break;
                case 2: {
                    con.loadAnchorPane(paneData, "AttendanceHistory.fxml");

                }
                break;
                case 3: {
                    con.loadAnchorPane(paneData, "StudentManage.fxml");
                }
                break;
                case 4: {
                    con.loadAnchorPane(paneData, "Course.fxml");
                }
                break;
                case 5: {
                    con.loadAnchorPane(paneData, "SubjectManage.fxml");
                }
                break;
            }
        }
    }

    @FXML
    private void actionLogout(ActionEvent event) {
        Config config = new Config();
        config.newStage2(stage, btLogout, "/org/view/login.fxml", "Login", false,
                StageStyle.UNDECORATED, false);
    }

}
