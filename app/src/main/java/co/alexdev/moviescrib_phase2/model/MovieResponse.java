package co.alexdev.moviescrib_phase2.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/*Class used to receive the response from our calls*/
public class MovieResponse {

    @SerializedName("results")
    private List<Movie> movieList;

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movieList=" + movieList + "\n"+
                '}';
    }
}
