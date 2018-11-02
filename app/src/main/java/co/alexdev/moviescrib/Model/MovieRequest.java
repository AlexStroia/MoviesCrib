package co.alexdev.moviescrib.Model;

import android.util.Log;

import java.util.List;

import co.alexdev.moviescrib.Utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRequest {

    private static final String TAG = "MovieRequest";
    static List<Movie> movieList;

    static public List<Movie> getPopularMovies() {

        Call<MovieResponse> popularMoviesCall = RetrofitClient.shared().getMovieApi().popularMovieList();
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                Log.d(TAG, "onResponse: " + response.body().getMovieList().toString());
                movieList = response.body().getMovieList();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                return;
            }
        });
        return movieList;
    }

    static public List<Movie> getTopRatedMovies() {

        Call<MovieResponse> topRatedCall = RetrofitClient.shared().getMovieApi().topRatedMovieList();

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                Log.d(TAG, "onResponse: " + response.body().getMovieList().toString());
                movieList = response.body().getMovieList();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                return;
            }
        });
        return movieList;
    }
}
