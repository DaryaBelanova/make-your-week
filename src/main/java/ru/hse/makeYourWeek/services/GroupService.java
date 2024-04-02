package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.repository.GroupAdjacencyRepo;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;
    @Autowired
    private GroupAdjacencyRepo groupAdjacencyRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;

    public List<Group> getAll() {
        return groupRepo.findAll();
    }

    public Group getById(Integer id) {
        return groupRepo.findById(id).orElse(null);
    }

    public List<Group> deleteAndSaveNew(List<Group> groups) {
        timeTableRepo.deleteAll();
        teacherGroupRepo.deleteAll();
        groupAdjacencyRepo.deleteAll();
        groupRepo.deleteAll();
        return groupRepo.saveAll(groups);
    }
}
