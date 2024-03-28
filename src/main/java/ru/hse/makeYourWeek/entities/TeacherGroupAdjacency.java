package ru.hse.makeYourWeek.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "teachers_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TeacherGroupAdjacency {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "teacher_id")
    private Integer teacherId;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "count_per_week")
    private Integer countPerWeek;

    public TeacherGroupAdjacency(Integer teacherId, Integer groupId, Integer countPerWeek) {
        this.teacherId = teacherId;
        this.groupId = groupId;
        this.countPerWeek = countPerWeek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherGroupAdjacency that = (TeacherGroupAdjacency) o;
        return Objects.equals(teacherId, that.teacherId) && Objects.equals(groupId, that.groupId) && Objects.equals(countPerWeek, that.countPerWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacherId, groupId, countPerWeek);
    }

    @Override
    public String toString() {
        return "TeacherGroupAdjacency{" +
                "teacherId=" + teacherId +
                ", groupId=" + groupId +
                ", countPerWeek=" + countPerWeek +
                '}';
    }
}
