package org.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.animations.FadeInUpTransition;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.databaseAccess.DACourse;
import org.databaseAccess.DASubject;
import org.model.Course;
import org.model.Student;
import org.model.Subject;
import org.model.Teacher;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class CourseController implements Initializable {

    private final String datePattern = "dd/MM/yyyy";
    @FXML
    private AnchorPane paneData;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<Course> tableData;
    @FXML
    private TableColumn<Course, String> colCourseId;
    @FXML
    private TableColumn<Course, String> colClassId;
    @FXML
    private TableColumn<Course, String> colTeacherId;
    @FXML
    private TableColumn<Course, String> colSubjectId;
    @FXML
    private TableColumn<Course, String> colDate;
    @FXML
    private TableColumn<Course, LocalDate> colStartDate;
    @FXML
    private TableColumn<Course, LocalDate> colEndDate;
    @FXML
    private TableColumn<Course, String> colPod;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private Button btDelete;
    @FXML
    private Button btNew;
    @FXML
    private AnchorPane paneCrud;
    @FXML
    private Button btBack;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfClassId;
    @FXML
    private TextField tfTeacherEmail;
    @FXML
    private TextField tfSubjectId;
    @FXML
    private Button btSave;
    @FXML
    private Button btCheckTeacher;
    @FXML
    private DatePicker tfStartDate;
    @FXML
    private DatePicker tfEndDate;
    @FXML
    private RadioButton radMorning;
    @FXML
    private ToggleGroup podRadio;
    @FXML
    private RadioButton radAfternoon;
    @FXML
    private ProgressBar pgBar;
    private Service<Void> loadData;
    private ObservableList<Course> listData = FXCollections.observableArrayList();
    private String partOfDay = "7AM - 11AM";
    private DACourse daCourse = new DACourse();
    private int currentUserLevel = LoginController.currentUser.getLevel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        longStart();
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
            Course crs = tableData.getSelectionModel().getSelectedItem();
            int selectedIndex = getIndexById(crs.getCourseId());
            listData.remove(selectedIndex);
            daCourse.delete(crs.getCourseId());
            controlSplash.courseList.remove(selectedIndex);
        }
    }

    @FXML
    private void addNew(ActionEvent event) {
        paneTable.setOpacity(0);
        new FadeInUpTransition(paneCrud).play();
    }

    @FXML
    private void actionBack(ActionEvent event) {
        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTable).play();
    }

    @FXML
    private void actionSave(ActionEvent event) {
        if (tfId.getText().equals("") || tfClassId.getText().equals("") || tfTeacherEmail.getText().equals("")
                || tfSubjectId.getText().equals("") || tfStartDate.getValue().toString().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else {
            //Get all text from textfield
            String courseId = tfId.getText();
            String classId = tfClassId.getText();
            String teacherEmail = tfTeacherEmail.getText();
            String subjectId = tfSubjectId.getText();
            LocalDate startDate = tfStartDate.getValue();
            int period = new DASubject().getSubjectById(tfSubjectId.getText());
            LocalDate endDate = startDate.plusDays(period);
            tfEndDate.setValue(endDate);

            //Create course
            Course course = new Course(courseId, teacherEmail, classId, subjectId, startDate, endDate, partOfDay);

            //Check valid course
            if (isDuplicate(course)) {
                Notifications.create().title("Add Error").text("Duplicate Course ID").showError();
            } else if (!isCorrectEmail(teacherEmail)) {
                Notifications.create().title("Add Error").text("Teacher Email Not Found").showError();
            } else if (!isValidTime(course)) {
                Notifications.create().title("Add Error").
                        text("The time period is invalid. Please check Teacher's Time Slot and Time period in other course").showError();
            } else if (!isCorrectClass(classId)) {
                Notifications.create().title("Add Error").text("Class doesn't exist").showError();
            } else {
                listData.add(course);
                controlSplash.courseList.add(course);
                daCourse.add(course);
                clear();
            }
        }
    }

    @FXML
    private void checkTeacher(ActionEvent event) {
    }

    private void initTable() {
        Config config = new Config();
        config.setModelColumn(colCourseId, "courseId");
        config.setModelColumn(colClassId, "classId");
        config.setModelColumn(colTeacherId, "teacherEmail");
        config.setModelColumn(colSubjectId, "subjectId");
        config.setModelColumn(colStartDate, "startDate");
        config.setModelColumn(colEndDate, "endDate");
        config.setModelColumn(colPod, "partOfDay");
        tableData.setItems(listData);

        FilteredList<Course> filteredData = new FilteredList<>(listData, p -> true);
        tableData.setItems(filteredData);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(course -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty() || cbSearch.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "Course ID":
                        if (course.getCourseId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Class ID":
                        if (course.getClassId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Teacher ID":
                        if (course.getTeacherEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Subject ID":
                        if (course.getSubjectId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                }
                return false;
            });
        });
    }

    private void setEditable() {
        tableData.setEditable(true);
        colClassId.setEditable(true);
        colClassId.setCellFactory(TextFieldTableCell.forTableColumn());
        colClassId.setOnEditCommit(event -> {
            Course cou = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else if (!isCorrectClass(event.getNewValue())) {
                Notifications.create().title("Add Error").text("Class doesn't exist").showError();
            } else {
                cou.setClassId(event.getNewValue());
                daCourse.edit(cou);
            }
        });

        colCourseId.setEditable(true);
        colCourseId.setCellFactory(TextFieldTableCell.forTableColumn());
        colCourseId.setOnEditCommit((event -> {
            Course cou = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else {
                cou.setCourseId(event.getNewValue());
                if (isDuplicate(cou)) {
                    cou.setCourseId(event.getOldValue());
                    undoEdit();
                    Notifications.create().title("Edit Error").text("Duplicate ID").showError();
                } else daCourse.edit(cou);
            }
        }));

        colSubjectId.setEditable(true);
        colSubjectId.setCellFactory(TextFieldTableCell.forTableColumn());
        colSubjectId.setOnEditCommit(event -> {
            Course cou = event.getTableView().getItems().get(event.getTablePosition().getRow());
            LocalDate defaultEndDate = cou.getDefaultEndDate();
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else {
                Optional<Subject> optSubject = controlSplash.subjectList.stream()
                        .filter(subject -> subject.getId().equals(event.getNewValue()))
                        .findAny();
                if (optSubject.isPresent()) {
                    Subject s = optSubject.get();
                    cou.setSubjectId(event.getNewValue());
                    cou.setEndDate(cou.getDefaultStartDate().plusDays(s.getPeriod()));
                    if (isValidTime(cou)) {
                        undoEdit();
                        daCourse.edit(cou);
                    } else {
                        cou.setSubjectId(event.getOldValue());
                        cou.setEndDate(defaultEndDate);
                        undoEdit();
                        Notifications.create().title("Edit Error")
                                .text("The time period is invalid. Please check Teacher's Time Slot and Time period in other course").showError();
                    }
                } else {
                    Notifications.create().title("Edit Error").text("Subject doesn't Exist").showError();
                }
            }
        });

        colTeacherId.setEditable(true);
        colTeacherId.setCellFactory(TextFieldTableCell.forTableColumn());
        colTeacherId.setOnEditCommit(event -> {
            Course cou = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else if (!isCorrectEmail(event.getNewValue())) {
                Notifications.create().title("Edit Error").text("Teacher Email not found").showError();
            } else {
                cou.setTeacherEmail(event.getNewValue());
                if (isValidTime(cou)) {
                    daCourse.edit(cou);
                } else {
                    cou.setTeacherEmail(event.getOldValue());
                    undoEdit();
                    Notifications.create().title("Edit Error")
                            .text("The time period is invalid. Please check Teacher's Time Slot and Time period in other course").showError();
                }

            }
        });

    }

    private void formatDatePicker() {

        tfStartDate.setPromptText(datePattern.toLowerCase());
        tfStartDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        tfEndDate.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(datePattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    private void undoEdit() {
        new java.util.Timer().schedule(new java.util.TimerTask() {

            @Override
            public void run() {
                Platform.runLater(() -> {
                    tableData.getColumns().get(0).setVisible(false);
                    tableData.getColumns().get(0).setVisible(true);
                });
            }
        }, 50);

    }

    private boolean isDuplicate(Course crs) {
        return listData.stream().anyMatch(course -> course.getCourseId().equals(crs.getCourseId()));
    }

    private boolean isCorrectClass(String className) {
        Map<String, Student> map = new LinkedHashMap<>();
        for (Student student : controlSplash.studentList) {
            map.put(student.getClassName(), student);
        }
        List<Student> studentList = new ArrayList<>(map.values());
        return studentList.stream().anyMatch(student -> student.getClassName().equals(className));
    }

    private boolean isCorrectEmail(String teacherEmail) {
        List<Teacher> teacherList = controlSplash.teacherList;
        return teacherList.stream().anyMatch(teacher -> teacher.getEmail().equals(teacherEmail));
    }

    private void clear() {
        tfId.clear();
        tfClassId.clear();
        tfTeacherEmail.clear();
        tfStartDate.setValue(null);
        tfSubjectId.clear();
    }

    private void longStart() {
        loadData = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updateProgress(30, 100);
                        paneTable.setOpacity(0);
                        listData.addAll(controlSplash.courseList);
                        updateProgress(50, 100);
                        updateProgress(100, 100);
                        return null;
                    }
                };
            }
        };

        btNew.disableProperty().bind(loadData.runningProperty());
        pgBar.progressProperty().bind(loadData.progressProperty());

        loadData.start();
        loadData.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                pgBar.setOpacity(0);
                paneCrud.setOpacity(0);
                new FadeInUpTransition(paneTable).play();
                initTable();
                if (currentUserLevel != 3) {
                    setEditable();
                } else if (currentUserLevel == 3) {
                    btDelete.setOpacity(0);
                    btNew.setOpacity(0);
                    btDelete.setDisable(true);
                }
                cbSearch.getItems().addAll("Course ID", "Class Name", "Teacher Email", "Subject ID");
                formatDatePicker();
                initRadioButton();
            });
        });
    }

    private void initRadioButton() {
        radMorning.setUserData("7AM - 11AM");
        radAfternoon.setUserData("1PM - 5PM");
        podRadio.selectedToggleProperty().addListener(ov -> {
            if (podRadio.getSelectedToggle().equals(radMorning)) {
                partOfDay = "7AM - 11AM";
            } else {
                partOfDay = "1PM - 5PM";
            }
        });
    }

    private boolean checkTime(LocalDate oldStartDate, LocalDate oldEndDate, LocalDate newStartDate, LocalDate newEndDate) {
        if (newStartDate.isBefore(LocalDate.now()) || newStartDate.isEqual(LocalDate.now())) {
            return false;
        } else if (newStartDate.isEqual(oldStartDate) || newStartDate.isEqual(oldEndDate)) {
            return false;
        } else if (newEndDate.isEqual(oldStartDate) || newEndDate.isEqual(oldEndDate)) {
            return false;
        } else if (newStartDate.isAfter(oldStartDate) && newStartDate.isBefore(oldEndDate)) {
            return false;
        } else if (newEndDate.isAfter(oldStartDate) && newEndDate.isBefore(oldEndDate)) {
            return false;
        }
        return true;
    }

    private boolean isValidTime(Course course) {
        List<Course> courseList = listData.stream().filter(
                c -> c.getPartOfDay().equals(course.getPartOfDay()) && c.getTeacherEmail().equals(course.getTeacherEmail()))
                .collect(Collectors.toList());
        for (Course course1 : courseList) {
            if (!checkTime(course1.getDefaultStartDate(), course1.getDefaultEndDate(),
                    course.getDefaultStartDate(), course.getDefaultEndDate())) {
                return false;
            }
        }
        return true;
    }

    private int getIndexById(String courseId) {
        for (int i = 0; i < controlSplash.courseList.size(); i++) {
            if (controlSplash.courseList != null
                    && controlSplash.courseList.get(i).getCourseId().equals(courseId)) {
                return i;
            }
        }
        return -1;
    }
}
