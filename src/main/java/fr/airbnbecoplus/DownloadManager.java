package fr.airbnbecoplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

import fr.airbnbecoplus.entity.PublicConfig;
import javafx.scene.control.TextArea;

public class DownloadManager {

    final String credentialsUrl = "https://wiki.vrpirates.club/downloads/vrp-public.json";

    final String baseUri = "https://skrazzle.glomtom.cyou/";
    final String password = "gL59VfgPxoHR";

    // Pas fini de toute facon pratiquement inutile flemme de finir
    public PublicConfig downloadCredentials(TextArea console) {
        console.setText(console.getText() + "Download Credentials from VRPirates Servers" + "\n");
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new URL(credentialsUrl).openStream());
        } catch (Exception e) {
            System.out.println(e);
        }
        Gson gson = new Gson();

        return gson.fromJson(reader, PublicConfig.class);
    }

    public void downloadManifest(TextArea console) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "rclone", "copy", "--http-url", baseUri, ":http:/meta.7z", "./", "--progress" };
        Process proc = rt.exec(commands);
        InputStream stdIn = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdIn);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        while ((line = br.readLine()) != null)
            console.setText(console.getText() + line + "\n");
            console.setScrollTop(Double.MIN_VALUE);
    }

    public void downloadGame(String fullName, TextArea console) {

    }

}
