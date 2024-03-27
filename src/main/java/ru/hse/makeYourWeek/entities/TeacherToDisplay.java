package ru.hse.makeYourWeek.entities;

import lombok.AllArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
public class TeacherToDisplay {
    private Integer id;
    private String name;
    private Integer workingHours;
    // TODO: choose type
    private List<String> groupNames;


}
