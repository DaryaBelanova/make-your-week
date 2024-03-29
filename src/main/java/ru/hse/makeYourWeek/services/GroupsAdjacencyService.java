package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.GroupsAdjacencyPair;
import ru.hse.makeYourWeek.repository.GroupAdjacencyRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.List;

@Service
public class GroupsAdjacencyService {
    @Autowired
    private GroupAdjacencyRepo groupAdjacencyRepo;
    @Autowired
    private TimeTableRepo timeTableRepo;

    public List<GroupsAdjacencyPair> getAll() {
        return groupAdjacencyRepo.findAll();
    }

    public List<GroupsAdjacencyPair> deleteAndSaveNew(List<GroupsAdjacencyPair> pairs) {
        groupAdjacencyRepo.deleteAll();
        timeTableRepo.deleteAll();
        for (int i = 0; i < pairs.size(); i++) {
            pairs.get(i).setId(i + 1);
        }
        return groupAdjacencyRepo.saveAll(pairs);
    }
}
