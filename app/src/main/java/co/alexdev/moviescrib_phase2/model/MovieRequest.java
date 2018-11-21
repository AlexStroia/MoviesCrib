package co.alexdev.moviescrib_phase2.model;

import java.util.List;

import co.alexdev.moviescrib_phase2.utils.networking.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*Repository class used to perform Requests*/
public class MovieRequest {

    /*Listener implemented in mainActivity*/
    public interface MovieListListener {
        void onMostPopularListReceivedListener(final List<Movie> movieList);

        void onTopRatedListReceivedListener(final List<Movie> movieList);
    }

    private static MovieListListener mMovieListListener;

    /*Static function that is getting the popular movies*/
    static public void getPopularMovies(MovieListListener movieListListener) {
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
    public static void getTopRatedMovies(MovieListListener movieListListener) {
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
}
