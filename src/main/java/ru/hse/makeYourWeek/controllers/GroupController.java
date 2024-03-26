package ru.hse.makeYourWeek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.ApplicationContextHolder;
import ru.hse.makeYourWeek.dao.Group;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.services.GroupService;

import java.io.IOException;

@Controller
@FxmlView("groups.fxml")
public class GroupController {
    public Button mainButton;
    @FXML
    public TableView<Group> groupTableView;
    @FXML
    public TableColumn<Group, Integer> id;
    @FXML
    public TableColumn<Group, String> name;
    public Button teachersButton;
    public Button timeTableButton;

    @Autowired
    private GroupService groupService;

    @FXML
    private void initialize() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupTableView.getItems().clear();
        // заполняем таблицу данными
        groupTableView.getItems().addAll(groupService.getAll());
        /*teacherTableView.setItems(teachers);
        teachers.addAll(teacherService.getAll());*/
    }

    /*public void onShowDataButtonClick(ActionEvent event) {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        groupTableView.getItems().clear();
        // заполняем таблицу данными
        groupTableView.getItems().addAll(groupService.getAll());
        *//*groupTableView.setItems(groups);
        groups.addAll(groupService.getAll());*//*
    }*/

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
}
