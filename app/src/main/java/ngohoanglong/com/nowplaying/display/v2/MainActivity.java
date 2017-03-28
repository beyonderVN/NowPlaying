package ngohoanglong.com.nowplaying.display.v2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
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

    //Construct
    private RxDelegate rxDelegate = new RxDelegate();

    @Inject
    MainViewModel mainViewModel;

    private StateDelegate stateDelegate ;


    MainViewModel.MainState getState (){
        return mainViewModel.getState();
    }

    List<MainViewModel.Section> getSections(){
        return getState().sections;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rxDelegate = new RxDelegate();
        stateDelegate = new StateDelegate() {
            @NonNull
            @Override
            protected MainViewModel createViewModel() {
                return mainViewModel;
            }

            @NonNull
            @Override
            protected MainViewModel.MainState createStateModel() {
                return new MainViewModel.MainState(new ArrayList<>());
            }
        };
        lifecycleDelegates.add(rxDelegate);
        lifecycleDelegates.add(stateDelegate);

        NowPlayingApplication.appComponent.inject(this);

        setContentView(R.layout.activity_main_v2);

        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupUI();

    }
    void setupUI(){
        setupStatusBar();
    }

    void setupViewPage(){

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SectionFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this,getState().sections));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
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
        mainViewModel.loadFirst()
                .takeUntil(rxDelegate.stopEvent())
                .subscribe(sections -> {
                    if(sections.size()>0){
                        setupViewPage();
                    }
                });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
//        if(dragPanelMovieDetailDelegate.draggablePanel.isMaximized()){
//            dragPanelMovieDetailDelegate.draggablePanel.minimize();
//            return;
//        }
//        super.onBackPressed();
    }
}
