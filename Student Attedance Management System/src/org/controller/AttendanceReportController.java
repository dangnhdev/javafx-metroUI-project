package org.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.config.Config;
import org.databaseAccess.DAAttendance;
import org.model.AttendanceReport;
import org.model.Course;
import org.model.Student;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class AttendanceReportController implements Initializable {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYY HH:mm:ss");
    @FXML
    private AnchorPane paneData;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<AttendanceReport> tableData;
    @FXML
    private Button btSave;
    @FXML
    private TableColumn<AttendanceReport, String> colStudentId;
    @FXML
    private TableColumn<AttendanceReport, String> colStudentName;
    @FXML
    private TableColumn<AttendanceReport, Boolean> colStatus;
    @FXML
    private Text lbTeacher;
    @FXML
    private Text lbClass;
    @FXML
    private Text lbCourse;
    @FXML
    private Text lbTimeSlot;
    @FXML
    private Text lbDateTime;
    @FXML
    private Button btViewHistory;
    private ObservableList<AttendanceReport> listData = FXCollections.observableArrayList();
    private Config con = new Config();
    private DAAttendance daAttendance = new DAAttendance();
    private Course presentCourse = null;
    private String timeNow = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        createData();
        Platform.runLater(() -> lbDateTime.setText(LocalDateTime.now().toString()));
        setTime();
    }

    @FXML
    private void actionSave(ActionEvent event) {
        for (AttendanceReport attendanceReport : listData) {
            attendanceReport.setIsReported(true);
            attendanceReport.setDateCreated(LocalDate.now());
            daAttendance.add(attendanceReport);
        }
        colStatus.setEditable(false);
    }

    private List<Student> getStudentList(Course course) {
        List<Student> studentList = controlSplash.studentList.stream().filter(
                student -> student.getClassName().equals(course.getClassId()))
                .collect(Collectors.toList());
        return studentList;
    }

    private void initTable() {
        tableData.setItems(listData);
        tableData.setEditable(true);
        con.setModelColumn(colStudentId, "studentId");
        con.setModelColumn(colStudentName, "studentName");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(CheckBoxTableCell.forTableColumn(colStatus));
        colStatus.setEditable(true);
    }

    public void getPresentCourse(String teacherEmail) {
        List<Course> courseList = controlSplash.courseList.stream()
                .filter(c -> isPresentCourse(c, teacherEmail)).collect(Collectors.toList());
        if (!courseList.isEmpty()) {
            presentCourse = courseList.get(0);
        }
    }

    public boolean isPresentCourse(Course c, String teacherEmail) {
        if (LocalDateTime.now().getHour() >= 7 && LocalDateTime.now().getHour() < 11) {
            timeNow = "7AM - 11AM";
        } else if (LocalDateTime.now().getHour() >= 13 && LocalDateTime.now().getHour() < 17) {
            timeNow = "1PM - 5PM";
        }
        return c.getTeacherEmail().equals(teacherEmail)
                && (LocalDate.now().isAfter(c.getDefaultStartDate()) || ((LocalDate.now().isEqual(c.getDefaultStartDate()))))
                && (LocalDate.now().isBefore(c.getDefaultEndDate()) || ((LocalDate.now().isEqual(c.getDefaultEndDate()))))
                && (c.getPartOfDay().equals(timeNow));
    }

    private void createData() {
        getPresentCourse(LoginController.currentUser.getEmail());
        if (presentCourse != null) {
            List<AttendanceReport> attendanceReportList = daAttendance.getAll().stream()
                    .filter(attendanceReport -> attendanceReport.getDateCreated().equals(LocalDate.now())
                                    && attendanceReport.getCourseId().equals(presentCourse.getCourseId())
                    ).collect(Collectors.toList());

            if (attendanceReportList.isEmpty()) {
                for (Student student : getStudentList(presentCourse)) {
                    AttendanceReport ar = new AttendanceReport(student.getStudentId(), student.getName(), presentCourse.getCourseId());
                    listData.add(ar);
                    initLabel();
                }
            } else {
                listData.addAll(attendanceReportList);
                colStatus.setEditable(false);
                btSave.setDisable(true);
                initLabel();
            }
        }
    }

    private void setTime() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> {
                            Platform.runLater(() -> {
                                lbDateTime.setText(LocalDateTime.now().format(formatter));
                            });
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void initLabel() {
        lbTeacher.setText(presentCourse.getTeacherEmail());
        lbClass.setText(presentCourse.getClassId());
        lbCourse.setText(presentCourse.getCourseId());
        lbTimeSlot.setText(presentCourse.getPartOfDay());
    }
}
