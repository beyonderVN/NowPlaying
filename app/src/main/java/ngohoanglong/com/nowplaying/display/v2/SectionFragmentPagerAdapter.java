package ngohoanglong.com.nowplaying.display.v2;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class SectionFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<MainViewModel.Section> sections;
    Context context;

    public SectionFragmentPagerAdapter(FragmentManager fm, Context context, List<MainViewModel.Section> sections) {
        super(fm);
        this.context = context;
        this.sections = sections;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Fragment getItem(int position) {
        return MovieListFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return sections.get(position).getTitle();
    }
}