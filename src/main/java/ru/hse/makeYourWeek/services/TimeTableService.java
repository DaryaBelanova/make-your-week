package ru.hse.makeYourWeek.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hse.makeYourWeek.entities.TeacherGroupAdjacency;
import ru.hse.makeYourWeek.entities.TimeSlot;
import ru.hse.makeYourWeek.entities.TimeTableRecord;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.TimeTableRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeTableService {
    @Autowired
    private TimeTableRepo timeTableRepo;

    public List<TimeTableRecord> deleteAndSaveNew(TeacherGroupGraph teacherGroupGraph) {
        timeTableRepo.deleteAll();
        List<TimeTableRecord> allTimeTableRecords = new ArrayList<>();
        int id = 1;
        for (int i = 0; i < teacherGroupGraph.getVerticesCount(); i++) {
            TeacherGroupGraph.Vertex vertex = teacherGroupGraph.getAdjacencyList().get(i);
            TeacherGroupAdjacency teacherGroupAdjacency = vertex.getValue();
            List<TimeSlot> vertexColors = new ArrayList<>(vertex.getColors());
            for (int j = 0; j < vertexColors.size(); j++) {
                allTimeTableRecords.add(new TimeTableRecord(id, teacherGroupAdjacency.getId(), vertexColors.get(j).getId()));
                id++;
            }
        }
        return timeTableRepo.saveAll(allTimeTableRecords);
    }

    public List<TimeTableRecord> getAll() {
        return timeTableRepo.findAll();
    }

    public long getRecordsCount() {
        return timeTableRepo.count();
    }
}
