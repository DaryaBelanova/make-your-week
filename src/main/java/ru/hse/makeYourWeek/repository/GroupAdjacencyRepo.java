package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.dao.GroupsAdjacencyPair;

@Repository
public interface GroupAdjacencyRepo extends JpaRepository<GroupsAdjacencyPair, Integer> {
}
