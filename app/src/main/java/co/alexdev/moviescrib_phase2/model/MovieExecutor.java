package co.alexdev.moviescrib_phase2.model;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/*Executor class used for the database operations*/
public final class MovieExecutor {

    private static final Object LOCK = new Object();
    private static MovieExecutor sMovieExecutor;
    private final Executor movieIO;
    private final Executor networkIO;
    private final Executor mainThreadIO;


    public MovieExecutor(Executor movieIO, Executor networkIO, Executor mainThreadIO) {
        this.movieIO = movieIO;
        this.networkIO = networkIO;
        this.mainThreadIO = mainThreadIO;
    }

    /*Executors.newSingleThreadExecutor() - make sure that operations are done in order*/
    public static MovieExecutor getInstance() {
        if (sMovieExecutor == null) {
            synchronized (LOCK) {
                sMovieExecutor = new MovieExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sMovieExecutor;
    }

    public Executor getNetworkIO() {
        return networkIO;
    }

    public Executor getMainThreadIO() {
        return mainThreadIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mainThreadHandler.post(runnable);
        }
    }

    public Executor getMovieIO() {
        return movieIO;
    }
}
