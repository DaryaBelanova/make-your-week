package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.GroupsAdjacencyPair;
import ru.hse.makeYourWeek.repository.GroupAdjacencyRepo;

import java.util.List;

@Service
public class GroupsAdjacencyService {
    @Autowired
    private GroupAdjacencyRepo groupAdjacencyRepo;

    public List<GroupsAdjacencyPair> getAll() {
        return groupAdjacencyRepo.findAll();
    }
}
