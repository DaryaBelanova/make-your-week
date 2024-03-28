package ru.hse.makeYourWeek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.entities.TimeSlot;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.services.ColorService;
import ru.hse.makeYourWeek.services.GroupService;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Set;

@Controller
@FxmlView("timeTable.fxml")
public class TimeTableController {
    public VBox mon1;
    public VBox tue1;
    public VBox wed1;
    public VBox thu1;
    public VBox fri1;

    public VBox mon2;
    public VBox tue2;
    public VBox wed2;
    public VBox thu2;
    public VBox fri2;

    public VBox mon3;
    public VBox tue3;
    public VBox wed3;
    public VBox thu3;
    public VBox fri3;

    public VBox mon4;
    public VBox tue4;
    public VBox wed4;
    public VBox thu4;
    public VBox fri4;

    public VBox mon5;
    public VBox tue5;
    public VBox wed5;
    public VBox thu5;
    public VBox fri5;

    public VBox mon6;
    public VBox tue6;
    public VBox wed6;
    public VBox thu6;
    public VBox fri6;

    public VBox mon7;
    public VBox tue7;
    public VBox wed7;
    public VBox thu7;
    public VBox fri7;

    @Autowired
    private ColorService colorService;
    @Autowired
    private GroupService groupService;
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
        changeTab(groupsButton, "groupsToDisplay.fxml");
    }

    public void onActionGenerateButtonClick(ActionEvent event) {
        TeacherGroupGraph teacherGroupGraph = ApplicationContextHolder.getApplicationContext().getBean(TeacherGroupGraph.class);
        /*for (int i = 0; i < teacherGroupGraph.getAdjacencyList().size(); i++) {
            TeacherGroupGraph.Vertex vertex = teacherGroupGraph.getAdjacencyList().get(i);
            System.out.println(vertex.getValue() + " adj colors = " + vertex.getAdjacentColors() + "; colors = " + vertex.getColors());
            System.out.println("Adj vertices: ");
            for (int j = 0; j < vertex.getAdjacencyList().size(); j++) {
                System.out.print(vertex.getAdjacencyList().get(j).getValue() + " ");
            }
            System.out.println();
            System.out.println();
        }*/
        colorService.colorizeTeacherGroupGraph(teacherGroupGraph);

        for (TeacherGroupGraph.Vertex vertex : teacherGroupGraph.getAdjacencyList()) {
            Group group = groupService.getById(vertex.getValue().getGroupId());
            Set<TimeSlot> vertexColors = vertex.getColors();
            TeacherGroupAdjacency adjacency = vertex.getValue();
            for (TimeSlot timeSlot : vertexColors) {
                VBox slot = getVBoxByTimeSlot(timeSlot);
                Text text = new Text(group.getName());
                slot.getChildren().add(text);
            }
        }
    }

    public void onActionShowForGroupsButtonClick(TeacherGroupGraph teacherGroupGraph) {

    }

    public void onActionShowForTeachersButtonClick(TeacherGroupGraph teacherGroupGraph) {

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

    private VBox getVBoxByTimeSlot(TimeSlot timeSlot) {
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 1) {
            return mon1;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 2) {
            return mon2;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 3) {
            return mon3;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 4) {
            return mon4;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 5) {
            return mon5;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 6) {
            return mon6;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 7) {
            return mon7;
        }

        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 1) {
            return tue1;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 2) {
            return tue2;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 3) {
            return tue3;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 4) {
            return tue4;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 5) {
            return tue5;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 6) {
            return tue6;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 7) {
            return tue7;
        }

        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 1) {
            return wed1;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 2) {
            return wed2;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 3) {
            return wed3;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 4) {
            return wed4;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 5) {
            return wed5;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 6) {
            return wed6;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 7) {
            return wed7;
        }

        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 1) {
            return thu1;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 2) {
            return thu2;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 3) {
            return thu3;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 4) {
            return thu4;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 5) {
            return thu5;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 6) {
            return thu6;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 7) {
            return thu7;
        }

        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 1) {
            return fri1;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 2) {
            return fri2;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 3) {
            return fri3;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 4) {
            return fri4;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 5) {
            return fri5;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 6) {
            return fri6;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 7) {
            return fri7;
        }
        return null;
    }
}
