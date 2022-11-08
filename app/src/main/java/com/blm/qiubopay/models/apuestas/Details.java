package com.blm.qiubopay.models.apuestas;

import java.io.Serializable;

public class Details implements Serializable {
    private String Game;
    private String League;
    private String Description;
    private String CompleteDescription;
    private String VisitorTeam;
    private int VisitorScore;
    private String HomeTeam;
    private int HomeScore;
    private String GameDateTime;
    private float Points;
    private int Odds;
    private WagerResult WagerResult;

    public String getGame() {
        return Game;
    }

    public void setGame(String game) {
        Game = game;
    }

    public String getLeague() {
        return League;
    }

    public void setLeague(String league) {
        League = league;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCompleteDescription() {
        return CompleteDescription;
    }

    public void setCompleteDescription(String completeDescription) {
        CompleteDescription = completeDescription;
    }

    public String getVisitorTeam() {
        return VisitorTeam;
    }

    public void setVisitorTeam(String visitorTeam) {
        VisitorTeam = visitorTeam;
    }

    public int getVisitorScore() {
        return VisitorScore;
    }

    public void setVisitorScore(int visitorScore) {
        VisitorScore = visitorScore;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public int getHomeScore() {
        return HomeScore;
    }

    public void setHomeScore(int homeScore) {
        HomeScore = homeScore;
    }

    public String getGameDateTime() {
        return GameDateTime;
    }

    public void setGameDateTime(String gameDateTime) {
        GameDateTime = gameDateTime;
    }

    public float getPoints() {
        return Points;
    }

    public void setPoints(float points) {
        Points = points;
    }

    public int getOdds() {
        return Odds;
    }

    public void setOdds(int odds) {
        Odds = odds;
    }

    public WagerResult getWagerResult() {
        return WagerResult;
    }

    public void setWagerResult(WagerResult wagerResult) {
        WagerResult = wagerResult;
    }
}

