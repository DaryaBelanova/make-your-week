package ru.hse.makeYourWeek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.hse.makeYourWeek.entities.*;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.services.*;
import ru.hse.makeYourWeek.util.ApplicationContextHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@FxmlView("timeTable.fxml")
public class TimeTableController {
    public VBox mon1;
    public VBox tue1;
    public VBox wed1;
    public VBox thu1;
    public VBox fri1;

    public VBox mon2;
    public VBox tue2;
    public VBox wed2;
    public VBox thu2;
    public VBox fri2;

    public VBox mon3;
    public VBox tue3;
    public VBox wed3;
    public VBox thu3;
    public VBox fri3;

    public VBox mon4;
    public VBox tue4;
    public VBox wed4;
    public VBox thu4;
    public VBox fri4;

    public VBox mon5;
    public VBox tue5;
    public VBox wed5;
    public VBox thu5;
    public VBox fri5;

    public VBox mon6;
    public VBox tue6;
    public VBox wed6;
    public VBox thu6;
    public VBox fri6;

    public VBox mon7;
    public VBox tue7;
    public VBox wed7;
    public VBox thu7;
    public VBox fri7;


    @Autowired
    private ColorService colorService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherGroupAdjacencyService teacherGroupAdjacencyService;
    @Autowired
    private TimeSlotService timeSlotService;
    @Autowired
    private TimeTableService timeTableService;


    public Button saveTimeTableFileButton;
    public Button mainButton;
    public Button teachersButton;
    public Button groupsButton;
    public Button generateButton;


    public void onActionMainButtonClick(ActionEvent event) throws IOException {
        changeTab(mainButton, "main.fxml");
    }

    public void onActionTeachersButtonClick(ActionEvent event) throws IOException {
        changeTab(teachersButton, "teachersToDisplay.fxml");
    }

    public void onActionGroupsButtonClick(ActionEvent event) throws IOException {
        changeTab(groupsButton, "groupsToDisplay.fxml");
    }

    public void onActionGenerateButtonClick(ActionEvent event) {
        TeacherGroupGraph teacherGroupGraph = ApplicationContextHolder.getApplicationContext().getBean(TeacherGroupGraph.class);
        teacherGroupGraph.build();

        boolean isValid = colorService.colorizeTeacherGroupGraph(teacherGroupGraph);
        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Некорректные данные");
            alert.setHeaderText(null);
            alert.setContentText("Невозможно сгенерировать корректное расписание.\nЗагружены изначально конфликтующие данные.");
            alert.showAndWait();
            return;
        }
        saveNewTimeTableToDB(teacherGroupGraph);

