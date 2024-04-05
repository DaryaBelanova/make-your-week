package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Integer> {

    @Query(value = "SELECT * FROM teachers WHERE name=?",nativeQuery = true)
    public Teacher findByName(String name);
}
