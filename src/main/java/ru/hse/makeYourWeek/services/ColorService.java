package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.TimeSlot;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.TimeSlotRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.List;

@Service
public class ColorService {
    private final TimeSlotRepo timeSlotRepo;
    private final TimeTableRepo timeTableRepo;

    @Autowired
    public ColorService(TimeSlotRepo timeSlotRepo, TimeTableRepo timeTableRepo) {
        this.timeSlotRepo = timeSlotRepo;
        this.timeTableRepo = timeTableRepo;
    }

    public boolean colorizeTeacherGroupGraph(TeacherGroupGraph graph) {
        List<TimeSlot> colors = timeSlotRepo.findAll();
        return colorizeTeacherGroupGraph(graph, colors);
    }

    private boolean colorizeTeacherGroupGraph(TeacherGroupGraph graph, List<TimeSlot> colors) {

        TeacherGroupGraph.Vertex minDegreeVertex = null;
        // для каждой вершины
        for (TeacherGroupGraph.Vertex vertex : graph.getAdjacencyList()) {
            // если вершина уже раскрашена
            if (vertex != null && vertex.getColors().size() == vertex.getValue().getCountPerWeek()) {
                continue;
            }
            if (minDegreeVertex == null || vertex.getAdjacentCount() < minDegreeVertex.getAdjacentCount()) {
                minDegreeVertex = vertex;
            } else if (vertex.getAdjacentCount().equals(minDegreeVertex.getAdjacentCount())) {
                if (vertex.getAdjacentColorsCount() < minDegreeVertex.getAdjacentColorsCount()) {
                    minDegreeVertex = vertex;
                }
            }
        }
        if (minDegreeVertex == null) {
            System.out.println("Раскрашено!");
            return true;
        }

        for (int i = 0; i < colors.size(); i++) {
            if (minDegreeVertex.getColors().size() == minDegreeVertex.getValue().getCountPerWeek()) {
                break;
            }
            if (!minDegreeVertex.getAdjacentColors().contains(colors.get(i)) && !minDegreeVertex.getColors().contains(colors.get(i))) {
                minDegreeVertex.getColors().add(colors.get(i));
            }
        }
        if (minDegreeVertex.getColors().size() != minDegreeVertex.getValue().getCountPerWeek()) {
            System.out.println("Невозможно раскрасить!");
            return false;
        }

        colorizeTeacherGroupGraph(graph, colors);
        return true;
    }

}
