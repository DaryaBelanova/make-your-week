package ru.hse.makeYourWeek.controllers;

import au.com.bytecode.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.entities.GroupToDisplay;
import ru.hse.makeYourWeek.entities.GroupsAdjacencyPair;
import ru.hse.makeYourWeek.services.GroupService;
import ru.hse.makeYourWeek.services.GroupToDisplayService;
import ru.hse.makeYourWeek.services.GroupsAdjacencyService;
import ru.hse.makeYourWeek.services.TimeTableService;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@FxmlView("groupsToDisplay.fxml")
public class GroupToDisplayController {
    @FXML
    public TableView<GroupToDisplay> groupToDisplayTableView;
    @FXML
    public TableColumn<GroupToDisplay, Integer> id;
    @FXML
    public TableColumn<GroupToDisplay, String> name;
    @FXML
    public TableColumn<GroupToDisplay, List<Integer>> adjacency;


    public Button mainButton;
    @FXML
    public Button uploadAdjacenciesButton;
    public Button teachersButton;
    public Button timeTableButton;
    public Button uploadButton;


    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupToDisplayService groupToDisplayService;
    @Autowired
    private GroupsAdjacencyService groupsAdjacencyService;
    @Autowired
    private TimeTableService timeTableService;

    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        adjacency.setCellValueFactory(new PropertyValueFactory<>("adjacentGroupsIDs"));
        displayGroups();
    }

    private void displayGroups() {
        groupToDisplayTableView.getItems().clear();
        groupToDisplayTableView.getItems().addAll(groupToDisplayService.getAll());
    }

    public void onActionMainButtonClick(ActionEvent event) throws IOException {
        changeTab(mainButton, "main.fxml");
    }

    public void onActionTeachersButtonClick(ActionEvent event) throws IOException {
        changeTab(teachersButton, "teachersToDisplay.fxml");
    }

    public void onActionTimeTableButtonClick(ActionEvent event) throws IOException{
        changeTab(timeTableButton, "timeTableImproved.fxml");
    }

    private void changeTab(Button onClick, String fxmlFileName) throws IOException{
        //Close current
        Stage stage = (Stage) onClick.getScene().getWindow();
        // do what you have to do
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
            uploadGroups();
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
                uploadGroups();
            } else {
                // Перенаправить пользователя на окно расписания
                try {
                    changeTab(timeTableButton, "timeTable.fxml");
                } catch (IOException e) {

                }
            }
        });

    }

    private void uploadGroups() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            List<Group> newGroups = new ArrayList<>();
            List<String> usedNames = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        if (record.length != 2) {
                            throw new Exception("Ошибка обработки файла");
                        }
                        Integer id = Integer.valueOf(record[0]);
                        String name = record[1];
                        if (!isValidGroupName(name)) {
                            throw new Exception();
                        }
                        if (usedNames.contains(name)) {
                            throw new Exception();
                        }
                        newGroups.add(new Group(id, name));
                        usedNames.add(name);
                    } catch (Exception e) {
                        displayAlert("Ошибка обработки файла", "Ошибка!");
                        return;
                    }
                }
                displayAlert("Файл успешно загружен", "Успех!");
                saveNewGroupsToDB(newGroups);
            } catch (FileNotFoundException e) {
                displayAlert("Ошибка загрузки файла", "Ошибка!");
            } catch (IOException e) {
                displayAlert("Ошибка обработки файла", "Ошибка!");
            }
        } else {
            //displayAlert("Ошибка загрузки файла", "Ошибка!");
        }
    }

    private boolean isValidGroupName(String groupName) {
        for (int i = 0; i < groupName.length(); i++) {
            if (Character.UnicodeBlock.of(groupName.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return true;
            }
        }
        return false;
    }

    public void onActionUploadAdjacenciesButtonClick() {
        if (timeTableService.getRecordsCount() == 0) {
            uploadAdjacencies();
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
                uploadAdjacencies();
            } else {
                // Перенаправить пользователя на другое окно
                try {
                    changeTab(timeTableButton, "timeTable.fxml");
                } catch (IOException e) {

                }
            }
        });

    }

    private void uploadAdjacencies() {
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка файла");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        List<GroupsAdjacencyPair> usedPairs = new ArrayList<>();
        if (selectedFile != null) {
            List<GroupsAdjacencyPair> newGroupsAdjacencyPairs = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        if (record.length != 2) {
                            throw new Exception("Неверный формат заполнения файла");
                        }
                        Integer firstId = Integer.valueOf(record[0]);
                        Integer secondId = Integer.valueOf(record[1]);
                        if (groupService.getById(firstId) == null || groupService.getById(secondId) == null) {
                            throw new Exception("Группы с таким ID не существует");
                        }
                        GroupsAdjacencyPair pair = new GroupsAdjacencyPair(firstId, secondId);
                        GroupsAdjacencyPair reversed = new GroupsAdjacencyPair(secondId, firstId);
                        if (usedPairs.contains(pair) || usedPairs.contains(reversed)) {
                            throw new Exception();
                        }
                        usedPairs.add(pair);
                        usedPairs.add(new GroupsAdjacencyPair(pair.getGroup2Id(), pair.getGroup1Id()));
                        newGroupsAdjacencyPairs.add(pair);
                    } catch (Exception e) {
                        displayAlert("Ошибка обработки файла", "Ошибка!");
                        return;
                    }
                }
                displayAlert("Файл успешно загружен", "Успех!");
                saveNewGroupsAdjacencyToDB(newGroupsAdjacencyPairs);
            } catch (FileNotFoundException e) {
                displayAlert("Ошибка загрузки файла", "Ошибка!");
            } catch (IOException e) {
                displayAlert("Ошибка обработки файла", "Ошибка!");
            }
        } else {
            //displayAlert("Ошибка загрузки файла", "Ошибка!");
        }
    }

    private void saveNewGroupsToDB(List<Group> groups) {
        List<Group> saved = groupService.deleteAndSaveNew(groups);
        //System.out.println(Arrays.toString(saved.toArray()));
        displayGroups();
    }

    private void saveNewGroupsAdjacencyToDB(List<GroupsAdjacencyPair> pairs) {
        List<GroupsAdjacencyPair> saved = groupsAdjacencyService.deleteAndSaveNew(pairs);
        displayGroups();
    }

    private void displayAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
