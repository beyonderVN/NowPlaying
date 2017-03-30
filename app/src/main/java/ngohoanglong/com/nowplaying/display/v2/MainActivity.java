package ngohoanglong.com.nowplaying.display.v2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.NowPlayingApplication;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.util.delegate.RxDelegate;
import ngohoanglong.com.nowplaying.util.delegate.StateDelegate;
import ngohoanglong.com.nowplaying.util.mvvm.BaseDelegateActivity;


public class MainActivity extends BaseDelegateActivity {
    private static final String TAG = "MainActivity";

    @Inject
    MainViewModel mainViewModel;

    private RxDelegate rxDelegate;
    private StateDelegate stateDelegate;
    DragPanelMovieDetailDelegate dragPanelMovieDetailDelegate;

    List<MainViewModel.Section> getSections() {
        return mainViewModel.mSections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NowPlayingApplication.appComponent
                .inject(this);
        rxDelegate = new RxDelegate();
        dragPanelMovieDetailDelegate = new DragPanelMovieDetailDelegate(this, rxDelegate);
        stateDelegate = new StateDelegate() {
            @NonNull
            @Override
            protected MainViewModel createViewModel() {
                return mainViewModel;
            }

            @NonNull
            @Override
            protected MainViewModel.MainState createStateModel() {
                return new MainViewModel.MainState();
            }
        };
        lifecycleDelegates.add(rxDelegate);
        lifecycleDelegates.add(stateDelegate);
        lifecycleDelegates.add(dragPanelMovieDetailDelegate);
        setContentView(R.layout.activity_main_v2);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupUI();
    }

    void setupUI() {
        setupStatusBar();
    }

    void setupViewPage() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SectionFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this, getSections()));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < getSections().size(); i++
                ) {
            tabLayout.getTabAt(i).setCustomView(R.layout.tab_custom);
            View view = tabLayout.getTabAt(i).getCustomView();
            ImageView imageView = (ImageView) view.findViewById(R.id.ivBackground);
            TextView tv = (TextView) view.findViewById(R.id.text1);
            tv.setText(getSections().get(i).getTitle());
            Picasso.with(this).load(getSections().get(i).urlBackGround)
                    .into(imageView)
            ;
            if(tabLayout.getTabAt(i).isSelected()){
                View v = view.findViewById(R.id.vMark);
                v.setAlpha(0.2f);
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            }else {
                tv.setTextColor(getResources().getColor(R.color.white));
                View v = view.findViewById(R.id.vMark);
                v.setAlpha(0.7f);
            }
        }
        if (getSections().size() > 3) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view == null) {
                    return;
                }
                TextView tv = (TextView) view.findViewById(R.id.text1);
                View v = view.findViewById(R.id.vMark);
                v.setAlpha(0.2f);
                tv.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (view == null) {
                    return;
                }
                TextView tv = (TextView) view.findViewById(R.id.text1);
                tv.setTextColor(getResources().getColor(R.color.white));
                View v = view.findViewById(R.id.vMark);
                v.setAlpha(0.7f);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setupStatusBar() {
        Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.statusbar));
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mainViewModel.isNeedLoadFirst()){
            mainViewModel.loadFirst()
                    .takeUntil(rxDelegate.stopEvent())
                    .subscribe(sections -> {
                        setupViewPage();
                    });
        }else {
            setupViewPage();
        }
        mainViewModel.bindViewModel();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (dragPanelMovieDetailDelegate.draggablePanel.isMaximized()) {
            dragPanelMovieDetailDelegate.draggablePanel.minimize();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        NowPlayingApplication.appComponent.getAppState().saveAppState();
        super.onDestroy();
    }
}
