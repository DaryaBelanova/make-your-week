package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Integer> {
}
