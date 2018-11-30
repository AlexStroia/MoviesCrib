package co.alexdev.moviescrib_phase2.model;

import java.util.List;

public final class MoviesListener {
    /*Listener used when the view pager position is changed
    * used to update the position of the menu */
    public interface onViewPagerPositionChangedListener {
        void onViewPagerPositionChanged(int position);
    }

    /*Listener used when the navigation view position is changed
     * used to update the position of the view pager */
    public interface onNavigationViewPositionChangedListener {
        void onNavigationViewPositionChanged(int position);
    }

    public interface onNoFavoritesAdded {
        void onNoFavoritesMoviesAdded();
    }

    /*Listener used when the data is received from the server*/
    public interface MovieListListener {
        void onMostPopularListReceivedListener(List<Movie> movieList);

        void onTopRatedListReceivedListener(List<Movie> movieList);

        void onTrailerListReceivedListener(List<Trailer> trailerList);

        void onReviewsListReceivedListener(List<Reviews> reviewsList);
    }
}
