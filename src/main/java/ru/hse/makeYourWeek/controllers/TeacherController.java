package ru.hse.makeYourWeek.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.dao.Teacher;
import ru.hse.makeYourWeek.repository.TeacherRepo;

@Controller
public class TeacherController {
    private final TeacherRepo teacherRepo;

    public TeacherController(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    private ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Teacher, String> name;
    @FXML
    private TableColumn<Teacher, Integer> id;
    @FXML
    private TableColumn<Teacher, Integer> workingHours;
    @FXML
    private TableView<Teacher> teacherTableView;


    public void onShowDataButtonClick() {
        // устанавливаем тип и значение которое должно хранится в колонке
        id.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name.setCellValueFactory(new PropertyValueFactory<>("ФИО"));
        workingHours.setCellValueFactory(new PropertyValueFactory<>("Количество рабочих часов"));
        // заполняем таблицу данными
        teacherTableView.setItems(teachers);
        teachers.addAll(teacherRepo.findAll());
    }
}
