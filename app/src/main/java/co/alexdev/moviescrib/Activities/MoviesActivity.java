package co.alexdev.moviescrib.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import co.alexdev.moviescrib.Adapter.MoviesAdapter;
import co.alexdev.moviescrib.Model.Movie;
import co.alexdev.moviescrib.R;

public class MoviesActivity extends AppCompatActivity implements MoviesAdapter.onMovieClickListener {

    private RecyclerView rv_movies;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<Movie> mMoviesArray = new ArrayList<>();

    private Toast mToast;
    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        rv_movies = findViewById(R.id.rv_movies);

        setupRecyclerView();
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


    @Override
    public void onMovieClick(int position) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, "Position: " + position, Toast.LENGTH_LONG);
        mToast.show();
    }
}
