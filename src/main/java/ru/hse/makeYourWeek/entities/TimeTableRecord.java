package ru.hse.makeYourWeek.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "timetable")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeTableRecord {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "teacher_group_id")
    private Integer teacherGroupId;
    @Column(name = "timeslot_id")
    private Integer timeSlotId;

    public TimeTableRecord(Integer teacherGroupId, Integer timeSlotId) {
        this.teacherGroupId = teacherGroupId;
        this.timeSlotId = timeSlotId;
    }
}
