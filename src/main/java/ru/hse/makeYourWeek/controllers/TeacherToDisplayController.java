package ru.hse.makeYourWeek.controllers;

import au.com.bytecode.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.entities.TeacherToDisplay;
import ru.hse.makeYourWeek.services.*;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@FxmlView("teachersToDisplay.fxml")
public class TeacherToDisplayController {
    public Button mainButton;
    public Button groupsButton;
    public Button timeTableButton;
    public Button uploadButton;
    public AnchorPane anchorPane;
    public Button uploadGroupsForTeachersButton;


    @FXML
    private TableView<TeacherToDisplay> teacherTableView;
    @FXML
    private TableColumn<TeacherToDisplay, String> name;
    @FXML
    private TableColumn<TeacherToDisplay, Integer> id;
    public TableColumn<TeacherToDisplay, VBox> groupsWithCountPerWeek;


    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherToDisplayService teacherToDisplayService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherGroupAdjacencyService teacherGroupAdjacencyService;
    @Autowired
    private TimeTableService timeTableService;

    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        //groups.setCellValueFactory(new PropertyValueFactory<>("groups"));
        groupsWithCountPerWeek.setCellValueFactory(new PropertyValueFactory<>("toDisplay"));
        displayTeachers();
    }

    private void displayTeachers() {
        teacherTableView.getItems().clear();
        // заполняем таблицу данными
        teacherTableView.getItems().addAll(teacherToDisplayService.getAll());
    }

    public void onActionMainButtonClick(ActionEvent event) throws IOException {
        changeTab(mainButton, "main.fxml");
    }

    public void onActionGroupsButtonClick(ActionEvent event) throws IOException {
        changeTab(groupsButton, "groupsToDisplay.fxml");
    }

    public void onActionTimeTableButtonClick(ActionEvent event) throws IOException {
        changeTab(timeTableButton, "timeTable.fxml");
    }

    private void changeTab(Button onClick, String fxmlFileName) throws IOException {
        //Close current
        Stage stage = (Stage) onClick.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setControllerFactory(ApplicationContextHolder.getApplicationContext()::getBean);
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ПоНедельник");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onActionUploadButtonClick(ActionEvent event) throws IOException {
        if (timeTableService.getRecordsCount() == 0) {
            uploadTeachers();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Окно подтверждения");
        alert.setHeaderText("Подтверждение загрузки файла");
        alert.setContentText("Внимание! Обновление данных приведет к неактуальности расписания, поэтому оно будет безвозвратно удалено.\n" +
                "Необходимо выгрузить файл с расписанием, чтобы его сохранить.\n" +
                "Хотите продолжить обновление данных?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Продолжить выполнение метода по загрузке файла
                uploadTeachers();
            } else {
                // Перенаправить пользователя на окно расписания
                try {
                    changeTab(timeTableButton, "timeTable.fxml");
                } catch (IOException e) {

                }
            }
        });


    }

    private void uploadTeachers() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            List<Teacher> newTeachers = new ArrayList<>();
            // если ФИО одинаковые, нужно приписать какой-то различитель, например, предмет, который ведет учитель
            List<String> usedTeachersFio = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        if (record.length != 3) {
                            throw new Exception("Неверный формат заполнения файла");
                        }
                        Integer id = Integer.valueOf(record[0]);
                        String name = record[1];
                        if (usedTeachersFio.contains(name)) {
                            throw new Exception("Этот учитель уже был внесен. Если ФИО двух учителей совпадают, добавьте к ним различитель, например, предмет, который ведет учитель");
                        }
                        Integer workingHours = Integer.valueOf(record[2]);
                        newTeachers.add(new Teacher(id, name, workingHours));
                        usedTeachersFio.add(name);
                    } catch (Exception e) {
                        displayAlert("Ошибка обработки файла", "Ошибка!");
                        return;
                    }
                }
                displayAlert("Файл успешно загружен", "Успех!");
                saveNewTeachersToDB(newTeachers);
            } catch (FileNotFoundException e) {
                displayAlert("Ошибка загрузки файла", "Ошибка!");
            } catch (IOException e) {
                displayAlert("Ошибка обработки файла", "Ошибка!");
            }
        } else {
            //displayAlert("Ошибка загрузки файла", "Ошибка!");
        }
    }

    public void onActionUploadGroupsForTeachersButtonClick(ActionEvent event) throws IOException {
        if (timeTableService.getRecordsCount() == 0) {
            uploadGroupsForTeachers();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Окно подтверждения");
        alert.setHeaderText("Подтверждение загрузки файла");
        alert.setContentText("Внимание! Обновление данных приведет к неактуальности расписания, поэтому оно будет безвозвратно удалено.\n" +
                "Необходимо выгрузить файл с расписанием, чтобы его сохранить.\n" +
                "Хотите продолжить обновление данных?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Продолжить выполнение метода по загрузке файла
                uploadGroupsForTeachers();
            } else {
                // Перенаправить пользователя на окно расписания
                try {
                    changeTab(timeTableButton, "timeTable.fxml");
                } catch (IOException e) {

                }
            }
        });

    }

    private void uploadGroupsForTeachers() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка файла");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            List<TeacherGroupAdjacency> newTeacherGroupAdjacencies = new ArrayList<>();
            List<TeacherGroupAdjacency> used = new ArrayList<>();

            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        if (record.length != 3) {
                            throw new Exception("Неверный формат заполнения файла");
                        }
                        Integer teacherId = Integer.valueOf(record[0]);
                        Integer groupId = Integer.valueOf(record[1]);
                        Integer countPerWeek = Integer.valueOf(record[2]);
                        if (teacherService.getById(teacherId) == null || groupService.getById(groupId) == null) {
                            throw new Exception("Нет учителя или группы с таким ID");
                        }
                        TeacherGroupAdjacency teacherGroupAdjacency = new TeacherGroupAdjacency(teacherId, groupId, countPerWeek);
                        if (used.contains(teacherGroupAdjacency)) {
                            throw new Exception("Данная связь уже была внесена");
                        }
                        newTeacherGroupAdjacencies.add(teacherGroupAdjacency);
                        used.add(teacherGroupAdjacency);
                    } catch (Exception e) {
                        displayAlert("Ошибка обработки файла", "Ошибка!");
                        return;
                    }
                }
                displayAlert("Файл успешно загружен", "Успех!");
                saveNewTeacherGroupAdjacenciesToDB(newTeacherGroupAdjacencies);
            } catch (FileNotFoundException e) {
                displayAlert("Ошибка загрузки файла", "Ошибка!");
            } catch (IOException e) {
                displayAlert("Ошибка обработки файла", "Ошибка!");
            }
        } else {
            //displayAlert("Ошибка загрузки файла", "Ошибка!");
        }
    }

    private void saveNewTeacherGroupAdjacenciesToDB(List<TeacherGroupAdjacency> teacherGroupAdjacencies) {
        List<TeacherGroupAdjacency> saved = teacherGroupAdjacencyService.deleteAndSaveNew(teacherGroupAdjacencies);
        displayTeachers();
    }

    private void saveNewTeachersToDB(List<Teacher> teachers) {
        List<Teacher> saved = teacherService.deleteAndSaveNew(teachers);
        displayTeachers();
    }

    private void displayAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
