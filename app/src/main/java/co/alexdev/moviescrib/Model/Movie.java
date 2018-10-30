package co.alexdev.moviescrib.Model;

public final class Movie {
    private final String title;
    private final double vote_average;
    private final String release_date;
    private final String image;
    private final String overview;

    public Movie(String title, double vote_average, String release_date, String image, String overview) {
        this.title = title;
        this.vote_average = vote_average;
        this.release_date = release_date;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public String getOverview() {
        return overview;
    }
}
