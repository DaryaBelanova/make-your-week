package ru.hse.makeYourWeek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.entities.GroupsAdjacencyPair;
import ru.hse.makeYourWeek.services.GroupService;
import ru.hse.makeYourWeek.services.GroupsAdjacencyService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@FxmlView("raw.fxml")
public class RawGroupController {
    public TilePane tilePane1;
    public TilePane tilePane2;
    public TilePane tilePane3;
    public TilePane tilePane4;
    public TilePane tilePane5;
    public TilePane tilePane6;
    public TilePane tilePane7;
    public TilePane tilePane8;
    public TilePane tilePane9;
    public TilePane tilePane10;
    public TilePane tilePane11;
    public Button mainButton;
    public Button teachersButton;
    public Button timeTableButton;

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupsAdjacencyService groupsAdjacencyService;



    @FXML
    private void initialize() {
        List<Group> allGroups = groupService.getAll();
        Map<String, Rectangle> used = new HashMap<>();
        //Map<String, StackPane> map = new HashMap<>();
        for (Group group : allGroups) {
            Rectangle rectangle = new Rectangle(78, 20);
            rectangle.setStroke(Color.GRAY);
            rectangle.setFill(Color.TRANSPARENT);

            Text text = new Text(group.getName());
            text.setTabSize(10);
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, text);
            //map.put(group.getName(), stackPane);

            getTilePaneByGroup(group.getName()).getChildren().add(stackPane);
            used.put(group.getName(), rectangle);
        }

        List<GroupsAdjacencyPair> adjacencyPairs = groupsAdjacencyService.getAll();

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
    private TilePane getTilePaneByGroup(String groupName) {
        if (groupName.contains("11")) return tilePane11;

        if (groupName.contains("10")) return tilePane10;

        if (groupName.contains("9")) return tilePane9;

        if (groupName.contains("8")) return tilePane8;

        if (groupName.contains("7")) return tilePane7;

        if (groupName.contains("6")) return tilePane6;

        if (groupName.contains("5")) return tilePane5;

        if (groupName.contains("4")) return tilePane4;

        if (groupName.contains("3")) return tilePane3;

        if (groupName.contains("2")) return tilePane2;

        return tilePane1;
    }
}
