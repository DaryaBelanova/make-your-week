package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.dao.Group;

@Repository
public interface GroupRepo extends JpaRepository<Group, Integer> {
}
