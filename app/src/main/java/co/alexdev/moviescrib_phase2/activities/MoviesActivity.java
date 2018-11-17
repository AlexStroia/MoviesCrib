package co.alexdev.moviescrib_phase2.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.adapter.MoviesAdapter;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieRequest;
import co.alexdev.moviescrib_phase2.R;

/*Main activity where we can see a list with all the movies*/
public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener, AdapterView.OnItemSelectedListener, MovieRequest.MovieListListener {

    private RecyclerView rv_movies;
    private Spinner sp_sorting;
    private GridLayoutManager gridLayoutManager;
    private List<Movie> mMoviesList = new ArrayList<>();

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

    /*Set recyclerView with the adapter and with the data that came from the API fetched into the array*/
    private void setupRecyclerView() {
        mMoviesAdapter = new MoviesAdapter(this, mMoviesList, this);
        gridLayoutManager = new GridLayoutManager(this, 2);
        rv_movies.setLayoutManager(gridLayoutManager);
        rv_movies.setAdapter(mMoviesAdapter);
    }

    /*Adapter that the spinner will use, his format, and the resources that the spinner will have*/
    private void setupSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.sorting_styles));
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sorting.setAdapter(spinnerAdapter);
        sp_sorting.setOnItemSelectedListener(this);
    }

    /*Get the popular movies*/
    private void getMostPopularMovies() {
        MovieRequest.getPopularMovies(this);
    }

    /*Get top rated movies*/
    private void getTopRatedMovies() {
        MovieRequest.getTopRatedMovies();
    }

    /*When a movie is clicked make an itent to populate the detail activity with the movie informations*/
    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        movie = mMoviesList.get(position);
        final String movieKey = getString(R.string.selected_movie_key);
        if (movie != null) {
            intent.putExtra(movieKey, movie);
        }
        startActivity(intent);
    }

    /*Get the Spinner selected item from the SpinnerAdapter*
      Compare the SelectedItem with the item that is in the spinnerArray
      If they are equal, perform the specific operation
     */
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

    /*When movie list is received, set the mMoviesList with the received list
     * Set the adapter to load the data
     * If there is no data, show a toast
     */
    @Override
    public void onMovieListReceivedListener(List<Movie> movieList) {
        if (movieList.size() != 0) {
            mMoviesList = movieList;
            mMoviesAdapter.setMovieList(mMoviesList);
        } else {
            mToast = Toast.makeText(this, getString(R.string.no_movies_error), Toast.LENGTH_LONG);
            mToast.show();
        }
    }
}
