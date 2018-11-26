package co.alexdev.moviescrib_phase2.utils.listener;

import java.util.List;

import co.alexdev.moviescrib_phase2.model.Movie;
import co.alexdev.moviescrib_phase2.model.Reviews;
import co.alexdev.moviescrib_phase2.model.Trailer;

public final class BaseListener {
    /*Listener used when the view pager position is changed
    * used to update the position of the menu */
    public interface onViewPagerPositionChangedListener {
        void onViewPagerPositionChanged(int position);
    }

    /*Listener used when the navigation view position is changed
     * used to update the position of the view pager */
    public interface onNavigationViewPositionChangedListener {
        void onNavigationViewPositionChanged(final int position);
    }

    /*Listener used when the data is received from the server*/
    public interface MovieListListener {
        void onMostPopularListReceivedListener(final List<Movie> movieList);

        void onTopRatedListReceivedListener(final List<Movie> movieList);

        void onTrailerListReceivedListener(final List<Trailer> trailerList);

        void onReviewsListReceivedListener(final List<Reviews> reviewsList);
    }
}
