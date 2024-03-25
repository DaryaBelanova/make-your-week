package ru.hse.makeYourWeek.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Objects;

@Entity
@Table(name = "lesson_times")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeSlot {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "lesson_number")
    private Integer lessonNumber;
    @Column(name = "in_day")
    private String inDay;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return Objects.equals(lessonNumber, timeSlot.lessonNumber) && Objects.equals(inDay, timeSlot.inDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonNumber, inDay);
    }
}
