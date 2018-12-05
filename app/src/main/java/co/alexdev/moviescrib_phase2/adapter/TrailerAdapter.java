package co.alexdev.moviescrib_phase2.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.model.Trailer;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private static final String MIMETYPE = "text/html";
    private static final String ENCODING_UTF8 = "utf8";

    public interface OnTrailerClickListener {
        void onTrailerClickListener(int position);
    }

    private List<Trailer> trailerList;
    private static OnTrailerClickListener mListener;
    private Resources resources;
    private Context context;

    public TrailerAdapter(Context context, List<Trailer> trailerList, OnTrailerClickListener listener) {
        this.context = context;
        this.trailerList = trailerList;
        this.mListener = listener;
        resources = context.getResources();
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.item_trailer_layout, viewGroup, false);
        return new TrailerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        Trailer trailer = trailerList.get(i);
        trailerViewHolder.tv_name.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList != null && !trailerList.isEmpty() ? trailerList.size() : 0;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    static class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_name;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_movie_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onTrailerClickListener(position);
        }
    }
}
