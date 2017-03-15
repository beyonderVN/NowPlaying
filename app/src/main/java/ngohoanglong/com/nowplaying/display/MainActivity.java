package ngohoanglong.com.nowplaying.display;

import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Window;
import android.view.WindowManager;

import com.vnwarriors.advancedui.appcore.common.recyclerviewhelper.InfiniteScrollListener;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.NowPlayingApplication;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.display.recyclerview.MumAdapter;
import ngohoanglong.com.nowplaying.display.recyclerview.holderfactory.HolderFactoryImpl;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.util.delegate.RxDelegate;
import ngohoanglong.com.nowplaying.util.delegate.StateDelegate;
import ngohoanglong.com.nowplaying.util.mvvm.BaseDelegateActivity;


public class MainActivity extends BaseDelegateActivity {
    private static final String TAG = "MainActivity";

    //Construct
    private RxDelegate rxDelegate = new RxDelegate();

    @Inject
    MainViewModel mainViewModel;

    ObservableArrayList<BaseHM> baseHMs = new ObservableArrayList<>();

    private StateDelegate stateDelegate = new StateDelegate() {
        @NonNull
        @Override
        protected MainViewModel createViewModel() {
            return mainViewModel;
        }

        @NonNull
        @Override
        protected MainViewModel.MainState createStateModel() {
            return new MainViewModel.MainState(new ObservableArrayList<>());
        }
    };

    private DragPanelMovieDetailDelegate dragPanelMovieDetailDelegate = new DragPanelMovieDetailDelegate(this,rxDelegate);
    {
        lifecycleDelegates.add(rxDelegate);
        lifecycleDelegates.add(stateDelegate);
        lifecycleDelegates.add(dragPanelMovieDetailDelegate);
    }

    @BindInt(R.integer.column_num)
    int columnNum;
    @BindView(R.id.rvMovieList)
    RecyclerView listRV;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NowPlayingApplication.appComponent.inject(this);

        setContentView(R.layout.activity_main);

        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setupUI();

    }
    void setupUI(){
        setupRV();
        setupSwipeRefreshLayout();
        setupStatusBar();

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

    private void setupSwipeRefreshLayout() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listRV.setLayoutFrozen(true);
                swipeRefresh.setRefreshing(true);
            }
        });
    }


    boolean isLoadingMore;
    void setupRV(){
        final StaggeredGridLayoutManager staggeredGridLayoutManagerVertical =
                new StaggeredGridLayoutManager(
                        columnNum, //The number of Columns in the grid
                        LinearLayoutManager.VERTICAL);
        staggeredGridLayoutManagerVertical.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        staggeredGridLayoutManagerVertical.invalidateSpanAssignments();

        listRV.setLayoutManager(staggeredGridLayoutManagerVertical);
        listRV.setHasFixedSize(true);
        listRV.addOnScrollListener(new InfiniteScrollListener(staggeredGridLayoutManagerVertical) {
            @Override
            public void onLoadMore() {
                try {
                    mainViewModel.loadMore();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }

            @Override
            public boolean isLoading() {
                return isLoadingMore;
            }

            @Override
            public boolean isNoMore() {
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        MumAdapter baseAdapter = new MumAdapter(this, new HolderFactoryImpl(),mainViewModel.getPosts(), dragPanelMovieDetailDelegate);
        listRV.setAdapter(baseAdapter);
        mainViewModel.bindViewModel();
        mainViewModel.getIsLoadingMore()
                .takeUntil(rxDelegate.stopEvent())
                .subscribe(aBoolean -> isLoadingMore=aBoolean);
    }
}