        displayTimeTable();
    }

    @FXML
    private void initialize() {
        displayTimeTable();
    }

    private void clearTimeTableView() {
        for (TimeSlot timeSlot : timeSlotService.getAll()) {
            VBox slot = getVBoxByTimeSlot(timeSlot);
            slot.getChildren().clear();
        }
    }

    private void displayTimeTable() {
        clearTimeTableView();

        List<TimeTableRecord> allRecords = timeTableService.getAll();
        for (TimeTableRecord record : allRecords) {
            TeacherGroupAdjacency teacherGroupAdjacency = teacherGroupAdjacencyService.getById(record.getTeacherGroupId());
            Teacher teacher = teacherService.getById(teacherGroupAdjacency.getTeacherId());
            Group group = groupService.getById(teacherGroupAdjacency.getGroupId());
            TimeSlot timeSlot = timeSlotService.getById(record.getTimeSlotId());

            VBox slot = getVBoxByTimeSlot(timeSlot);
            Text text = new Text(teacher.getName() + " - " + group.getName());
            slot.getChildren().add(text);
        }
    }

    private void saveNewTimeTableToDB(TeacherGroupGraph teacherGroupGraph) {
        List<TimeTableRecord> saved = timeTableService.deleteAndSaveNew(teacherGroupGraph);
        displayTimeTable();
    }

    private void changeTab(Button onClick, String fxmlFileName) throws IOException {
        //Close current
        Stage stage = (Stage) onClick.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFileName));
        fxmlLoader.setControllerFactory(ApplicationContextHolder.getApplicationContext()::getBean);
        Parent root = (Parent) fxmlLoader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("ПоНедельник");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void onActionSaveTimeTableFileButtonClick(ActionEvent event) {
        List<TimeTableRecord> allRecords = timeTableService.getAll();

        if (allRecords.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Нет данных в базе");
            alert.setHeaderText(null);
            alert.setContentText("Расписание еще не сгенерировано. Нет данных для выгрузки.");
            alert.showAndWait();
            return;
        }

        List<List<List<String>>> toWrite = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            List<List<String>> columns = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                columns.add(new ArrayList<>());
            }
            toWrite.add(columns);
        }
        for (TimeTableRecord timeTableRecord : allRecords) {
            TeacherGroupAdjacency teacherGroupAdjacency = teacherGroupAdjacencyService.getById(timeTableRecord.getTeacherGroupId());
            Teacher teacher = teacherService.getById(teacherGroupAdjacency.getTeacherId());
            Group group = groupService.getById(teacherGroupAdjacency.getGroupId());
            TimeSlot timeSlot = timeSlotService.getById(timeTableRecord.getTimeSlotId());

            String toAdd = teacher.getName() + " - " + group.getName();
            switch (timeSlot.getInDay()) {
                case "Понедельник":
                    toWrite.get(timeSlot.getLessonNumber()).get(1).add(toAdd);
                    break;
                case "Вторник":
                    toWrite.get(timeSlot.getLessonNumber()).get(2). add(toAdd);
                    break;
                case "Среда":
                    toWrite.get(timeSlot.getLessonNumber()).get(3).add(toAdd);
                    break;
                case "Четверг":
                    toWrite.get(timeSlot.getLessonNumber()).get(4).add(toAdd);
                    break;
                case "Пятница":
                    toWrite.get(timeSlot.getLessonNumber()).get(5).add(toAdd);
                    break;
            }
        }


        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Расписание");
            sheet.setColumnWidth(0, 15 * 512); // Ширина столбца в символах, умноженная на 256
            sheet.setColumnWidth(1, 15 * 512); // Ширина столбца в символах, умноженная на 256
            sheet.setColumnWidth(2, 15 * 512); // Ширина столбца в символах, умноженная на 256
            sheet.setColumnWidth(3, 15 * 512); // Ширина столбца в символах, умноженная на 256
            sheet.setColumnWidth(4, 15 * 512); // Ширина столбца в символах, умноженная на 256
            sheet.setColumnWidth(5, 15 * 512); // Ширина столбца в символах, умноженная на 256

            // Создаем стиль для жирного текста
            CellStyle boldStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            boldStyle.setFont(font);

            // Заполнение названий столбцов
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("");

            Cell cell1 = headerRow.createCell(1);
            cell1.setCellValue("ПН");
            cell1.setCellStyle(boldStyle);

            Cell cell2 = headerRow.createCell(2);
            cell2.setCellValue("ВТ");
            cell2.setCellStyle(boldStyle);
            Cell cell3 = headerRow.createCell(3);
            cell3.setCellValue("СР");
            cell3.setCellStyle(boldStyle);

            Cell cell4 = headerRow.createCell(4);
            cell4.setCellValue("ЧТ");
            cell4.setCellStyle(boldStyle);

            Cell cell5 = headerRow.createCell(5);
            cell5.setCellValue("ПТ");
            cell5.setCellStyle(boldStyle);

            // Заполнение данных
            for (int i = 1; i <= 7; i++) {
                Row row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(i + " урок");
                cell.setCellStyle(boldStyle);
                row.setHeightInPoints(250);
                for (int j = 1; j <= 5; j++) {
                    StringBuilder res = new StringBuilder();
                    for (String record : toWrite.get(i).get(j)) {
                        res.append(record).append("\n");
                    }
                    row.createCell(j).setCellValue(res.toString());
                }
            }

            // Сохранение в файл
            String fileName = "timetable.xlsx";
            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.'));

            File file = new File(fileName);
            int index = 1;
            while (file.exists()) {
                fileName = baseName + "(" + index + ")" + extension;
                file = new File(fileName);
                index++;
            }
            try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                workbook.write(fileOut);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Успех!");
                alert.setHeaderText(null);
                alert.setContentText("Файл с расписанием успешно сохранен.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Не удалось сохранить файл.");
            alert.showAndWait();
        }
    }

    private VBox getVBoxByTimeSlot(TimeSlot timeSlot) {
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 1) {
            return mon1;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 2) {
            return mon2;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 3) {
            return mon3;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 4) {
            return mon4;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 5) {
            return mon5;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 6) {
            return mon6;
        }
        if (timeSlot.getInDay().equals("Понедельник") && timeSlot.getLessonNumber() == 7) {
            return mon7;
        }

        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 1) {
            return tue1;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 2) {
            return tue2;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 3) {
            return tue3;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 4) {
            return tue4;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 5) {
            return tue5;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 6) {
            return tue6;
        }
        if (timeSlot.getInDay().equals("Вторник") && timeSlot.getLessonNumber() == 7) {
            return tue7;
        }

        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 1) {
            return wed1;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 2) {
            return wed2;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 3) {
            return wed3;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 4) {
            return wed4;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 5) {
            return wed5;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 6) {
            return wed6;
        }
        if (timeSlot.getInDay().equals("Среда") && timeSlot.getLessonNumber() == 7) {
            return wed7;
        }

        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 1) {
            return thu1;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 2) {
            return thu2;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 3) {
            return thu3;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 4) {
            return thu4;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 5) {
            return thu5;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 6) {
            return thu6;
        }
        if (timeSlot.getInDay().equals("Четверг") && timeSlot.getLessonNumber() == 7) {
            return thu7;
        }

        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 1) {
            return fri1;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 2) {
            return fri2;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 3) {
            return fri3;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 4) {
            return fri4;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 5) {
            return fri5;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 6) {
            return fri6;
        }
        if (timeSlot.getInDay().equals("Пятница") && timeSlot.getLessonNumber() == 7) {
            return fri7;
        }
        return null;
    }
}
