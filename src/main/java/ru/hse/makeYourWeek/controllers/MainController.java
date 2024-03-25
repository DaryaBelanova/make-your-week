package ru.hse.makeYourWeek.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.dao.Teacher;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.repository.TeacherRepo;

import java.io.IOException;

@Controller
@AllArgsConstructor
@FxmlView("MainController.fxml")
public class MainController {

    private final TeacherController teacherController;
    public void loadView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainController.class.getResource("MainController.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),620,480);
        stage.setScene(scene);
        stage.show();
    }

    public void onShowTeachersButtonClick() throws IOException{
        teacherController.onShowDataButtonClick();
    }
}
