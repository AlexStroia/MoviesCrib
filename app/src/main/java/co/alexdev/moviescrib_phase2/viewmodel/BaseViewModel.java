package co.alexdev.moviescrib_phase2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.MovieApplicationRepository;

public class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);

        getData();
    }

    private void getData() {
        MovieApplicationRepository.getInstance(this.getApplication()).getPopularMoviesCall();
        MovieApplicationRepository.getInstance(this.getApplication()).getTopRatedMoviesCall();
    }

    public LiveData<List<Movie>> getMostPopularMoviesFromDatabase() {
        return MovieApplicationRepository.getInstance(this.getApplication()).getMostPopularMoviesFromDatabase();
    }

    public LiveData<List<Movie>> getTopRatedMoviesFromDatabase() {
        return MovieApplicationRepository.getInstance(this.getApplication()).getTopRatedMoviesFromDatabase();
    }

    public LiveData<List<Favorite>> getFavoritesMoviesFromDatabase() {
        return MovieApplicationRepository.getInstance(this.getApplication()).getFavoritesMoviesFromDatabase();
    }
}
