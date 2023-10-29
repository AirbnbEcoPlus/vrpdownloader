package fr.airbnbecoplus.entity;

public class Game {
    String gameName;
    String releaseName;
    String packageName;
    String versionCode;
    String lastUpdated;
    String size;
    public Game(String gameName, String releaseName, String packageName, String versionCode, String lastUpdated, String size){
        this.gameName = gameName;
        this.releaseName = releaseName;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.lastUpdated = lastUpdated;
        this.size = size;
    }
}
