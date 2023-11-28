package fr.airbnbecoplus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import fr.airbnbecoplus.entity.PublicConfig;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class DownloadManager {

    final String credentialsUrl = "https://wiki.vrpirates.club/downloads/vrp-public.json";

    PublicConfig publicConfig; 
    
    public PublicConfig getCredentials() {
       return publicConfig; 
    }

    // Pas fini de toute facon pratiquement inutile flemme de finir
    public void downloadCredentials(TextArea console) {
        console.setText(console.getText() + "Download Credentials from VRPirates Servers" + "\n");
        HttpClient httpClient = null;
        try{
        httpClient = HttpClients.custom()
            .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustAllStrategy())
                    .build()
                )
            ).build();
        }catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e){
            System.out.println(e);
        }
        HttpGet httpGet = new HttpGet(credentialsUrl);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line);
                    }

                    // Utilisation de Gson pour analyser le contenu JSON
                    Gson gson = new Gson();
                    // Remplacez MyClass.class par la classe que vous attendez en tant que résultat
                    PublicConfig config = gson.fromJson(content.toString(), PublicConfig.class);

                    // Faites quelque chose avec l'objet obtenu 
                    publicConfig = config;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void downloadManifest(TextArea console) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "rclone", "copy", "--http-url", publicConfig.baseUri, ":http:/meta.7z", "./vrpdata/", "--progress" };
        Process proc = rt.exec(commands);
        InputStream stdIn = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdIn);
        BufferedReader br = new BufferedReader(isr);

        Deque<String> recentLines = new ArrayDeque<>(5);

        String line = null;
        while ((line = br.readLine()) != null) {
            final String lineToAppend = line;
            Platform.runLater(() -> {
                recentLines.add(lineToAppend);
                if (recentLines.size() > 5) {
                    recentLines.removeFirst();
                }
                console.setText(String.join("\n", recentLines) + "\n");
                console.setScrollTop(Double.MAX_VALUE);
            });
        }
    }

    public void downloadGame(String crypted, TextArea console) throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] commands = { "rclone", "copy", "--http-url", publicConfig.baseUri, ":http:/" + crypted + "/", "./vrpdata/",
                "--progress" };
        Process proc = rt.exec(commands);
        InputStream stdIn = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdIn);
        BufferedReader br = new BufferedReader(isr);
        Deque<String> recentLines = new ArrayDeque<>(5);

        String line = null;
        while ((line = br.readLine()) != null) {
            final String lineToAppend = line;
            Platform.runLater(() -> {
                recentLines.add(lineToAppend);
                if (recentLines.size() > 5) {
                    recentLines.removeFirst();
                }
                console.setText(String.join("\n", recentLines) + "\n");
                console.setScrollTop(Double.MAX_VALUE);
            });
        }
    }

}
