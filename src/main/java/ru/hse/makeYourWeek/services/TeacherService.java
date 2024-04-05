package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;
import ru.hse.makeYourWeek.repository.TeacherRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepo teacherRepo;
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;

    public List<Teacher> getAll() {
        return teacherRepo.findAll();
    }

    public List<Teacher> deleteAndSaveNew(List<Teacher> teachers) {
        timeTableRepo.deleteAll();
        teacherGroupRepo.deleteAll();
        teacherRepo.deleteAll();
        return teacherRepo.saveAll(teachers);
    }

    public Teacher getById(Integer id) {
        return teacherRepo.findById(id).orElse(null);
    }

    public Teacher getByName(String name) {
        return teacherRepo.findByName(name);
    }
}
