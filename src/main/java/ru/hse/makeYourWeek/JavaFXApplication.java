package ru.hse.makeYourWeek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.hse.makeYourWeek.controllers.MainController;
import net.rgielen.fxweaver.core.FxWeaver;
import ru.hse.makeYourWeek.model.GroupsGraph;
import ru.hse.makeYourWeek.model.TeacherGroupGraph;
import ru.hse.makeYourWeek.repository.*;
import ru.hse.makeYourWeek.services.ColorService;

import java.io.IOException;
import java.util.Arrays;

@ConfigurationPropertiesScan("ru.hse.makeYourWeek")
@SpringBootApplication
public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class, args);
    }

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(JavaFXApplication.class)
                .run(args);
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        /*FxWeaver fxWeaver = applicationContext.getBean(FxWeaver.class);

        Parent root = fxWeaver.loadView(MainController.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/

        //new MainController().loadView(stage);
        applicationContext.getBean(MainController.class).loadView(stage);


        TeacherGroupGraph graph = applicationContext.getBean(TeacherGroupGraph.class);
        graph.build();
        ColorService colorService = applicationContext.getBean(ColorService.class);
        //colorService.colorizeTeacherGroupGraph(graph);
        //colorService.printTimeTableToBD(graph);

        for (TeacherGroupGraph.Vertex vertex : graph.getAdjacencyList()) {
            System.out.println(vertex.getValue().toString() + Arrays.toString(vertex.getColors().toArray()));
        }
    }
}
