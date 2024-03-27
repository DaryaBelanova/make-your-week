package ru.hse.makeYourWeek.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GroupToDisplay {
    private Integer id;
    private String name;
    private List<Integer> adjacentGroupsIDs;

    public GroupToDisplay(Group group, List<Integer> adjacentGroupsIDs) {
        id = group.getId();
        name = group.getName();
        this.adjacentGroupsIDs = adjacentGroupsIDs;
    }
}
