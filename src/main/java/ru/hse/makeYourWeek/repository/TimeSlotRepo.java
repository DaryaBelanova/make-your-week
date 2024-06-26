package ru.hse.makeYourWeek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hse.makeYourWeek.entities.TimeSlot;

@Repository
public interface TimeSlotRepo extends JpaRepository<TimeSlot, Integer> {
}
