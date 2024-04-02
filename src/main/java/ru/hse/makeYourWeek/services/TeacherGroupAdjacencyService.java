package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.List;

@Service
public class TeacherGroupAdjacencyService {
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;


    public List<TeacherGroupAdjacency> deleteAndSaveNew(List<TeacherGroupAdjacency> teacherGroupAdjacencies) {
        timeTableRepo.deleteAll();
        teacherGroupRepo.deleteAll();
        for (int i = 0; i < teacherGroupAdjacencies.size(); i++) {
            teacherGroupAdjacencies.get(i).setId(i + 1);
        }
        return teacherGroupRepo.saveAll(teacherGroupAdjacencies);
    }

    public List<TeacherGroupAdjacency> getAll() {
        return teacherGroupRepo.findAll();
    }

    public TeacherGroupAdjacency getById(Integer id) {
        return teacherGroupRepo.findById(id).orElse(null);
    }
}
