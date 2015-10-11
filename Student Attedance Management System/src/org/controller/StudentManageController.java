package org.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.StageStyle;
import org.animations.FadeInUpTransition;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.databaseAccess.DAStudent;
import org.model.Student;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class StudentManageController implements Initializable {

    @FXML
    private AnchorPane paneData;
    @FXML
    private Button btNew;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<Student> tableData;
    @FXML
    private TableColumn<Student, String> colId;
    @FXML
    private TableColumn<Student, String> colName;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private TableColumn<Student, String> colClass;
    @FXML
    private AnchorPane paneCrud;
    @FXML
    private Button btBack;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfClassId;
    @FXML
    private Button btSave;
    @FXML
    private Button btDelete;
    private ObservableList<Student> listData;
    @FXML
    private ComboBox<String> cbSearch;
    private DAStudent daStudent = new DAStudent();
    private int currentUserLevel = LoginController.currentUser.getLevel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTable).play();
        initTable();
        if (currentUserLevel != 3) {
            setEditable();
        } else if (currentUserLevel == 3) {
            btNew.setOpacity(0);
            btDelete.setOpacity(0);
        }
        cbSearch.getItems().addAll("ID", "Name", "Email", "Class");
        listData.addAll(controlSplash.studentList);
    }

    @FXML
    private void addNew(ActionEvent event) {
        paneTable.setOpacity(0);
        new FadeInUpTransition(paneCrud).play();
        btDelete.setDisable(true);
    }

    @FXML
    private void actionBack(ActionEvent event) {
        paneCrud.setOpacity(0);
        new FadeInUpTransition(paneTable).play();
        btDelete.setDisable(false);
    }

    @FXML
    private void actionSave(ActionEvent event) {
        if (tfId.getText().equals("") || tfName.getText().equals("") || tfEmail.getText().equals("") || tfClassId.getText().equals("")) {
            Notifications.create().title("Add Error")
                    .text("All Text Field must be filled!")
                    .showError();
        } else if (!Config.pattern.matcher(tfEmail.getText()).matches()) {
            Notifications.create().title("Add Error")
                    .text("Email isn't valid")
                    .showError();
        } else if (isDuplicateID(tfId.getText())) {
            Notifications.create().title("Add Error").text("Duplicate ID").showError();
        } else if (isDuplicateEmail(tfEmail.getText())) {
            Notifications.create().title("Add Error").text("Duplicate Email").showError();
        } else {
            Student student = new Student();
            student.setStudentId(tfId.getText());
            student.setName(tfName.getText());
            student.setEmail(tfEmail.getText());
            student.setClassName(tfClassId.getText());
            listData.add(student);
            controlSplash.studentList.add(student);
            daStudent.add(student);
            Notifications.create().title("").text("Add Successfull").showInformation();
            clear();
        }
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
            Student s = tableData.getSelectionModel().getSelectedItem();
            int selectedIndex = getIndexById(s.getStudentId());
            daStudent.delete(s.getStudentId());
            listData.remove(selectedIndex);
            controlSplash.studentList.remove(selectedIndex);
        }
    }

    private void initTable() {
        listData = FXCollections.observableArrayList();
        Config config = new Config();
        config.setModelColumn(colId, "studentId");
        config.setModelColumn(colName, "name");
        config.setModelColumn(colEmail, "email");
        config.setModelColumn(colClass, "className");
        tableData.setItems(listData);

        //Create fillter for search field
        FilteredList<Student> filteredData = new FilteredList<>(listData, p -> true);
        tableData.setItems(filteredData);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty() || cbSearch.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "ID":
                        if (student.getStudentId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Name":
                        if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Email":
                        if (student.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Class":
                        if (student.getClassName().toLowerCase().contains(lowerCaseFilter)) {
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
        colId.setEditable(true);
        colId.setCellFactory(TextFieldTableCell.forTableColumn());
        colId.setOnEditCommit(event -> {
            Student std = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexById(std.getStudentId());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> {
                    Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
                });
            } else if (isDuplicateID(event.getNewValue())) {
                undoEdit();
                Notifications.create().title("Add Error").text("Duplicate ID").showError();
            } else {
                std.setStudentId(event.getNewValue());
                daStudent.edit(std);
                controlSplash.studentList.get(index).setStudentId(event.getNewValue());
            }
        });

        colName.setEditable(true);
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Student std = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> {
                    Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
                });
            } else {
                std.setName(event.getNewValue());
                daStudent.edit(std);
            }
        });

        colEmail.setEditable(true);
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Student std = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexByEmail(std.getEmail());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> {
                    Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
                });
            } else if (!Config.pattern.matcher(event.getNewValue()).matches()) {
                undoEdit();
                Notifications.create().title("Add Error")
                        .text("Email isn't valid")
                        .showError();
            } else if (isDuplicateEmail(event.getNewValue())) {
                undoEdit();
                Notifications.create().title("Add Error").text("Duplicate Email").showError();
            } else {
                std.setEmail(event.getNewValue());
                daStudent.edit(std);
                controlSplash.studentList.get(index).setEmail(event.getNewValue());
            }
        });

        colClass.setEditable(true);
        colClass.setCellFactory(TextFieldTableCell.forTableColumn());
        colClass.setOnEditCommit(event -> {
            Student std = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("Field cannot be empty").showError());
            } else if (!listData.stream().anyMatch(student -> student.getClass().equals(event.getNewValue()))) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("You cannot create new class here").showError());
            } else {
                std.setClassName(event.getNewValue());
                daStudent.edit(std);
            }
        });
    }

    private void clear() {
        tfId.clear();
        tfName.clear();
        tfEmail.clear();
        tfClassId.clear();
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

    private boolean isDuplicateID(String studentID) {
        return listData.stream().anyMatch(student -> student.getStudentId().equals(studentID));
    }

    private boolean isDuplicateEmail(String studentEmail) {
        return listData.stream().anyMatch(student -> student.getEmail().equals(studentEmail));
    }

    private int getIndexById(String studentId) {
        for (int i = 0; i < controlSplash.studentList.size(); i++) {
            if (controlSplash.studentList != null
                    && controlSplash.studentList.get(i).getStudentId().equals(studentId)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexByEmail(String studentEmail) {
        for (int i = 0; i < controlSplash.studentList.size(); i++) {
            if (controlSplash.studentList != null
                    && controlSplash.studentList.get(i).getEmail().equals(studentEmail)) {
                return i;
            }
        }
        return -1;
    }
}
