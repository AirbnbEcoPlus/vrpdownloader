package fr.airbnbecoplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class ExtractManager {
    public void extract(String path, String password, TextArea console) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "7z", "x", path, "-p" + password, "-aoa" };
        Process proc = rt.exec(commands);
        InputStream stdIn = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdIn);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        while ((line = br.readLine()) != null) {
            final String lineToAppend = line;
            Platform.runLater(() -> {
                console.setText(console.getText() + lineToAppend + "\n");
                console.setScrollTop(Double.MAX_VALUE);
            });
        }
    }
    //Pas utile cette merde en plus trop dangereux !
    public void delete(String path) throws IOException{
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "rm", "-R", path };
        Process proc = rt.exec(commands);
    }
}
