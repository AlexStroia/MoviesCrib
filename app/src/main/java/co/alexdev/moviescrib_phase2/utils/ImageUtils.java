package co.alexdev.moviescrib_phase2.utils;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Movie;

public class ImageUtils {

    //TODO this will be added to the view model later
    public static List<Movie> formatMoviesList(List<Movie> movieList, Enums.MovieType movieType) {
        for (Movie movie : movieList) {
            movie.setMovieType(movieType.toString());
        }
        return movieList;
    }
}
