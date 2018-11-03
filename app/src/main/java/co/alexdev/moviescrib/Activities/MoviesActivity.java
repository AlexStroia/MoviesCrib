package co.alexdev.moviescrib.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
    private static final String TAG = "MoviesActivity";
    private RecyclerView rv_movies;
    private Spinner sp_sorting;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private List<Movie> mMoviesArray = new ArrayList<>();

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

        getMovies();
    }

    private void setupRecyclerView() {
        mMoviesAdapter = new MoviesAdapter(this, mMoviesArray, this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

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

    private void getMovies() {
        MovieRequest.getPopularMovies(this);
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

    @Override
    public void onMovieListReceivedListener(List<Movie> movieList) {
        mMoviesAdapter.setMovieList(movieList);
    }
}
