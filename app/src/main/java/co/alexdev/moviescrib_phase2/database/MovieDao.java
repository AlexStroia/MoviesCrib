package co.alexdev.moviescrib_phase2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies WHERE movieType LIKE 'MOST_POPULAR' ORDER BY title")
    LiveData<List<Movie>> getMostPopularMovies();

    @Query("SELECT * FROM movies where movieType LIKE 'TOP_RATED' ORDER BY title")
    LiveData<List<Movie>> getTopRatedMovies();

    /*Query all the selected movies that are stored as favorite*/
    @Query("SELECT * from movie_favorites")
    LiveData<List<Favorite>> getFavoritesMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Movie> movieList);

    @Query("Select * FROM movies WHERE id = :id")
    LiveData<Movie> loadMovieById(int id);

    /*Delete movie that is currently stored as favorite*/
    @Delete
    void deleteMovieFromFavorites(Favorite favorite);

    /*Insert a movie into the favorite database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToFavorite(Favorite favorite);

    @Query("Select * from movie_favorites WHERE id = :id")
    Favorite loadMovieFromFavoritesById(int id);

    /*Check if movie is markedAsFavorite*/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);
}
