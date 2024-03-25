package ru.hse.makeYourWeek.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.dao.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.dao.TimeSlot;
import ru.hse.makeYourWeek.dao.TimeTable;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.TimeSlotRepo;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ColorService {
    private final TimeSlotRepo timeSlotRepo;
    private final TimeTableRepo timeTableRepo;

    @Autowired
    public ColorService(TimeSlotRepo timeSlotRepo, TimeTableRepo timeTableRepo) {
        this.timeSlotRepo = timeSlotRepo;
        this.timeTableRepo = timeTableRepo;
    }

    public void colorizeTeacherGroupGraph(TeacherGroupGraph graph) {
        List<TimeSlot> colors = timeSlotRepo.findAll();
        colorizeTeacherGroupGraph(graph, colors);
    }
    private void colorizeTeacherGroupGraph(TeacherGroupGraph graph, List<TimeSlot> colors) {

        TeacherGroupGraph.Vertex minDegreeVertex = null;
        for (TeacherGroupGraph.Vertex vertex : graph.getAdjacencyList()) {
            if (minDegreeVertex != null && minDegreeVertex.getColors().size() == minDegreeVertex.getValue().getCountPerWeek()) {
                continue;
            }
            if (minDegreeVertex == null || vertex.getAdjacentCount() < minDegreeVertex.getAdjacentCount()) {
                minDegreeVertex = vertex;
            }
            else if (vertex.getAdjacentCount().equals(minDegreeVertex.getAdjacentCount())) {
                if (vertex.getAdjacentColorsCount() < minDegreeVertex.getAdjacentColorsCount()) {
                    minDegreeVertex = vertex;
                }
            }
        }
        if (minDegreeVertex == null) {
            return;
        }
        Integer necessaryColorsCount = minDegreeVertex.getValue().getCountPerWeek();
        while (necessaryColorsCount != 0) {
            for (int i = 0; i < colors.size(); i++) {
                if (!minDegreeVertex.getAdjacentColors().contains(colors.get(i)) && !minDegreeVertex.getColors().contains(colors.get(i))) {
                    minDegreeVertex.getColors().add(colors.get(i));
                    necessaryColorsCount--;
                }
            }
        }
        colorizeTeacherGroupGraph(graph, colors);
    }

    public void printTimeTableToBD(TeacherGroupGraph graph) {
        for (TeacherGroupGraph.Vertex vertex : graph.getAdjacencyList()) {
            for (TimeSlot color : vertex.getColors()) {
                timeTableRepo.save(new TimeTable(vertex.getId(), color.getId()));
            }
        }
    }
}
