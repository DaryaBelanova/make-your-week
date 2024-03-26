package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.repository.GroupRepo;

import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepo groupRepo;

    public List<Group> getAll() {
        return groupRepo.findAll();
    }
}
