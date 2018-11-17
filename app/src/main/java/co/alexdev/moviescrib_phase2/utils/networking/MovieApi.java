package co.alexdev.moviescrib_phase2.utils.networking;

import co.alexdev.moviescrib_phase2.model.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;

/*Endpoints used when we are querying*/
/*GET means that we want to get some data*/
public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponse> popularMovieList();

    @GET("movie/top_rated")
    Call<MovieResponse> topRatedMovieList();
}
