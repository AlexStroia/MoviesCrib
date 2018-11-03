package co.alexdev.moviescrib.Model;

import android.util.Log;

import java.util.List;

import co.alexdev.moviescrib.Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRequest {

    public interface MovieListListener {
        void onMovieListReceivedListener(final List<Movie> movieList);
    }

    private static MovieListListener mMovieListListener;

    static public void getPopularMovies(MovieListListener movieListListener) {
        mMovieListListener = movieListListener;

        Call<MovieResponse> popularMoviesCall = RetrofitClient.shared().getMovieApi().popularMovieList();
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                mMovieListListener.onMovieListReceivedListener(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
    }

    public static void getTopRatedMovies() {

        Call<MovieResponse> topRatedCall = RetrofitClient.shared().getMovieApi().topRatedMovieList();

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                mMovieListListener.onMovieListReceivedListener(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
    }
}
