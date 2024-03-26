package ru.hse.makeYourWeek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.ApplicationContextHolder;

import java.io.IOException;

@Controller
@FxmlView("timeTable.fxml")
public class TimeTableController {
    public Button mainButton;
    public Button teachersButton;
    public Button groupsButton;
    public Button generateButton;

    public void onActionMainButtonClick(ActionEvent event) throws IOException {
        changeTab(mainButton, "main.fxml");
    }

    public void onActionTeachersButtonClick(ActionEvent event) throws IOException {
        changeTab(teachersButton, "teachers.fxml");
    }

    public void onActionGroupsButtonClick(ActionEvent event) throws IOException {
        changeTab(groupsButton, "groups.fxml");
    }

    public void onActionGenerateButtonClick(ActionEvent event) {

    }

    private void changeTab(Button onClick, String fxmlFileName) throws IOException {
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
