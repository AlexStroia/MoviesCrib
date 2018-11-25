package co.alexdev.moviescrib_phase2.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.alexdev.moviescrib_phase2.R;
import co.alexdev.moviescrib_phase2.activities.BaseActivity;
import co.alexdev.moviescrib_phase2.adapter.PagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements BaseActivity.onViewPagerPositionChangedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    public BaseFragment() {
        // Required empty public constructor
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

    private void initView() {
        setupViewPager();
        setupTabLayout();
    }

    private void setupViewPager() {
        FragmentManager fragmentManager = getChildFragmentManager();
        pagerAdapter = new PagerAdapter(fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
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
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    /*Check if is not the same position */
    @Override
    public void onViewPagerPositionChanged(int position) {
        if (position != viewPager.getCurrentItem()) {
            viewPager.setCurrentItem(position);
        }
    }
}
