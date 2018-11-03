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

    private static final String TAG = "MovieRequest";
    private static MovieListListener mMovieListListener;

    static public void getPopularMovies(MovieListListener movieListListener) {
        mMovieListListener = movieListListener;

        Call<MovieResponse> popularMoviesCall = RetrofitClient.shared().getMovieApi().popularMovieList();
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                mMovieListListener.onMovieListReceivedListener(response.body().getMovieList());
                Log.d(TAG, "onResponse: " + response.body().getMovieList().toString());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                return;
            }
        });
    }

    static void getTopRatedMovies() {

        Call<MovieResponse> topRatedCall = RetrofitClient.shared().getMovieApi().topRatedMovieList();

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                mMovieListListener.onMovieListReceivedListener(response.body().getMovieList());
                Log.d(TAG, "onResponse: " + response.body().getMovieList().toString());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                return;
            }
        });
    }
}
