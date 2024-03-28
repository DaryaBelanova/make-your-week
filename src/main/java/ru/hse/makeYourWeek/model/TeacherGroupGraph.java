package ru.hse.makeYourWeek.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.entities.TimeSlot;
import ru.hse.makeYourWeek.repository.TeacherGroupRepo;

import java.util.*;

@Getter
@Component
public class TeacherGroupGraph {
    private List<Vertex> adjacencyList;
    private int verticesCount;
    private final TeacherGroupRepo teacherGroupRepo;
    private final GroupsGraph groupsGraph;

    @Autowired
    public TeacherGroupGraph(TeacherGroupRepo teacherGroupRepo, GroupsGraph groupsGraph) {
        this.teacherGroupRepo = teacherGroupRepo;
        this.groupsGraph = groupsGraph;
        build();
    }

    public void build() {
        adjacencyList = new ArrayList<>();

        verticesCount = (int) teacherGroupRepo.count();
        // здесь все тройки
        List<TeacherGroupAdjacency> all = teacherGroupRepo.findAll();

        for (int i = 0; i < verticesCount; i++) {
            // вносятся все вершины в мписок смежности, для которых будт добавляться пересечения
            adjacencyList.add(new Vertex(all.get(i)));
        }

        // для всех вершин из списка
        for (int i = 0; i < adjacencyList.size(); i++) {
            // выбираем вершину, которой будем добавлять пересекающиеся с ней
            Vertex toMakeAdjacent = adjacencyList.get(i);
            // для всех вершин, кроме ее самой
            for (int j = 0; j < adjacencyList.size(); j++) {
                if (i == j) {
                    continue;
                }
                // берем вершину для проверки
                Vertex toCheck = adjacencyList.get(j);
                // если у вершин совпадают id учителей
                if (toMakeAdjacent.value.getTeacherId().equals(toCheck.value.getTeacherId())
                        // или если у вершин совпадают id групп
                        || toMakeAdjacent.value.getGroupId().equals(toCheck.value.getGroupId())
                        // или если у анализируемой вершины в графе вершин в списке смежности есть id второй вершины (т.е. они пересекаются)
                        || groupsGraph.getAdjacencyList().get(toMakeAdjacent.value.getGroupId() - 1).contains(toCheck.value.getGroupId() - 1)) {
                    toMakeAdjacent.adjacencyList.add(toCheck);
                    toMakeAdjacent.adjacentCount++;
                    //toMakeAdjacent.adjacentColorsCount += toCheck.colors.size();
                }
            }
        }
    }

    @Getter
    public static class Vertex {
        private Integer id;
        private TeacherGroupAdjacency value;
        private List<Vertex> adjacencyList;
        private Set<TimeSlot> colors;
        private Set<TimeSlot> adjacentColors;
        private Integer adjacentCount;
        private Integer adjacentColorsCount;
        public Vertex(TeacherGroupAdjacency adjacency) {
            id = adjacency.getId();
            value = adjacency;
            adjacencyList = new ArrayList<>();
            colors = new HashSet<>();
            adjacentColors = new HashSet<>();
            adjacentCount = 0;
            adjacentColorsCount = 0;
        }
        public HashSet<TimeSlot> getAdjacentColors() {
            HashSet<TimeSlot> result = new HashSet<>();
            for (Vertex adjacent : adjacencyList) {
                result.addAll(adjacent.colors);
            }
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
