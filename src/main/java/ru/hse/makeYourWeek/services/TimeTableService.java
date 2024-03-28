package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

@Service
public class TimeTableService {
    @Autowired
    private TimeTableRepo timeTableRepo;

    public void saveToDB(TeacherGroupGraph graph) {

    }
}
