package ru.hse.makeYourWeek.controllers;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.ApplicationContextHolder;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.services.TeacherService;

import java.io.IOException;

@Controller
@FxmlView("teachers.fxml")
public class TeacherController {

    public Button mainButton;

    public Button groupsButton;
    public Button timeTableButton;
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
        teacherTableView.getItems().clear();
        // заполняем таблицу данными
        teacherTableView.getItems().addAll(teacherService.getAll());
        /*teacherTableView.setItems(teachers);
        teachers.addAll(teacherService.getAll());*/
    }

    public void onActionMainButtonClick(ActionEvent event) throws IOException{
        changeTab(mainButton, "main.fxml");
    }

    public void onActionGroupsButtonClick(ActionEvent event) throws IOException{
        changeTab(groupsButton, "groups.fxml");
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
