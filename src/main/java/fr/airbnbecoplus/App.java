package fr.airbnbecoplus;

import java.io.IOException;

import fr.airbnbecoplus.entity.PublicConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    DownloadManager downloadManager = new DownloadManager();
    ExtractManager extractManager = new ExtractManager();
    PublicConfig publicConfig;
    @Override
    public void start(Stage stage) throws Exception {
        // Download Info
        publicConfig = downloadManager.downloadCredentials();
        downloadManager.downloadManifest();
         


        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));

        var scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
