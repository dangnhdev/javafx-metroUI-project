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
import org.databaseAccess.DATeacher;
import org.model.Teacher;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class TeacherManageController implements Initializable {

    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<Teacher> tableData;
    @FXML
    private TableColumn<Teacher, String> colId;
    @FXML
    private TableColumn<Teacher, String> colName;
    @FXML
    private TableColumn<Teacher, String> colEmail;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private Button btDelete;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btNew;
    @FXML
    private AnchorPane inputPane;
    private ObservableList<Teacher> listData = FXCollections.observableArrayList();
    private DATeacher daTeacher = new DATeacher();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        setEditable();
        cbSearch.getItems().addAll("ID", "Name", "Email");
        new FadeInUpTransition(paneTable).play();
        new FadeInUpTransition(inputPane).play();
        listData.addAll(controlSplash.teacherList);
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
            Teacher tmp = tableData.getSelectionModel().getSelectedItem();
            int selectedIndex = getIndexById(tmp.getId());
            listData.remove(selectedIndex);
            controlSplash.teacherList.remove(selectedIndex);
        }
    }

    @FXML
    private void addNew(ActionEvent event) {
        if (tfId.getText().equals("") || tfName.getText().equals("") || tfEmail.getText().equals("")) {
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
            Teacher teacher = new Teacher();
            teacher.setId(tfId.getText());
            teacher.setName(tfName.getText());
            teacher.setEmail(tfEmail.getText());
            listData.add(teacher);
            controlSplash.teacherList.add(teacher);
            daTeacher.add(teacher);
            clear();

        }
    }

    private void initTable() {
        Config config = new Config();
        config.setModelColumn(colId, "id");
        config.setModelColumn(colName, "name");
        config.setModelColumn(colEmail, "email");
        tableData.setItems(listData);
        FilteredList<Teacher> filteredData = new FilteredList<>(listData, p -> true);
        tableData.setItems(filteredData);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(teacher -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty() || cbSearch.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "ID":
                        if (teacher.getId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Name":
                        if (teacher.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Email":
                        if (teacher.getEmail().toLowerCase().contains(lowerCaseFilter)) {
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
        colName.setEditable(true);
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            Teacher teacher = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("Field cannot be empty").showError());
            } else {
                teacher.setName(event.getNewValue());
                daTeacher.edit(teacher);
            }
        });

        colId.setEditable(true);
        colId.setCellFactory(TextFieldTableCell.forTableColumn());
        colId.setOnEditCommit(event -> {
            Teacher teacher = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexById(teacher.getId());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else if (isDuplicateID(event.getNewValue())) {
                undoEdit();
                Notifications.create().title("Add Error").text("Duplicate ID").showError();
            } else {
                teacher.setId(event.getNewValue());
                daTeacher.edit(teacher);
                controlSplash.teacherList.get(index).setId(event.getNewValue());
            }
        });

        colEmail.setEditable(true);
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Teacher teacher = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexByEmail(teacher.getEmail());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Notifications.create().title("Edit Error").text("Field cannot be empty").showError();
            } else if (!Config.pattern.matcher(event.getNewValue()).matches()) {
                undoEdit();
                Notifications.create().title("Edit Error")
                        .text("Email isn't valid")
                        .showError();
            } else if (isDuplicateEmail(event.getNewValue())) {
                undoEdit();
                Notifications.create().title("Add Error").text("Duplicate Email").showError();
            } else {
                teacher.setEmail(event.getNewValue());
                daTeacher.edit(teacher);
                controlSplash.teacherList.get(index).setEmail(event.getNewValue());
            }
        });
    }

    private void undoEdit() {
        Platform.runLater(() -> {
            tableData.getColumns().get(0).setVisible(false);
            tableData.getColumns().get(0).setVisible(true);
        });
    }

    private boolean isDuplicateID(String teacherId) {
        return listData.stream().anyMatch(teacher -> teacher.getId().equals(teacherId));
    }

    private boolean isDuplicateEmail(String teacherEmail) {
        return listData.stream().anyMatch(teacher -> teacher.getEmail().equals(teacherEmail));
    }

    private void clear() {
        tfId.clear();
        tfName.clear();
        tfEmail.clear();
    }

    private int getIndexById(String teacherId) {
        for (int i = 0; i < controlSplash.teacherList.size(); i++) {
            if (controlSplash.teacherList != null
                    && controlSplash.teacherList.get(i).getId().equals(teacherId)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexByEmail(String teacherEmail) {
        for (int i = 0; i < controlSplash.teacherList.size(); i++) {
            if (controlSplash.teacherList != null
                    && controlSplash.teacherList.get(i).getEmail().equals(teacherEmail)) {
                return i;
            }
        }
        return -1;
    }
}
