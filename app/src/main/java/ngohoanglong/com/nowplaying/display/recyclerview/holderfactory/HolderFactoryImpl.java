package ngohoanglong.com.nowplaying.display.recyclerview.holderfactory;

import android.view.View;

import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.NoMoreItemHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.LoadingMoreHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieDetailHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.BaseViewHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.LoadingMoreHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.MovieDetailHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.MovieTrailerItemHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.MovieViewHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.NoMoreItemHolder;


/**
 * Created by Long on 10/5/2016.
 */

public class HolderFactoryImpl implements HolderFactory {

    private static final int ITEM_MOVIE = R.layout.layout_item_movie;
    private static final int LOADING_MORE = R.layout.infinite_loading;
    private static final int NO_MORE = R.layout.infinite_no_more;
    private static final int MOVIE_DETAIL = R.layout.layout_movie_detail;
    private static final int MOVIE_TRAILER_ITEM = R.layout.layout_movie_trailer_item;



    @Override
    public BaseViewHolder createHolder(int type, View view) {
        switch(type) {
            case ITEM_MOVIE:
                return new MovieViewHolder(view);
            case LOADING_MORE:
                return new LoadingMoreHolder(view);
            case NO_MORE:
                return new NoMoreItemHolder(view);
            case MOVIE_DETAIL:
                return new MovieDetailHolder(view);
            case MOVIE_TRAILER_ITEM:
                return new MovieTrailerItemHolder(view);
        }
        return null;
    }
    @Override
    public int getType(MovieHM movieHM) {
        return ITEM_MOVIE;
    }

    @Override
    public int getType(LoadingMoreHM loadingMoreHM) {
        return LOADING_MORE;
    }

    @Override
    public int getType(NoMoreItemHM noMoreItemHM) {
        return NO_MORE;
    }

    @Override
    public int getType(MovieDetailHM movieDetailHM) {
        return MOVIE_DETAIL;
    }

    @Override
    public int getType(TrailerMovieHM trailerMovieHM) {
        return MOVIE_TRAILER_ITEM;
    }

}
