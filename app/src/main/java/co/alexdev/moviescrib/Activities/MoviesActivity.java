package co.alexdev.moviescrib.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import co.alexdev.moviescrib.Adapter.MoviesAdapter;
import co.alexdev.moviescrib.Model.Movie;
import co.alexdev.moviescrib.Model.MovieRequest;
import co.alexdev.moviescrib.R;


public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener, AdapterView.OnItemSelectedListener, MovieRequest.MovieListListener {

    private RecyclerView rv_movies;
    private Spinner sp_sorting;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Movie> mMoviesArray = new ArrayList<>();

    private Toast mToast;
    private MoviesAdapter mMoviesAdapter;
    private Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        rv_movies = findViewById(R.id.rv_movies);
        sp_sorting = findViewById(R.id.sp_sorting);

        setupRecyclerView();
        setupSpinner();

        getMostPopularMovies();
    }

    private void setupRecyclerView() {
        mMoviesAdapter = new MoviesAdapter(this, mMoviesArray, this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rv_movies.setLayoutManager(staggeredGridLayoutManager);
        rv_movies.setAdapter(mMoviesAdapter);
    }

    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sorting_styles));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sorting.setAdapter(spinnerAdapter);
        sp_sorting.setOnItemSelectedListener(this);
    }

    private void getMostPopularMovies() {
        MovieRequest.getPopularMovies(this);
    }

    private void getTopRatedMovies() {
        MovieRequest.getTopRatedMovies();
    }

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        movie = mMoviesArray.get(position);
        final String movieKey = getString(R.string.selected_movie_key);
        if (movie != null) {
            intent.putExtra(movieKey, movie);
        }
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final String spinnerSelectedItem = adapterView.getItemAtPosition(i).toString();
        final String[] spinnerItems = getResources().getStringArray(R.array.sorting_styles);

        if (spinnerSelectedItem.equalsIgnoreCase(spinnerItems[0])) {
            getMostPopularMovies();
        } else if (spinnerSelectedItem.equalsIgnoreCase(spinnerItems[1])) {
            getTopRatedMovies();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onMovieListReceivedListener(List<Movie> movieList) {
        if (movieList.size() != 0) {
            mMoviesArray = movieList;
            mMoviesAdapter.setMovieList(mMoviesArray);
        } else {
            mToast = Toast.makeText(this, getString(R.string.no_movies_error), Toast.LENGTH_LONG);
            mToast.show();
        }
    }
}
