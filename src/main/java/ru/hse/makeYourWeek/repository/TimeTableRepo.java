package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.TimeTable;

@Repository
public interface TimeTableRepo extends JpaRepository<TimeTable, Integer> {
}
