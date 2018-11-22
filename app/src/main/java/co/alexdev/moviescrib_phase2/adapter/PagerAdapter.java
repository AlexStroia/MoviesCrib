package co.alexdev.moviescrib_phase2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import co.alexdev.moviescrib_phase2.fragments.FavouritesFragment;
import co.alexdev.moviescrib_phase2.fragments.MostPopularFragment;
import co.alexdev.moviescrib_phase2.fragments.TopRatedFragment;

/*Class used to populate the pages inside a ViewPager*/
public class PagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(FragmentManager fm, final int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MostPopularFragment();

            case 1:
                return new TopRatedFragment();

            case 2:
                return new FavouritesFragment();

        }
        return null;
    }


    @Override
    public int getCount() {
        return (numberOfTabs != 0 ? numberOfTabs : 0);
    }
}
