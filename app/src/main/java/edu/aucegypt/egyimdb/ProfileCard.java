package edu.aucegypt.egyimdb;

import java.io.Serializable;

public class ProfileCard implements Serializable {
    private String MovieName;
    private String Descr;
    private String Duration;
    private String ReleaseDate;
    private String Revenue;
    private String MovieID;
    private String PG;

    public ProfileCard(String MovieID, String MovieName, String Descr, String Duration, String ReleaseDate, String Revenue, String PG) {
        this.MovieName = MovieName;
        this.Descr = Descr;
        this.Duration = Duration;
        this.ReleaseDate = ReleaseDate;
        this.Revenue = Revenue;
        this.PG = PG;
        this.MovieID = MovieID;
    }

    public String getMovieName() {
        return MovieName;
    }

    public String getDescr() {
        return Descr;
    }

    public String getDuration() {
        return Duration;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public String getRevenue() {
        return Revenue;
    }

    public String getPG() {
        return PG;
    }

    public String getMovieID() {
        return MovieID;
    }

}
