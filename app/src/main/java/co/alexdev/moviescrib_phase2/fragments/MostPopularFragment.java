package co.alexdev.moviescrib_phase2.fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.activities.DetailActivity;
import co.alexdev.moviescrib_phase2.adapter.MostPopularMoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieRequest;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.model.MoviesListener;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostPopularFragment extends BaseFragment implements MostPopularMoviesAdapter.onMostPopularMovieCLick, MoviesListener.MovieListListener {

    private static final String TAG = "MostPopularFragment";
    private static final int GRID_COLUMN_SPAN = 2;
    private RecyclerView rv_movies;
    private MostPopularMoviesAdapter mMostPopularMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<Movie> mMovieList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        rv_movies = rootView.findViewById(R.id.rv_movies);

        setupRecyclerView();

        initView();

        return rootView;
    }

    private void setupRecyclerView() {
        mMostPopularMoviesAdapter = new MostPopularMoviesAdapter(getActivity(), mMovieList, this);
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_SPAN);
        rv_movies.setAdapter(mMostPopularMoviesAdapter);
        rv_movies.setLayoutManager(mGridLayoutManager);
    }

    /*Get the popular movies*/
    private void getMostPopularMovies() {
        MovieRequest.getPopularMovies(this);
    }

    @Override
    public void onMovieClick(int position) {
        final Movie movie = mMovieList.get(position);
        if (movie != null) {
            showDetailActivity(movie);
        }
        Log.d(TAG, "onMovieClick: " + movie.toString());
    }

    private void showDetailActivity(final Movie movie) {
        final Resources resources = getResources();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(resources.getString(R.string.selected_movie_key), movie);
        startActivity(intent);
    }

    /*If user is connected to the network, fetch all data from the web, else if is not, fetch data from the db
     * If there is no data to show from the database, show a dialog message*/
    private void initView() {
        final LiveData<List<Movie>> mostPopularMoviesList = mDb.movieDao().getMostPopularMovies();
        if (MovieUtils.isNetworkAvailable(getActivity())) {
            getMostPopularMovies();
        } else if (canStoreOfflineData) {
            mostPopularMoviesList.observe(this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movieList) {
                    if (movieList != null && movieList.size() > 0) {
                        mMovieList = movieList;
                        mMostPopularMoviesAdapter.setMovieList(movieList);
                    }
                }
            });
        } else {
            MovieUtils.showDialog(getActivity(), Enums.DialogType.NO_INTERNET, null);
        }
    }

    private void getMostPopularMoviesFromDatabase() {
        mDb.movieDao().getMostPopularMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                mMovieList = movieList;
                mMostPopularMoviesAdapter.setMovieList(movieList);
            }
        });

    }

    private List<Movie> syncMoviesWithFavorites(final List<Movie> formatedList) {
        MovieUtils.syncWithFavorites(mDb, getActivity(), formatedList);
        return formatedList;
    }

    /*Set da adapter with the populated list*/
    @Override
    public void onMostPopularListReceivedListener(List<Movie> movieList) {
        List<Movie> formatedList = MovieUtils.formatMoviesList(movieList, Enums.MovieType.MOST_POPULAR);
        /*When data is received from the server, insert it into the database*/
        mDb.movieDao().insert(formatedList);

        syncMoviesWithFavorites(formatedList);
        getMostPopularMoviesFromDatabase();
    }

    @Override
    public void onTopRatedListReceivedListener(List<Movie> movieList) {

    }

    @Override
    public void onTrailerListReceivedListener(List<Trailer> trailerList) {

    }

    @Override
    public void onReviewsListReceivedListener(List<Reviews> reviewsList) {

    }
}
