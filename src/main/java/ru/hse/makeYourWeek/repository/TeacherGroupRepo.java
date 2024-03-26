package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;

@Repository
public interface TeacherGroupRepo extends JpaRepository<TeacherGroupAdjacency, Integer> {
}
