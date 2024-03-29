package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.TimeSlot;
import ru.hse.makeYourWeek.repository.TimeSlotRepo;

import java.util.List;

@Service

public class TimeSlotService {
    @Autowired
    private TimeSlotRepo timeSlotRepo;

    public TimeSlot getById(Integer id) {
        return timeSlotRepo.findById(id).orElse(null);
    }

    public List<TimeSlot> getAll() {
        return timeSlotRepo.findAll();
    }
}
