package fr.airbnbecoplus;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.control.TextArea;

public class ExtractManager {
    public void extract(String path, String password, TextArea console) throws IOException{
        if(!new File(".meta").exists()){
        Runtime rt = Runtime.getRuntime();
        String[] commands = {"7z", "x", path, "-p" + password};
        Process proc = rt.exec(commands);
        InputStream stdIn = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdIn);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        while ((line = br.readLine()) != null)
            console.setText(console.getText() + line + "\n");
            console.setScrollTop(Double.MIN_VALUE);
        }
    }
}
