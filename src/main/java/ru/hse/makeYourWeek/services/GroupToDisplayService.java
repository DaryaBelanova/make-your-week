package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.Group;
import ru.hse.makeYourWeek.entities.GroupToDisplay;
import ru.hse.makeYourWeek.model.GroupsGraph;
import ru.hse.makeYourWeek.repository.GroupAdjacencyRepo;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupToDisplayService {
    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherGroupRepo teacherGroupRepo;
    @Autowired
    private GroupAdjacencyRepo groupAdjacencyRepo;
    @Autowired
    private GroupRepo groupRepo;

    public List<GroupToDisplay> getAll() {
        List<Group> groupsList = groupService.getAll();

        GroupsGraph groupsGraph = ApplicationContextHolder.getApplicationContext().getBean(GroupsGraph.class);
        groupsGraph.build();

        List<GroupToDisplay> groupsToDisplay = new ArrayList<>();

        for (Group group : groupsList) {
            List<Integer> adjacentIDs = new ArrayList<>();
            int indexInGraph = group.getId() - 1;
            for (int i = 0; i < groupsGraph.getAdjacencyList().get(indexInGraph).size(); i++) {
                adjacentIDs.add(groupsGraph.getAdjacencyList().get(indexInGraph).get(i) + 1);
            }
            groupsToDisplay.add(new GroupToDisplay(group.getId(), group.getName(), adjacentIDs));
        }
        return groupsToDisplay;
    }
}
