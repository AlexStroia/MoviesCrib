package co.alexdev.moviescrib.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import co.alexdev.moviescrib.Model.Movie;
import co.alexdev.moviescrib.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> movieList;
    private static onMovieClickListener mMovieClickListener;

    public interface onMovieClickListener {
        void onMovieClick(int position);
    }

    public MoviesAdapter(List<Movie> movieList, onMovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.mMovieClickListener = movieClickListener;
    }

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        final Context context = viewGroup.getContext();
        final View rootView = LayoutInflater.from(context).inflate(R.layout.movies_list_item, viewGroup, false);
        return new MoviesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder moviesViewHolder, int i) {

        final String title = movieList.get(i).getTitle();
        final String image = movieList.get(i).getImage();

        moviesViewHolder.tv_movie_title.setText(title);
        //Picasso.get().load().into(moviesViewHolder.iv_movie);
        //TODO - LOAD WITH PICASSO IMAGE
    }

    @Override
    public int getItemCount() {
        return (movieList != null && movieList.size() > 0 ? movieList.size() : 0);
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_movie;
        TextView tv_movie_title;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_movie = itemView.findViewById(R.id.iv_poster);
            tv_movie_title = itemView.findViewById(R.id.tv_movie_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            mMovieClickListener.onMovieClick(position);
        }
    }
}
