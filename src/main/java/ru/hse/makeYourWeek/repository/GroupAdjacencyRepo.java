package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.GroupsAdjacencyPair;

@Repository
public interface GroupAdjacencyRepo extends JpaRepository<GroupsAdjacencyPair, Integer> {
}
