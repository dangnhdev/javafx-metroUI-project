package org.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.databaseAccess.DAAttendance;
import org.model.AttendanceReport;
import org.model.Course;
import org.model.Student;
import org.model.StudentAttendance;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by 404NotFound on 10/7/2015.
 */
public class AttendanceHistoryController implements Initializable {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
    @FXML
    private AnchorPane paneData;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<StudentAttendance> tableData;
    @FXML
    private TableColumn<StudentAttendance, String> colStudentId;
    @FXML
    private TableColumn<StudentAttendance, String> colStudentName;
    @FXML
    private TextField tfSearch;
    @FXML
    private Button btSearch;
    private List<AttendanceReport> attendanceReportList;
    private List<AttendanceReport> arList;
    private Config con = new Config();
    private ObservableList<StudentAttendance> listData = FXCollections.observableArrayList();
    private Callback<TableColumn.CellDataFeatures<StudentAttendance, Boolean>, ObservableValue<Boolean>> callback;

    /**
     * Initializes the controller class.
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        attendanceReportList = new DAAttendance().getAll();
        callback = param -> param.getValue().getListStatus().get((int) param.getTableColumn().getUserData()).get() ? new SimpleBooleanProperty(true) : new SimpleBooleanProperty(false);
    }

    @FXML
    private void actionSearch(ActionEvent actionEvent) {
        String courseId = tfSearch.getText().trim().toLowerCase();
        if (courseId.equals("")) {
            Notifications.create().title("Error").text("TextField cannot be empty").showError();
        } else {
            Optional<Course> courseOptional = controlSplash.courseList.stream().filter(c -> c.getCourseId().equals(courseId)).findAny();

            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
//                Subject subject = controlSplash.subjectList.stream().filter(s -> s.getId().equals(course.getSubjectId())).findAny().get();
                List<Student> studentList = controlSplash.studentList.stream().filter(s -> s.getClassName().equals(course.getClassId())).collect(Collectors.toList());
                ObservableList<TableColumn<StudentAttendance, Boolean>> statusCol = FXCollections.observableArrayList();

                for (Student s : studentList) {
                    arList = attendanceReportList.stream()
                            .filter(attReport -> attReport.getStudentId().equals(s.getStudentId()) && attReport.getCourseId().equals(courseId))
                            .collect(Collectors.toList());
                    List<BooleanProperty> statusList = arList.stream().map((attendanceReport) -> attendanceReport.statusProperty()).collect(Collectors.toList());
                    StudentAttendance stdAtt = new StudentAttendance();
                    stdAtt.setStudentId(s.getStudentId());
                    stdAtt.setStudentName(s.getName());
                    stdAtt.setListStatus(statusList);
                    listData.add(stdAtt);
                }

                for (int i = 0; i < listData.get(0).getListStatus().size(); i++) {
                    List<LocalDate> dateList = arList.stream().map(attendanceReport -> attendanceReport.getDateCreated()).collect(Collectors.toList());
                    TableColumn<StudentAttendance, Boolean> tmpCol = new TableColumn<>(dateList.get(i).format(formatter));
                    tmpCol.setUserData(i);
                    tmpCol.setCellFactory(CheckBoxTableCell.forTableColumn(tmpCol));
                    tmpCol.setCellValueFactory(callback);
                    statusCol.add(tmpCol);
                }
                tableData.getColumns().addAll(statusCol);

            } else {
                Notifications.create().title("Error").text("Course not found!").showError();
            }
        }
    }

    private void initTable() {
        con.setModelColumn(colStudentId, "studentId");
        con.setModelColumn(colStudentName, "studentName");
        tableData.setItems(listData);
    }
}
