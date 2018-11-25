package co.alexdev.moviescrib_phase2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import co.alexdev.moviescrib_phase2.fragments.FavouritesFragment;
import co.alexdev.moviescrib_phase2.fragments.MostPopularFragment;
import co.alexdev.moviescrib_phase2.fragments.TopRatedFragment;

/*Class used to populate the pages inside a ViewPager*/
public class PagerAdapter extends FragmentStatePagerAdapter {

    private int mNumberOfTabs;

    public PagerAdapter(FragmentManager fm, final int mNumberOfTabs) {
        super(fm);
        this.mNumberOfTabs = mNumberOfTabs;
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
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return (mNumberOfTabs != 0 ? mNumberOfTabs : 0);
    }
}
