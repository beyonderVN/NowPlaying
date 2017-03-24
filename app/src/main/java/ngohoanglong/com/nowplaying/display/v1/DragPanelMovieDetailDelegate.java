package ngohoanglong.com.nowplaying.display.v1;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.github.pedrovgs.DraggableListener;
import com.github.pedrovgs.DraggablePanel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.BuildConfig;
import ngohoanglong.com.nowplaying.NowPlayingApplication;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.dependencyinjection.module.MovieModule;
import ngohoanglong.com.nowplaying.display.recyclerview.MumAdapter;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.util.delegate.BaseDelegate;
import ngohoanglong.com.nowplaying.util.delegate.RxDelegate;
import ngohoanglong.com.nowplaying.util.delegate.StateDelegate;
import ngohoanglong.com.nowplaying.util.mvvm.BaseDelegateActivity;
import rx.Subscriber;


/**
 * Created by Long on 11/15/2016.
 */

public class DragPanelMovieDetailDelegate extends BaseDelegate implements MumAdapter.OnSelectItemClickEvent {

    @BindView(R.id.draggable_panel)
    DraggablePanel draggablePanel;
    BaseDelegateActivity activity;

    Movie movie;
    RxDelegate rxDelegate;

    @Inject
    MovieDetailViewModel movieDetailViewModel;
    public StateDelegate movieDetailStateDelegate = new StateDelegate() {
        @NonNull
        @Override
        protected MovieDetailViewModel createViewModel() {
            return movieDetailViewModel;
        }

        @NonNull
        @Override
        protected MovieDetailViewModel.MovieDetailState createStateModel() {
            return new MovieDetailViewModel.MovieDetailState();
        }
    };

    public DragPanelMovieDetailDelegate(BaseDelegateActivity activity, RxDelegate rxDelegate) {
        this.activity = activity;
        this.rxDelegate = rxDelegate;
    }

    @Override
    public void onCreate(Bundle bundle) {
        NowPlayingApplication.appComponent
        .plus(new MovieModule())
        .inject(this);
        movieDetailStateDelegate.onCreate(bundle);
        ButterKnife.bind(this, activity);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        movieDetailStateDelegate.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        movieDetailStateDelegate.onStart();
        initializeYoutubeFragment();
        initializeDraggablePanel();
        hookDraggablePanelListeners();

    }

    private static YouTubePlayer youtubePlayer;
    private YouTubePlayerSupportFragment youtubeFragment;



    /**
     * Initialize the YouTubeSupportFrament attached as top fragment to the DraggablePanel widget and
     * reproduce the YouTube video represented with a YouTube url.
     */

    private void initializeYoutubeFragment() {
        String urlTrailer = ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).getUrlTrailer();
        youtubeFragment = YouTubePlayerSupportFragment.newInstance();
        youtubeFragment.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                          YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {

                    youtubePlayer = player;

                    if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                        youtubePlayer.setFullscreen(true);
                    }
                    youtubePlayer.loadVideo(urlTrailer);
                    youtubePlayer.setShowFullscreenButton(true);
                }
            }

            @Override public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                          YouTubeInitializationResult error) {
            }
        });
    }

    /**
     * Initialize and configure the DraggablePanel widget with two fragments and some attributes.
     */
    MoviePosterFragment moviePosterFragment;
    private void initializeDraggablePanel() {
        boolean isVisible = ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).isVisibale();
        draggablePanel.setVisibility(isVisible==true?View.VISIBLE:View.GONE);
        draggablePanel.setFragmentManager(activity.getSupportFragmentManager());
        draggablePanel.setTopFragment(youtubeFragment);
        moviePosterFragment = new MoviePosterFragment();
        movie = ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).getMovie();
        if (movie != null) {
            moviePosterFragment.setMovie(movie);
        }
        draggablePanel.setBottomFragment(moviePosterFragment);
        draggablePanel.initializeView();
    }

    /**
     * Hook the DraggableListener to DraggablePanel to pause or resume the video when the
     * DragglabePanel is maximized or closed.
     */
    private void hookDraggablePanelListeners() {
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override public void onMaximized() {
                playVideo();
            }

            @Override public void onMinimized() {
                //Empty
            }

            @Override public void onClosedToLeft() {
                pauseVideo();
            }

            @Override public void onClosedToRight() {
                pauseVideo();
            }
        });
    }

    /**
     * Pause the video reproduced in the YouTubePlayer.
     */
    private void pauseVideo() {
        if (youtubePlayer.isPlaying()) {
            youtubePlayer.pause();
        }
    }

    /**
     * Resume the video reproduced in the YouTubePlayer.
     */
    private void playVideo() {
        if (!youtubePlayer.isPlaying()) {
            youtubePlayer.play();
        }
    }

    @Override
    public void onItemClick(int pos, BaseHM baseHM) {
        if(draggablePanel.getVisibility()== View.GONE)draggablePanel.setVisibility(View.VISIBLE);
        if(baseHM instanceof TrailerMovieHM){
            movie = ((TrailerMovieHM) baseHM).getMovie();
        }else{
            movie = ((MovieHM) baseHM).getMovie();
        }
        ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).setMovie(movie);
        ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).setVisibale(draggablePanel.getVisibility()== View.VISIBLE);

        Log.d("onItemClick", "onItemClick: "+movie.toString());
        moviePosterFragment.setDetail(movie);
        movieDetailViewModel.getYouTubeUrl(movie.getId())
        .takeUntil(rxDelegate.stopEvent())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                draggablePanel.maximize();
            }

            @Override
            public void onError(Throwable e) {
                draggablePanel.maximize();
            }

            @Override
            public void onNext(String s) {
                ((MovieDetailViewModel.MovieDetailState)movieDetailViewModel.getState()).setUrlTrailer(s);
                if(activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    youtubePlayer.setFullscreen(true);
                }
                youtubePlayer.loadVideo(s);
                youtubePlayer.setShowFullscreenButton(true);
            }
        })

        ;
    }



}
