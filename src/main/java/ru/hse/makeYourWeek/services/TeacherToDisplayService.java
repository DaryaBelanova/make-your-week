package ru.hse.makeYourWeek.services;

import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.*;
import ru.hse.makeYourWeek.model.GroupsGraph;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherToDisplayService {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;
    @Autowired
    private GroupService groupService;
    public List<TeacherToDisplay> getAll() {
        List<Teacher> teachersList = teacherService.getAll();

        List<TeacherToDisplay> teachersToDisplay = new ArrayList<>();

        List<TeacherGroupAdjacency> teacherGroupAdjacencies = teacherGroupRepo.findAll();
        Map<Teacher, List<String>> teacherWithGroups = new HashMap<>();
        for (Teacher teacher : teachersList) {
            teacherWithGroups.put(teacher, new ArrayList<>());
        }
        for (TeacherGroupAdjacency teacherGroupAdjacency : teacherGroupAdjacencies) {
            Teacher teacher = teacherService.getById(teacherGroupAdjacency.getTeacherId());
            Group group = groupService.getById(teacherGroupAdjacency.getGroupId());

            String lessonsPerWeek = "";
            if (teacherGroupAdjacency.getCountPerWeek() == 1) {
                lessonsPerWeek = " урок";
            }
            if (teacherGroupAdjacency.getCountPerWeek() == 2 || teacherGroupAdjacency.getCountPerWeek() == 3 || teacherGroupAdjacency.getCountPerWeek() == 4) {
                lessonsPerWeek = " урока";
            }
            if (teacherGroupAdjacency.getCountPerWeek() > 4) {
                lessonsPerWeek = " уроков";
            }
            teacherWithGroups.get(teacher).add(group.getName() + " - " + teacherGroupAdjacency.getCountPerWeek() + lessonsPerWeek);
        }
        for (Map.Entry<Teacher, List<String>> pair : teacherWithGroups.entrySet()) {
            TeacherToDisplay teacherToDisplay = new TeacherToDisplay(pair.getKey(), pair.getValue());
            for (String group : pair.getValue()) {
                teacherToDisplay.getToDisplay().getChildren().add(new Text(group));
            }
            teachersToDisplay.add(teacherToDisplay);
        }
        return teachersToDisplay;
    }
}
