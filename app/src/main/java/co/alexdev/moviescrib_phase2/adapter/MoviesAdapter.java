package co.alexdev.moviescrib.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.alexdev.moviescrib.model.Movie;
import co.alexdev.moviescrib.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> movieList;
    private static onMovieClickListener mMovieClickListener;
    private final Context mContext;
    private final String tmdb_image_url;

    public interface onMovieClickListener {
        void onMovieClick(int position);
    }

    public MoviesAdapter(Context context, List<Movie> movieList, onMovieClickListener movieClickListener) {
        this.mContext = context;
        this.movieList = movieList;
        this.mMovieClickListener = movieClickListener;
        tmdb_image_url = context.getString(R.string.tmdb_image_url);
    }

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final Context context = mContext;
        final View rootView = LayoutInflater.from(context).inflate(R.layout.item_movie_list, viewGroup, false);
        return new MoviesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.MoviesViewHolder moviesViewHolder, int i) {
        final String title = movieList.get(i).getTitle();
        final String imagePath = movieList.get(i).getPoster_path();
        final String imageUri = buildImageUri(imagePath);

        moviesViewHolder.tv_movie_title.setText(title);
        Picasso.get().load(imageUri)
                .into(moviesViewHolder.iv_movie, new Callback() {
                    @Override
                    public void onSuccess() {
                        moviesViewHolder.pb_movie_progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return (movieList != null && movieList.size() > 0 ? movieList.size() : 0);
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    private String buildImageUri(final String imagePath) {
        String imageString = new StringBuilder().append(tmdb_image_url).append(imagePath).toString();
        return imageString;
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_movie;
        TextView tv_movie_title;
        ProgressBar pb_movie_progress;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_movie = itemView.findViewById(R.id.iv_poster);
            tv_movie_title = itemView.findViewById(R.id.tv_movie_name);
            pb_movie_progress = itemView.findViewById(R.id.pb_movie_loading);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            mMovieClickListener.onMovieClick(position);
        }
    }
}
