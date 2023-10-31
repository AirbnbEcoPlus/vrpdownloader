package fr.airbnbecoplus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.airbnbecoplus.entity.Game;
import fr.airbnbecoplus.entity.PublicConfig;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    DownloadManager downloadManager = new DownloadManager();
    ExtractManager extractManager = new ExtractManager();
    PublicConfig publicConfig;
    final String password = "gL59VfgPxoHR";


    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();
        ListView<String> listView = new ListView<String>();
        ToolBar topBar = new ToolBar();
        Button downloadButton = new Button();

        TextArea terminalField = new TextArea();
        terminalField.setMaxHeight(200000);
        terminalField.setMaxWidth(150);
        terminalField.setEditable(false);
        terminalField.setWrapText(true);

        VBox gamesDescription = new VBox();
        ImageView gameIcon = new ImageView();
        Label gameDesc = new Label();
        gamesDescription.getChildren().addAll(gameIcon, gameDesc);

        downloadButton.setText("Download");
        borderPane.setCenter(listView);
        borderPane.setTop(topBar);
        borderPane.setBottom(downloadButton);
        borderPane.setRight(gamesDescription);
        borderPane.setLeft(terminalField);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
           @Override
           public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
               
           }
        });



        stage.setTitle("VrpDownloader");
        stage.setScene(new Scene(borderPane, 600, 400));
        stage.show();

        // Download Info
        final Service<Void> downloadManifestService = new Service<Void>() {

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        downloadManager.downloadManifest(terminalField);
                        extractManager.extract("./meta.7z", password, terminalField);
                        addGamesToListView(listView);
                        return null;
                    }
                };
            }
        };
        downloadManifestService.start();
    }

    public void loadGameInformation(ImageView gameIcon, Label gameDesc, String name){
        
    }

    public void addGamesToListView(ListView listView){
       String fileName = "VRP-GameList.txt"; // Remplacez "votre_fichier.txt" par le chemin réel de votre fichier.

        List<Game> games = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName)); 
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if(firstLine){
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length == 6) {
                    String gameName = parts[0].trim();
                    String releaseName = parts[1].trim();
                    String packageName = parts[2].trim();
                    String versionCode = parts[3].trim();
                    String lastUpdated = parts[4].trim();
                    String size = parts[5].trim();
                    Game game = new Game(gameName, releaseName, packageName, versionCode, lastUpdated, size);
                    games.add(game);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Game game : games){
            listView.getItems().add(game.gameName);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}
