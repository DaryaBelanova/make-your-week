package ru.hse.makeYourWeek.outdated;

import au.com.bytecode.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.services.TeacherService;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@FxmlView("teachers.fxml")
public class TeacherController {

    public Button mainButton;

    public Button groupsButton;
    public Button timeTableButton;
    public Button uploadButton;
    public AnchorPane anchorPane;
    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    @FXML
    private TableView<Teacher> teacherTableView;
    @FXML
    private TableColumn<Teacher, String> name;
    @FXML
    private TableColumn<Teacher, Integer> id;

    @Autowired
    private TeacherService teacherService;

    /*public void onShowDataButtonClick() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherTableView.getItems().clear();
        // заполняем таблицу данными
        teacherTableView.getItems().addAll(teacherService.getAll());
        *//*teacherTableView.setItems(teachers);
        teachers.addAll(teacherService.getAll());*//*
    }*/

    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        displayTeachers();
    }

    private void displayTeachers() {
        teacherTableView.getItems().clear();
        // заполняем таблицу данными
        teacherTableView.getItems().addAll(teacherService.getAll());
        /*teacherTableView.setItems(teachers);
        teachers.addAll(teacherService.getAll());*/
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
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            List<Teacher> newTeachers = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        Integer id = Integer.valueOf(record[0]);
                        String name = record[1];
                        Integer workingHours = Integer.valueOf(record[2]);
                        newTeachers.add(new Teacher(id, name, workingHours));
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

    private void saveNewTeachersToDB(List<Teacher> teachers) {
        List<Teacher> saved = teacherService.deleteAndSaveNew(teachers);
        //System.out.println(Arrays.toString(saved.toArray()));
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
