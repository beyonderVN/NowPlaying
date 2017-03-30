package ngohoanglong.com.nowplaying.data;

import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.RequestLastedList;
import ngohoanglong.com.nowplaying.data.request.RequestMovieBySection;
import ngohoanglong.com.nowplaying.data.request.RequestNowPlaying;
import ngohoanglong.com.nowplaying.data.request.RequestPopularList;
import ngohoanglong.com.nowplaying.data.request.RequestSectionList;
import ngohoanglong.com.nowplaying.data.request.RequestUpComingList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import rx.Observable;

/**
 * Created by Long on 3/24/2017.
 */

public interface RequestFactory {

    RequestType getRequestType(RequestNowPlaying requestNowPlaying);

    RequestType getRequestType(RequestLastedList requestLastedList);

    RequestType getRequestType(RequestPopularList requestPopularList);

    RequestType getRequestType(RequestUpComingList requestUpComingList);

    enum RequestType{
        REQUEST_SECTION,
        REQUEST_MOVIE_BY_SECTION,
        REQUEST_NOW_PLAYING,
        REQUEST_LASTED,
        REQUEST_POPULAR,
        REQUEST_UPCOMING
    }

    Observable<BaseResponse> sendRequest(BaseRequest requestType);

    RequestType getRequestType(RequestSectionList requestSectionList);
    RequestType getRequestType(RequestMovieBySection requestMovieBySection);
}
