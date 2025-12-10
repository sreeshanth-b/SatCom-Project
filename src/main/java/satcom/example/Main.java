package satcom.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import satcom.ui.GlobeView;
import satcom.utils.OrekitInitializer;
import satcom.utils.TLELoader;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        OrekitInitializer.init();

        BorderPane root = new BorderPane();
        GlobeView map = new GlobeView(1000, 600);

        Button btn = new Button("Load Satellites");
        btn.setOnAction(e -> {
            String path = "/home/shreeshanth/Documents/satcom-app/satcomapp/data/Active.txt";

            var satellites = TLELoader.loadFromFile(path);
            satellites.stream().limit(200).forEach(tle -> map.plotSatellite(tle));  // show 200

            btn.setText("Loaded " + satellites.size() + " satellites!");
        });

        root.setTop(btn);
        root.setCenter(map);

        stage.setScene(new Scene(root, 1000, 600));
        stage.setTitle("SATCOM 2D Tracker");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
