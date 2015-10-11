package org.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.animations.FadeInLeftTransition;
import org.animations.FadeInRightTransition;
import org.animations.FadeInTransition;
import org.databaseAccess.*;
import org.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */


public class controlSplash implements Initializable {
    public static List<Student> studentList = new ArrayList<>();
    public static List<Course> courseList = new ArrayList<>();
    public static List<Teacher> teacherList = new ArrayList<>();
    public static List<AttendanceReport> attendanceReportList = new ArrayList<>();
    public static List<Subject> subjectList = new ArrayList<>();
    public static List<User> userList = new ArrayList<>();
    Stage stage;
    @FXML
    private ImageView imgLoading;
    @FXML
    private Text lblWelcome;
    @FXML
    private Text lblRudy;
    @FXML
    private VBox vboxBottom;
    @FXML
    private Label lblClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        longStart();
    }

    private void longStart() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        studentList = new DAStudent().getAll();
                        courseList = new DACourse().getAll();
                        teacherList = new DATeacher().getAll();
                        subjectList = new DASubject().getAll();
                        userList = new DAUser().getAll();
                        Thread.sleep(3000);
                        return null;
                    }
                };
            }
        };
        service.start();
        service.setOnRunning((WorkerStateEvent event) -> {
            new FadeInLeftTransition(lblWelcome).play();
            new FadeInRightTransition(lblRudy).play();
            new FadeInTransition(vboxBottom).play();
        });
        service.setOnSucceeded((WorkerStateEvent event) -> {
            try {
                Stage st = new Stage(StageStyle.UNDECORATED);
                st.setResizable(false);
                st.setMaximized(false);
                st.setTitle("Login");
                stage = (Stage) lblClose.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/view/Login.fxml"));
                Scene scene = new Scene(loader.load());
                st.setScene(scene);
                LoginController controller = loader.<LoginController>getController();
                st.show();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
