package co.alexdev.moviescrib_phase2.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Movie;

/*Class used to receive the response from our calls*/
public class MovieResponse {

    @SerializedName("results")
    private List<Movie> movieList;

    public List<Movie> getMovieList() {
        return movieList;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movieList=" + movieList + "\n" +
                '}';
    }
}
