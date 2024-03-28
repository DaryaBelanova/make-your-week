package ru.hse.makeYourWeek.services;

import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;

import java.util.List;

@Service
public class TeacherGroupAdjacencyService {
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;


    public List<TeacherGroupAdjacency> deleteAndSaveNew(List<TeacherGroupAdjacency> teacherGroupAdjacencies) {
        teacherGroupRepo.deleteAll();
        for (int i = 0; i < teacherGroupAdjacencies.size(); i++) {
            teacherGroupAdjacencies.get(i).setId(i + 1);
        }
        return teacherGroupRepo.saveAll(teacherGroupAdjacencies);
    }
}
