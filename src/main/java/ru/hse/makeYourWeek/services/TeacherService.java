package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Teacher;
import ru.hse.makeYourWeek.repository.TeacherRepo;

import java.util.List;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepo teacherRepo;

    public List<Teacher> getAll() {
        return teacherRepo.findAll();
    }
}
