package co.alexdev.moviescrib_phase2.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.activities.BaseActivity;
import co.alexdev.moviescrib_phase2.adapter.PagerAdapter;
import co.alexdev.moviescrib_phase2.database.MovieDatabase;
import co.alexdev.moviescrib_phase2.model.MoviesListener;
import co.alexdev.moviescrib_phase2.viewmodel.BaseViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements MoviesListener.onNavigationViewPositionChangedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "BaseFragment";
    public TabLayout tabLayout;
    public ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private int viewPagerPosition = 0;
    public MovieDatabase mDb;
    private MoviesListener.onViewPagerPositionChangedListener mListener;

    boolean canStoreOfflineData = false;
    BaseViewModel baseViewModel;
    SharedPreferences mSharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseViewModel = ViewModelProviders.of(this.getActivity()).get(BaseViewModel.class);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        canStoreOfflineData = mSharedPreferences.getBoolean(getString(R.string.store_offline_key), false);

        mDb = MovieDatabase.getInstance(getActivity().getApplicationContext());

        try {
            mListener = (MoviesListener.onViewPagerPositionChangedListener) context;
        } catch (ClassCastException e) {
            Log.d(TAG, "BaseFragment: " + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base, container, false);

        tabLayout = rootView.findViewById(R.id.tabLayout);
        viewPager = rootView.findViewById(R.id.viewPager);

        initView();

        /*Set the listener from the activity into this fragment to update the position of the view pager*/
        ((BaseActivity) getActivity()).setViewPagerPositionListener(BaseFragment.this);
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterSharedPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*Update the menu from BaseActivity with the position*/
        mListener.onViewPagerPositionChanged(viewPagerPosition);
    }

    /*Initialize the view*/
    private void initView() {
        setupViewPager();
        setupTabLayout();
        registerSharedPreferences();
    }

    private void registerSharedPreferences() {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void unregisterSharedPreferences() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /*Setup the view pager*/
    private void setupViewPager() {
        FragmentManager fragmentManager = getChildFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        /*Prevent from making multiple calls to the first fragment*/
        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
    }

    /*Setup the tab layout
     * Set the listener for the moment when a tab is clicked
     * Set the view pager to that tab position
     * In the end we add viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout)
     * With this function our view pager will sync with the tab layout*/
    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                viewPagerPosition = tab.getPosition();
                mListener.onViewPagerPositionChanged(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        /*Sync the view pager listener with the tab layout listener*/
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        Log.d(TAG, "setupTabLayout: " + viewPager.getCurrentItem());
    }

    /*Check if is not the same position */
    @Override
    public void onNavigationViewPositionChanged(int position) {
        if (position != viewPager.getCurrentItem()) {
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.store_offline_key))) {
            canStoreOfflineData = sharedPreferences.getBoolean(key, false);
        }
    }
}
