package co.alexdev.moviescrib_phase2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.utils.ImageUtils;

/*Adapter used to display data from the Favorites*/
public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private List<Favorite> favoriteList;

    public FavoritesAdapter(List<Favorite> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie_favorites, viewGroup, false);
        return new FavoritesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder favoritesViewHolder, int i) {

        Favorite favorite = favoriteList.get(i);
        float numberOfStars = getRatingBarStars(favorite.getRating());
        favoritesViewHolder.iv_poster_fav.setImageBitmap(ImageUtils.decode(favorite.getImage()));
        favoritesViewHolder.tv_plot_synopsis_fav.setText(favorite.getSynopsis());
        favoritesViewHolder.tv_title_fav.setText(favorite.getTitle());
        favoritesViewHolder.rb_vote_average_fav.setRating(numberOfStars);
    }

    @Override
    public int getItemCount() {
        return (favoriteList != null && favoriteList.size() != 0 ? favoriteList.size() : 0);
    }

    public void setFavoriteList(List<Favorite> favoriteList) {
        this.favoriteList = favoriteList;
        notifyDataSetChanged();
    }

    /*Calculate the rating stars*/
    private float getRatingBarStars(final float vote_average) {
        return vote_average / 2;
    }

    static class FavoritesViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_poster_fav;
        private TextView tv_title_fav;
        private TextView tv_plot_synopsis_fav;
        private RatingBar rb_vote_average_fav;


        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_poster_fav = itemView.findViewById(R.id.iv_poster_fav);
            tv_title_fav = itemView.findViewById(R.id.tv_title_fav);
            tv_plot_synopsis_fav = itemView.findViewById(R.id.tv_plot_synopsis_fav);
            rb_vote_average_fav = itemView.findViewById(R.id.rb_vote_average_fav);
        }
    }
}
