package co.alexdev.moviescrib_phase2.model;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*Executor class used for the database operations*/
public final class MovieExecutor {

    private static final Object LOCK = new Object();
    private static MovieExecutor sMovieExecutor;
    private final Executor movieIO;

    private MovieExecutor(Executor movieIO) {
        this.movieIO = movieIO;
    }

    /*Executors.newSingleThreadExecutor() - make sure that operations are done in order*/
    public static MovieExecutor getInstance() {
        if (sMovieExecutor == null) {
            synchronized (LOCK) {
                sMovieExecutor = new MovieExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sMovieExecutor;
    }

    public Executor getMovieIO() {
        return movieIO;
    }
}
