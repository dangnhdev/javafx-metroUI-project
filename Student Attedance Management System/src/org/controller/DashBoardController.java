package org.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.animations.FadeInLeftTransition;
import org.animations.FadeInRightTransition;
import org.animations.FadeInUpTransition;
import org.model.Student;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by 404NotFound on 9/30/2015.
 */
public class DashBoardController implements Initializable {
    @FXML
    private AnchorPane paneData;
    @FXML
    private Button btStudent;
    @FXML
    private Button btCourse;
    @FXML
    private Button btClass;
    @FXML
    private Button btSubject;
    @FXML
    private Button btTeacher;
    @FXML
    private Text lblWelcome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            new FadeInLeftTransition(btCourse).play();
            new FadeInRightTransition(btSubject).play();
            new FadeInRightTransition(btStudent).play();
            new FadeInUpTransition(btClass).play();
            new FadeInUpTransition(btTeacher).play();
        });

        btCourse.setText("Course: " + controlSplash.courseList.size());
        btClass.setText("Class: " + countTotalClass());
        btStudent.setText("Student: " + controlSplash.studentList.size());
        btSubject.setText("Subject: " + controlSplash.subjectList.size());
        btTeacher.setText("Teacher: " + controlSplash.teacherList.size());
        if (LoginController.currentUser.getLevel() == 3) {
            Platform.runLater(() -> lblWelcome.setText("Welcome Teacher " + getTeacherName()));
        } else Platform.runLater(() -> lblWelcome.setText(""));
    }

    private int countTotalClass() {
        Map<String, Student> map = new LinkedHashMap<>();
        for (Student student : controlSplash.studentList) {
            map.put(student.getClassName(), student);
        }
        return map.values().size();
    }

    private String getTeacherName() {
        return controlSplash.teacherList.stream()
                .filter(teacher -> teacher.getEmail().equals(LoginController.currentUser.getEmail()))
                .findFirst().get().getName();
    }
}