package co.alexdev.moviescrib_phase2.fragments;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
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
import co.alexdev.moviescrib_phase2.adapter.TopRatedMoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieRequest;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.model.MoviesListener;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.ImageUtils;

public class TopRatedFragment extends BaseFragment implements TopRatedMoviesAdapter.onTopRatedMovieClick, MoviesListener.MovieListListener {

    private static final String TAG = "TopRatedFragment";
    private static final int GRID_COLUMN_SPAN = 2;
    private RecyclerView rv_movies;
    private TopRatedMoviesAdapter mTopRatedMoviesAdapter;
    private GridLayoutManager mGridLayoutManager;
    private List<Movie> mMovieList = new ArrayList<>();
    private boolean hasBeenVisibleOnce = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);

        rv_movies = rootView.findViewById(R.id.rv_top_rated);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mTopRatedMoviesAdapter = new TopRatedMoviesAdapter(getActivity(), mMovieList, this);
        mGridLayoutManager = new GridLayoutManager(getActivity(), GRID_COLUMN_SPAN);
        rv_movies.setAdapter(mTopRatedMoviesAdapter);
        rv_movies.setLayoutManager(mGridLayoutManager);
    }

    /*Get top rated movies*/
    private void getTopRatedMovies() {
        MovieRequest.getTopRatedMovies(this);
    }

    private void showDetailActivity(final Movie movie) {
        final Resources resources = getResources();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(resources.getString(R.string.selected_movie_key), movie);
        startActivity(intent);
    }

    @Override
    public void onMovieClick(int position) {
        final Movie movie = mMovieList.get(position);
        showDetailActivity(movie);
        Log.d(TAG, "onMovieClick: " + movie.toString());
    }

    /*Set da adapter with the populated list*/
    @Override
    public void onMostPopularListReceivedListener(List<Movie> movieList) {
    }

    @Override
    public void onTopRatedListReceivedListener(List<Movie> movieList) {
        List<Movie> formatedList = ImageUtils.formatMoviesList(movieList, Enums.MovieType.TOP_RATED);
        mMovieList = formatedList;
        mTopRatedMoviesAdapter.setMovieList(mMovieList);
    }

    @Override
    public void onTrailerListReceivedListener(List<Trailer> trailerList) {

    }

    @Override
    public void onReviewsListReceivedListener(List<Reviews> reviewsList) {

    }

    /*Load data in the fragment only after it gets visible to the user*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible() && !hasBeenVisibleOnce) {
            getTopRatedMovies();
            hasBeenVisibleOnce = true;
        }
    }
}

