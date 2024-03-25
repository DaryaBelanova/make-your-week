package ru.hse.makeYourWeek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.dao.GroupsAdjacencyPair;
import ru.hse.makeYourWeek.repository.GroupRepo;
import ru.hse.makeYourWeek.repository.GroupAdjacencyRepo;

import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class GroupsGraph {
    private List<List<Integer>> adjacencyList;
    private int verticesCount;
    private final GroupAdjacencyRepo groupAdjacencyRepo;
    private final GroupRepo groupRepo;

    @Autowired
    public GroupsGraph(GroupAdjacencyRepo groupAdjacencyRepo, GroupRepo groupRepo) {
        this.groupAdjacencyRepo = groupAdjacencyRepo;
        this.groupRepo = groupRepo;
        build();
    }

    public void build() {
        adjacencyList = new ArrayList<>();

        verticesCount = (int) groupRepo.count();
        for (int i = 0; i < verticesCount; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (GroupsAdjacencyPair pair : groupAdjacencyRepo.findAll()) {
            adjacencyList.get(pair.getGroup1Id() - 1).add(pair.getGroup2Id() - 1);
            adjacencyList.get(pair.getGroup2Id() - 1).add(pair.getGroup1Id() - 1);
        }
    }
}
