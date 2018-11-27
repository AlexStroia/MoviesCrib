package co.alexdev.moviescrib_phase2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Movie;

@Dao
public interface MovieDao {

    @Query("SELECT * from movies")
    LiveData<List<Movie>> getMostPopularMovies();

    LiveData<List<Movie>> getTopRatedMovies();

    LiveData<List<Movie>>getFavoritesMovies();

    //TODO MAke 3 more list for toprated, most popular, favorites

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieToFavorites(Movie movie);

    @Delete
    void deleteMovieFromFavorites(Movie movie);
}
