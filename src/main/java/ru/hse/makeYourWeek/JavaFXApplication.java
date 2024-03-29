package ru.hse.makeYourWeek;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.hse.makeYourWeek.controllers.MainController;

import java.io.IOException;

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

        MainController mainController = applicationContext.getBean(MainController.class);
        mainController.loadView(stage);

        /*TeacherGroupGraph graph = applicationContext.getBean(TeacherGroupGraph.class);
        graph.build();
        ColorService colorService = applicationContext.getBean(ColorService.class);*/
        //colorService.colorizeTeacherGroupGraph(graph);
        //colorService.printTimeTableToBD(graph);
    }
}
