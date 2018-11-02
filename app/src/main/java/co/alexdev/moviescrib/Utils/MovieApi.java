package co.alexdev.moviescrib.Utils;


import java.util.List;

import co.alexdev.moviescrib.Model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponse> popularMovieList();

    @GET("movie/top_rated")
    Call<MovieResponse> topRatedMovieList();
}
