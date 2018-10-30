package co.alexdev.moviescrib.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.MoviesViewHolder moviesViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
