package ngohoanglong.com.nowplaying.display;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import ngohoanglong.com.nowplaying.util.delegate.BaseDelegate;
import ngohoanglong.com.nowplaying.util.delegate.RxDelegate;
import rx.Subscriber;


/**
 * Created by Long on 11/15/2016.
 */

public class DragPanelMovieDetailDelegate extends BaseDelegate implements MumAdapter.OnSelectItemClickEvent {

    @BindView(R.id.draggable_panel)
    DraggablePanel draggablePanel;
    FragmentActivity activity;

    @Inject
    MovieDetailViewModel movieDetailViewModel;


    RxDelegate rxDelegate;
    public DragPanelMovieDetailDelegate(FragmentActivity activity, RxDelegate rxDelegate) {
        this.activity = activity;
        this.rxDelegate = rxDelegate;
        this.movieDetailViewModel = movieDetailViewModel;
    }

    @Override
    public void onCreate(Bundle bundle) {
        NowPlayingApplication.appComponent
        .plus(new MovieModule())
        .inject(this);
        ButterKnife.bind(this, activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeYoutubeFragment();
        initializeDraggablePanel();
        hookDraggablePanelListeners();
    }

    private YouTubePlayer youtubePlayer;
    private YouTubePlayerSupportFragment youtubeFragment;

    private static final String VIDEO_KEY = "gsjtg7m1MMM";
    private static final String VIDEO_POSTER_THUMBNAIL =
            "http://4.bp.blogspot.com/-BT6IshdVsoA/UjfnTo_TkBI/AAAAAAAAMWk/JvDCYCoFRlQ/s1600/"
                    + "xmenDOFP.wobbly.1.jpg";

    private static final String VIDEO_POSTER_TITLE = "X-Men: Days of Future Past";


    /**
     * Initialize the YouTubeSupportFrament attached as top fragment to the DraggablePanel widget and
     * reproduce the YouTube video represented with a YouTube url.
     */


    private void initializeYoutubeFragment() {
        youtubeFragment = YouTubePlayerSupportFragment.newInstance();
        youtubeFragment.initialize(BuildConfig.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                          YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    youtubePlayer = player;
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
    private void initializeDraggablePanel() {
        draggablePanel.setFragmentManager(activity.getSupportFragmentManager());
        draggablePanel.setTopFragment(youtubeFragment);
        MoviePosterFragment moviePosterFragment = new MoviePosterFragment();
        moviePosterFragment.setPoster(VIDEO_POSTER_THUMBNAIL);
        moviePosterFragment.setPosterTitle(VIDEO_POSTER_TITLE);
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

        Movie movie = ((MovieHM) baseHM).getMovie();
        Log.d("onItemClick", "onItemClick: "+movie.toString());
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
                youtubePlayer.loadVideo(s);
                youtubePlayer.setShowFullscreenButton(true);
            }
        })

        ;
    }



}
