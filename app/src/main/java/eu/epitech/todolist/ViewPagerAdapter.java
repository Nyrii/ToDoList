package eu.epitech.todolist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by noboud_n on 14/01/2017.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new FragmentPage();
    }

    @Override
    public int getCount() {
        return TaskSaving.getCategories().size();
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;

    }

}
