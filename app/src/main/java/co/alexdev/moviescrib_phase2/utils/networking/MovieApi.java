package co.alexdev.moviescrib.utils.networking;

import co.alexdev.moviescrib.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponse> popularMovieList();

    @GET("movie/top_rated")
    Call<MovieResponse> topRatedMovieList();
}
