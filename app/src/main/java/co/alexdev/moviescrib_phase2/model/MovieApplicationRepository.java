package co.alexdev.moviescrib_phase2.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import co.alexdev.moviescrib_phase2.database.MovieDatabase;
import co.alexdev.moviescrib_phase2.model.response.MovieResponse;
import co.alexdev.moviescrib_phase2.model.response.ReviewsResponse;
import co.alexdev.moviescrib_phase2.model.response.TrailerResponse;
import co.alexdev.moviescrib_phase2.networking.RetrofitClient;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MovieApplicationRepository {

    private static MovieApplicationRepository movieApplicationRepository;
    private static MovieDatabase mDb;
    private static MovieExecutor mMovieExecutor;

    public static MovieApplicationRepository getInstance(Application application) {
        if (movieApplicationRepository == null) {
            movieApplicationRepository = new MovieApplicationRepository();
        }
        mMovieExecutor = MovieExecutor.getInstance();
        mDb = MovieDatabase.getInstance(application);
        return movieApplicationRepository;
    }

    public LiveData<List<Movie>> getPopularMoviesCall() {
        final MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();

        /*Initiate a call by getting the top rates movies and after that pass it to the listener
         * Definied MovieResponse which is the type of the response that we initially get*/
        Call<MovieResponse> popularMoviesCall = RetrofitClient.shared().getMovieApi().popularMovieList();
        popularMoviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Movie> responseMovies = response.body().getMovieList();
                responseMovies = formatListForDatabase(responseMovies, Enums.MovieType.MOST_POPULAR);
                movieList.setValue(responseMovies);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
        return movieList;
    }

    public LiveData<List<Movie>> getTopRatedMoviesCall() {
        final MutableLiveData<List<Movie>> movieList = new MutableLiveData<>();
        /*Initiate a call by getting the top rates movies and after that pass it to the listener
         * Definied MovieResponse which is the type of the response that we initially get*/
        Call<MovieResponse> topRatedCall = RetrofitClient.shared().getMovieApi().topRatedMovieList();

        topRatedCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                List<Movie> responseMovies = response.body().getMovieList();
                responseMovies = formatListForDatabase(responseMovies, Enums.MovieType.TOP_RATED);
                movieList.setValue(responseMovies);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                return;
            }
        });
        return movieList;
    }

    public LiveData<List<Trailer>> getMovieTrailerCall(final int movieTrailerId) {

        final MutableLiveData<List<Trailer>> trailerList = new MutableLiveData<>();
        Call<TrailerResponse> trailerCall = RetrofitClient.shared().getMovieApi().movieTrailers(movieTrailerId);

        trailerCall.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                trailerList.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                return;
            }
        });
        return trailerList;
    }

    /*Call to get the reviews for the current movie*/
    public LiveData<List<Reviews>> getMovieReviewsCall(final int movieId) {
        final MutableLiveData<List<Reviews>> reviewsList = new MutableLiveData<>();

        Call<ReviewsResponse> reviewsCall = RetrofitClient.shared().getMovieApi().movieReviews(movieId);
        reviewsCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                reviewsList.setValue(response.body().getResponse());
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                return;
            }
        });
        return reviewsList;
    }

    /*Database Operations for Movies */
    public LiveData<List<Movie>> getMostPopularMoviesFromDatabase() {
        return mDb.movieDao().getMostPopularMovies();
    }

    public LiveData<List<Movie>> getTopRatedMoviesFromDatabase() {
        return mDb.movieDao().getTopRatedMovies();
    }

    public LiveData<List<Favorite>> getFavoritesMoviesFromDatabase() {
        return mDb.movieDao().getFavoritesMovies();
    }

    public LiveData<Movie> loadMovieById(int id) {
        return mDb.movieDao().loadMovieById(id);
    }

    public void insert(final List<Movie> movieList) {
        mMovieExecutor.getMovieIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insert(movieList);
            }
        });
    }

    public void updateMovie(final Movie movie) {
        mMovieExecutor.getMovieIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().updateMovie(movie);
            }
        });
    }

    /*Database operations for Favorites */
    public void insertToFavorite(final Favorite favorite) {
        mMovieExecutor.getMovieIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertToFavorite(favorite);
            }
        });
    }

    public void deleteFromFavorite(final Favorite favorite) {
        mMovieExecutor.getMovieIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovieFromFavorites(favorite);
            }
        });
    }

    public Favorite loadMovieFromFavoritesById(int id) {
        return mDb.movieDao().loadMovieFromFavoritesById(id);
    }

    private List<Movie> formatListForDatabase(List<Movie> movieList, Enums.MovieType movieType) {
        movieList = MovieUtils.formatMoviesList(movieList, movieType);
        movieList = MovieUtils.syncWithFavorites(mDb, movieList);
        insert(movieList);
        return movieList;
    }
}
