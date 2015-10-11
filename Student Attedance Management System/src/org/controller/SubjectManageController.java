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
import javafx.util.converter.IntegerStringConverter;
import org.animations.FadeInUpTransition;
import org.config.Config;
import org.controlsfx.control.Notifications;
import org.databaseAccess.DASubject;
import org.model.Subject;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author 404NotFound
 */
public class SubjectManageController implements Initializable {

    FilteredList<Subject> filteredData;
    @FXML
    private AnchorPane paneData;
    @FXML
    private AnchorPane paneTable;
    @FXML
    private TableView<Subject> tableData;
    @FXML
    private TableColumn<Subject, String> colId;
    @FXML
    private TableColumn<Subject, String> colName;
    @FXML
    private TableColumn<Subject, Integer> colPeriod;
    @FXML
    private TextField tfSearch;
    @FXML
    private ComboBox<String> cbSearch;
    @FXML
    private Button btDelete;
    @FXML
    private AnchorPane inputPane;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPeriod;
    @FXML
    private Button btNew;
    private ObservableList<Subject> listData;
    private DASubject daSubject = new DASubject();
    private int currentUserLevel = LoginController.currentUser.getLevel();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        cbSearch.getItems().addAll("ID", "Name", "Period");
        if (currentUserLevel != 3) {
            setEditable();
        } else if (currentUserLevel == 3) {
            inputPane.setOpacity(0);
            btDelete.setOpacity(0);
            btNew.setDisable(true);
            btDelete.setDisable(true);
        }
        new FadeInUpTransition(paneTable).play();
        new FadeInUpTransition(inputPane).play();
        listData.addAll(daSubject.getAll());
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
            Subject tmp = tableData.getSelectionModel().getSelectedItem();
            int index = getIndexById(tmp.getId());
            listData.remove(index);
            controlSplash.subjectList.remove(index);
        }
    }

    @FXML
    private void addNew(ActionEvent event) {
        if (tfPeriod.getText().equals("") || tfName.getText().equals("") || tfId.getText().equals("")) {
            Notifications.create().title("Error!").text("All TextField must be filled").showError();
        } else if (!isInteger(tfPeriod.getText())) {
            Notifications.create().title("Error!").text("Period must be an Integer").showError();
        } else if (isDuplicateName(tfName.getText())) {
            Notifications.create().title("Add Error").text("Duplicate Name").showError();
        } else if (isDuplicateID(tfId.getText())) {
            Notifications.create().title("Add Error").text("Duplicate ID").showError();
        } else {
            String id = tfId.getText();
            String name = tfName.getText();
            int period = Integer.valueOf(tfPeriod.getText());
            Subject subject = new Subject(id, name, period);
            listData.add(subject);
            daSubject.add(subject);
            controlSplash.subjectList.add(subject);
            clear();
        }
    }

    private void initTable() {
        listData = FXCollections.observableArrayList();
        Config config = new Config();
        config.setModelColumn(colId, "id");
        config.setModelColumn(colName, "name");
        config.setModelColumn(colPeriod, "period");
        tableData.setItems(listData);
        filteredData = new FilteredList<>(listData, p -> true);
        tableData.setItems(filteredData);
        tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(subject -> {
                // If filter text is empty, display all
                if (newValue == null || newValue.isEmpty() || cbSearch.getValue() == null) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                switch (cbSearch.getValue()) {
                    case "ID":
                        if (subject.getId().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Name":
                        if (subject.getName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        break;
                    case "Period":
                        if (subject.getPeriod().equals(Integer.valueOf(newValue))) {
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
            Subject sbj = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexByName(sbj.getName());
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("Field cannot be empty").showError());
            } else if (isDuplicateName(event.getNewValue())) {
                Notifications.create().title("Add Error").text("Duplicate Name").showError();
            } else {
                sbj.setName(event.getNewValue());
                daSubject.edit(sbj);
                controlSplash.subjectList.get(index).setName(event.getNewValue());
            }
        });

        colId.setEditable(true);
        colId.setCellFactory(TextFieldTableCell.forTableColumn());
        colId.setOnEditCommit(event -> {
            Subject sbj = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexById(sbj.getId());
            System.out.println(index);
            if (event.getNewValue().equals("")) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("Field cannot be empty").showError());
            } else if (isDuplicateID(event.getNewValue())) {
                Notifications.create().title("Error!").text("Duplicate ID").showError();
                undoEdit();
            } else {
                sbj.setId(event.getNewValue());
                controlSplash.subjectList.get(index).setId(event.getNewValue());
                daSubject.edit(sbj);
            }
        });

        colPeriod.setEditable(true);
        colPeriod.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPeriod.setOnEditCommit(event -> {
            Subject sbj = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int index = getIndexByPeriod(sbj.getPeriod());
            if (event.getNewValue().toString().equals("")) {
                undoEdit();
                Platform.runLater(() -> Notifications.create().title("Edit Error").text("Field cannot be empty").showError());
            } else {
                sbj.setPeriod(event.getNewValue());
                controlSplash.subjectList.get(index).setPeriod(event.getNewValue());
                daSubject.edit(sbj);
            }
        });
    }

    private void undoEdit() {
        Platform.runLater(() -> {
            tableData.getColumns().get(0).setVisible(false);
            tableData.getColumns().get(0).setVisible(true);
        });
    }

    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    private boolean isDuplicateID(String subjectID) {
        return listData.stream().anyMatch(subject -> subject.getId().equals(subjectID));
    }

    private boolean isDuplicateName(String subjectName) {
        return listData.stream().anyMatch(subject -> subject.getName().equals(subjectName));
    }

    private void clear() {
        tfId.clear();
        tfName.clear();
        tfPeriod.clear();
    }

    private int getIndexById(String subjectId) {
        for (int i = 0; i < controlSplash.subjectList.size(); i++) {
            if (controlSplash.subjectList != null
                    && controlSplash.subjectList.get(i).getId().equals(subjectId)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexByName(String subjectName) {
        for (int i = 0; i < controlSplash.subjectList.size(); i++) {
            if (controlSplash.subjectList != null
                    && controlSplash.subjectList.get(i).getName().equals(subjectName)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexByPeriod(int period) {
        for (int i = 0; i < controlSplash.subjectList.size(); i++) {
            if (controlSplash.subjectList != null
                    && controlSplash.subjectList.get(i).getPeriod() == period) {
                return i;
            }
        }
        return -1;
    }

}
