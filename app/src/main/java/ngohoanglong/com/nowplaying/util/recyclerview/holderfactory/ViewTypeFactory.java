package ngohoanglong.com.nowplaying.util.recyclerview.holderfactory;


import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.LoadingMoreHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.MovieDetailHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.NoMoreItemHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.TrailerMovieHM;

/**
 * Created by Long on 11/10/2016.
 */
public interface ViewTypeFactory {

    int getType(LoadingMoreHM loadingMoreHM);

    int getType(MovieDetailHM movieDetailHM);

    int getType(MovieHM movieHM);

    int getType(NoMoreItemHM noMoreItemHM);

    int getType(TrailerMovieHM trailerMovieHM);
}
