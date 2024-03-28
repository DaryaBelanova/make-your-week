package ru.hse.makeYourWeek.entities;

import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class TeacherToDisplay {
    private Integer id;
    private String name;
    private Integer workingHours;
    private VBox toDisplay;
    private List<String> groupsWithCountPerWeek;

    public TeacherToDisplay(Teacher teacher, List<String> groups) {
        id = teacher.getId();
        name = teacher.getName();
        workingHours = teacher.getWorkingHours();
        this.groupsWithCountPerWeek = groups;
        toDisplay = new VBox();
    }
}
