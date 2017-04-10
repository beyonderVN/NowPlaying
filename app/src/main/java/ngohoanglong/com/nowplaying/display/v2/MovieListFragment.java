package ngohoanglong.com.nowplaying.display.v2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.vnwarriors.advancedui.appcore.common.recyclerviewhelper.InfiniteScrollListener;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.NowPlayingApplication;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.dependencyinjection.module.MovieModule;
import ngohoanglong.com.nowplaying.display.recyclerview.MumAdapter;
import ngohoanglong.com.nowplaying.display.recyclerview.holderfactory.HolderFactoryImpl;
import ngohoanglong.com.nowplaying.util.delegate.RxDelegate;
import ngohoanglong.com.nowplaying.util.delegate.StateDelegate;
import ngohoanglong.com.nowplaying.util.mvvm.BaseDelegateFragment;


public class MovieListFragment extends BaseDelegateFragment {
    private static final String TAG = "MovieListFragment";

    private static final String SECTION_POS = "param1";

    // TODO: Rename and change types of parameters
    private BaseRequest baseRequest;

    public MovieListFragment() {
        // Required empty public constructor
    }


    public static MovieListFragment newInstance(int pos) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_POS, pos);
        fragment.setArguments(args);
        return fragment;
    }

    int pos;
    @Inject
    MovieListViewModel movieListViewModel;

    MainViewModel.Section section;

    private RxDelegate rxDelegate;
    private StateDelegate stateDelegate;


    @BindInt(R.integer.column_num)
    int columnNum;
    @BindView(R.id.rvMovieList)
    RecyclerView listRV;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.viewAnimator)
    ViewAnimator viewAnimator;
    @BindView(R.id.tvErrorMessage)
    TextView tvErrorMessage;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        NowPlayingApplication.appComponent.plus(new MovieModule()).inject(this);
        rxDelegate = new RxDelegate();
        stateDelegate = new StateDelegate() {
            @NonNull
            @Override
            protected MovieListViewModel createViewModel() {
                return movieListViewModel;
            }

            @NonNull
            @Override
            protected MovieListViewModel.MovieListState createStateModel() {
                return new MovieListViewModel.MovieListState();
            }
        };
        lifecycleDelegates.add(rxDelegate);
        lifecycleDelegates.add(stateDelegate);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pos = getArguments().getInt(SECTION_POS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);
        setupUI();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        movieListViewModel.section = ((MainActivity) getActivity()).getSections().get(pos);
        movieListViewModel.bindViewModel();
        movieListViewModel.getIsLoadingMore()
        .takeUntil(rxDelegate.stopEvent())
        .subscribe(aBoolean -> {
            Log.d(TAG,"isLoadingMore: "+isLoadingMore);
            isLoadingMore = aBoolean;
        });
        movieListViewModel.loadFirst();

        MumAdapter mumAdapter = new MumAdapter(getContext(),
                new HolderFactoryImpl(),
                movieListViewModel.getPosts(),
                ((MainActivity) getActivity()).dragPanelMovieDetailDelegate
                );
        listRV.setAdapter(mumAdapter);

        movieListViewModel.getErrorStringPublishSubject()
                .takeUntil(rxDelegate.stopEvent())
                .subscribe(errorString -> {
                    viewAnimator.setDisplayedChild(2);
                    tvErrorMessage.setText(tvErrorMessage.getText()+errorString);

                });


    }

    void setupUI() {
        setupRV();
    }

    private void setupSwipeRefreshLayout() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(() -> {
            listRV.setLayoutFrozen(true);
            swipeRefresh.setRefreshing(true);
        });
    }

    boolean isLoadingMore;

    void setupRV() {
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
                    movieListViewModel.loadMore();
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
