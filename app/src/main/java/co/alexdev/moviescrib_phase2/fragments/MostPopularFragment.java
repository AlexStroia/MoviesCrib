package co.alexdev.moviescrib_phase2.fragments;


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
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MostPopularFragment extends BaseFragment implements MostPopularMoviesAdapter.onMostPopularMovieCLick {

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

    private void showDetailActivity(final int movieId) {
        final Resources resources = getResources();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(resources.getString(R.string.selected_movie_key), movieId);
        startActivity(intent);
    }

    /*If user is connected to the network, fetch all data from the web, else if is not, fetch data from the db
     * If there is no data to show from the database, show a dialog message*/
    private void initView() {
        if (MovieUtils.isNetworkAvailable(getActivity())) {
            getData();
        } else if (canStoreOfflineData) {
            getData();
        } else {
            MovieUtils.showDialog(getActivity(), Enums.DialogType.NO_INTERNET, null);
        }
    }

    private void getData() {
        baseViewModel.getMostPopularMoviesFromDatabase().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                mMovieList = movieList;
                mMostPopularMoviesAdapter.setMovieList(movieList);
            }
        });
    }

    @Override
    public void onMovieClick(int position) {
        final Movie movie = mMovieList.get(position);
        if (movie != null) {
            final int movieId = movie.getId();
            showDetailActivity(movieId);
        }
        Log.d(TAG, "onMovieClick: " + movie.toString());
    }
}
