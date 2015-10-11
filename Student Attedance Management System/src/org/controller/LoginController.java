package org.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.animations.FadeInLeftTransition;
import org.animations.FadeInLeftTransition1;
import org.animations.FadeInRightTransition;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.model.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class LoginController implements Initializable {

    public static User currentUser;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblUserLogin;
    @FXML
    private Text lblUsername;
    @FXML
    private Text lblPassword;
    @FXML
    private Button btLogin;
    @FXML
    private Text lblAbout;
    @FXML
    private Label lblClose;
    @FXML
    private Hyperlink linkForgot;
    private Stage stage;
    //    private List<Student> studentList = new ArrayList<>();
//    private List<Course> courseList = new ArrayList<>();
//    private List<Teacher> teacherList = new ArrayList<>();
//    private List<AttendanceReport> attendanceReportList = new ArrayList<>();
//    private List<Subject> subjectList = new ArrayList<>();
    private List<User> userList = controlSplash.userList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            new FadeInRightTransition(lblUserLogin).play();
            new FadeInLeftTransition(lblWelcome).play();
            new FadeInLeftTransition1(lblPassword).play();
            new FadeInLeftTransition1(lblUsername).play();
            new FadeInLeftTransition1(tfUsername).play();
            new FadeInLeftTransition1(tfPassword).play();
            new FadeInRightTransition(btLogin).play();
            new FadeInLeftTransition(linkForgot).play();
        });
        lblClose.setOnMouseClicked((MouseEvent event) -> {
            Platform.exit();
            System.exit(0);
        });

    }

    @FXML
    private void actionLogin(ActionEvent event) {
        {
            String userName = tfUsername.getText();
            String passWord = tfPassword.getText();
            if (checkUser(userName, passWord)) {
                currentUser = getCurrentUser(userName);
                Config c = new Config();
                c.newStage(stage, lblClose, "/org/view/formMenu.fxml", "Student Attedance Management System",
                        true, StageStyle.DECORATED, false);

            } else {
                Notifications.create().title("Login error!").text("Wrong ID or Password. Please check again!").showError();
            }
        }
    }

    @FXML
    private void forgotPassword() {
        try {
            Stage st = new Stage();
            stage = (Stage) lblClose.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/view/ForgotPassword.fxml"));
            Scene scene = new Scene(root);
            st.setResizable(false);
            st.setMaximized(false);
            st.setTitle("Forgot Password");
            st.initStyle(StageStyle.UNDECORATED);
            st.setScene(scene);
            st.showAndWait();
        } catch (Exception e) {
        }
    }

    private boolean checkUser(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private User getCurrentUser(String userName) {
        for (User user : userList) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }
}
