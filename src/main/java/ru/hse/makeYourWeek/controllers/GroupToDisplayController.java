package ru.hse.makeYourWeek.controllers;

import au.com.bytecode.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.entities.GroupToDisplay;
import ru.hse.makeYourWeek.services.GroupService;
import ru.hse.makeYourWeek.services.GroupToDisplayService;
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
    public Button mainButton;
    @FXML
    public TableView<GroupToDisplay> groupToDisplayTableView;
    @FXML
    public TableColumn<GroupToDisplay, Integer> id;
    @FXML
    public TableColumn<GroupToDisplay, String> name;
    @FXML
    public TableColumn<GroupToDisplay, List<Integer>> adjacency;

    public Button teachersButton;
    public Button timeTableButton;
    public Button uploadButton;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupToDisplayService groupToDisplayService;

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
        changeTab(teachersButton, "teachers.fxml");
    }

    public void onActionTimeTableButtonClick(ActionEvent event) throws IOException{
        changeTab(timeTableButton, "timeTable.fxml");
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
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            List<Group> newGroups = new ArrayList<>();
            try (CSVReader reader = new CSVReader(new FileReader(selectedFile))) {
                String[] record = reader.readNext();
                while ((record = reader.readNext()) != null) {
                    try {
                        Integer id = Integer.valueOf(record[0]);
                        String name = record[1];
                        newGroups.add(new Group(id, name));
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

    private void saveNewGroupsToDB(List<Group> groups) {
        List<Group> saved = groupService.deleteAndSaveNew(groups);
        //System.out.println(Arrays.toString(saved.toArray()));
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
