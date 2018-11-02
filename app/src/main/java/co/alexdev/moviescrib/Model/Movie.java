package co.alexdev.moviescrib.Model;

import com.google.gson.annotations.SerializedName;

public final class Movie {

    @SerializedName("title")
    private final String title;
    @SerializedName("vote_average")
    private final double vote_average;
    @SerializedName("release_date")
    private final String release_date;
    @SerializedName("poster_path")
    private final String poster_path;
    @SerializedName("overview")
    private final String overview;

    public Movie(String title, double vote_average, String release_date, String poster_path, String overview) {
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", vote_average=" + vote_average +
                ", release_date='" + release_date + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                '}';
    }
}
