package co.alexdev.moviescrib_phase2.model;

import android.util.Log;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.response.MovieResponse;
import co.alexdev.moviescrib_phase2.model.response.ReviewsResponse;
import co.alexdev.moviescrib_phase2.model.response.TrailerResponse;
import co.alexdev.moviescrib_phase2.networking.RetrofitClient;
import co.alexdev.moviescrib_phase2.utils.Enums;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Repository class used to perform Requests*/
public class MovieRequest {
    private static final String TAG = "MovieRequest";

    private static MoviesListener.MovieListListener mMovieListListener;

    /*Static function that is getting the popular movies*/
    static public void getPopularMovies(MoviesListener.MovieListListener movieListListener) {
        mMovieListListener = movieListListener;

        /*Initiate a call by getting the top rates movies and after that pass it to the listener
         * Definied MovieResponse which is the type of the response that we initially get*/
        Call<MovieResponse> popularMoviesCall = RetrofitClient.shared().getMovieApi().popularMovieList();
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                mMovieListListener.onMostPopularListReceivedListener(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
    }

    /*Static function that is getting the top rated movies movies*/
    public static void getTopRatedMovies(MoviesListener.MovieListListener movieListListener) {
        mMovieListListener = movieListListener;
        /*Initiate a call by getting the top rates movies and after that pass it to the listener
         * Definied MovieResponse which is the type of the response that we initially get*/
        Call<MovieResponse> topRatedCall = RetrofitClient.shared().getMovieApi().topRatedMovieList();

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                mMovieListListener.onTopRatedListReceivedListener(response.body().getMovieList());
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
    }

    public static void getMovieTrailer(final MoviesListener.MovieListListener movieListListener, final int movieTrailerId) {
        mMovieListListener = movieListListener;
        Call<TrailerResponse> trailerCall = RetrofitClient.shared().getMovieApi().movieTrailers(movieTrailerId);

        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                movieListListener.onTrailerListReceivedListener(response.body().getResponse());
                Log.d(TAG, "onResponse: " + response.body().getResponse().toString());

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

            }
        });
    }

    /*Call to get the reviews for the current movie*/
    public static void getMovieReviews(final MoviesListener.MovieListListener movieListListener, final int movieId) {
        mMovieListListener = movieListListener;

        Call<ReviewsResponse> reviewsCall = RetrofitClient.shared().getMovieApi().movieReviews(movieId);
        reviewsCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                mMovieListListener.onReviewsListReceivedListener(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {

            }
        });
    }
}
