package co.alexdev.moviescrib_phase2.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Reviews;

public class ReviewsMoviesAdapter extends RecyclerView.Adapter<ReviewsMoviesAdapter.ReviewsViewHolder> {

    List<Reviews> mReviewsList;

    public ReviewsMoviesAdapter(List<Reviews> mReviewsList) {
        this.mReviewsList = mReviewsList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie_reviews, viewGroup, false);
        return new ReviewsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder reviewsViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return ((mReviewsList != null && mReviewsList.size() > 0) ? mReviewsList.size() : 0);
    }

    static class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView tv_reviewer_name;
        TextView tv_reviewer_comment;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_reviewer_name = itemView.findViewById(R.id.tv_reviewer_name);
            tv_reviewer_comment = itemView.findViewById(R.id.tv_reviewer_comment);
        }
    }
}
