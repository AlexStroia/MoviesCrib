package co.alexdev.moviescrib.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import co.alexdev.moviescrib.Adapter.MoviesAdapter;
import co.alexdev.moviescrib.Model.Movie;
import co.alexdev.moviescrib.Model.MovieRequest;
import co.alexdev.moviescrib.R;


public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "MoviesActivity";
    private RecyclerView rv_movies;
    private Spinner sp_sorting;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> mMoviesArray = new ArrayList<>();

    private Toast mToast;
    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        rv_movies = findViewById(R.id.rv_movies);
        sp_sorting = findViewById(R.id.sp_sorting);

        setupRecyclerView();
        setupSpinner();

        MovieRequest.getPopularMovies();
    }

    private void setupRecyclerView() {
        Movie movie = new Movie("Avengers", 5.6, "11.05.1995", "www.google.ro", "Awesome movie");
        mMoviesArray.add(movie);
        mMoviesArray.add(movie);
        mMoviesArray.add(movie);
        mMoviesArray.add(movie);
        mMoviesArray.add(movie);
        mMoviesArray.add(movie);

        mMoviesAdapter = new MoviesAdapter(mMoviesArray, this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        rv_movies.setLayoutManager(gridLayoutManager);
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


    @Override
    public void onMovieClick(int position) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, "Position: " + position, Toast.LENGTH_LONG);
        mToast.show();

        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        final Object position = adapterView.getItemAtPosition(i);
        Log.d(TAG, "onItemSelected: position: " + position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
