package co.alexdev.moviescrib_phase2.fragments;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.adapter.FavoritesAdapter;
import co.alexdev.moviescrib_phase2.model.Favorite;
import co.alexdev.moviescrib_phase2.model.MoviesListener;
import co.alexdev.moviescrib_phase2.utils.Enums;
import co.alexdev.moviescrib_phase2.utils.MovieUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends BaseFragment {

    private RecyclerView rv_favorites;
    private FavoritesAdapter favoritesAdapter;
    private int favoritesListSize = 0;
    private MoviesListener.onNoFavoritesAdded mNoFavoritesAddedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mNoFavoritesAddedListener = (MoviesListener.onNoFavoritesAdded) getActivity();
        } catch (ClassCastException ex) {
            ex.getMessage();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        rv_favorites = view.findViewById(R.id.rv_favorites);
        rv_favorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoritesAdapter = new FavoritesAdapter(new ArrayList<Favorite>());
        rv_favorites.setAdapter(favoritesAdapter);

        retrieveFavorites();
        return view;
    }

    private void retrieveFavorites() {
        final LiveData<List<Favorite>> favoriteList = baseViewModel.getFavoritesMoviesFromDatabase();
        favoriteList.observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                favoritesListSize = favorites.size();
                favoritesAdapter.setFavoriteList(favorites);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && favoritesListSize == 0) {
            MovieUtils.showDialog(getActivity(), Enums.DialogType.NO_FAVORITES, mNoFavoritesAddedListener);
        }
    }
}