package ru.hse.makeYourWeek.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "groups_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupsAdjacencyPair {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "group1_id")
    private Integer group1Id;
    @Column(name = "group2_id")
    private Integer group2Id;

    public GroupsAdjacencyPair(Integer group1Id, Integer group2Id) {
        this.group1Id = group1Id;
        this.group2Id = group2Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupsAdjacencyPair that = (GroupsAdjacencyPair) o;
        return Objects.equals(group1Id, that.group1Id) && Objects.equals(group2Id, that.group2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group1Id, group2Id);
    }
}
