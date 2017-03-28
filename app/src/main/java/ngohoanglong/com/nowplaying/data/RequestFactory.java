package ngohoanglong.com.nowplaying.data;

import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.RequestMovieBySection;
import ngohoanglong.com.nowplaying.data.request.RequestNowPlaying;
import ngohoanglong.com.nowplaying.data.request.RequestSectionList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import rx.Observable;

/**
 * Created by Long on 3/24/2017.
 */

public interface RequestFactory {

    RequestType getRequestType(RequestNowPlaying requestNowPlaying);

    enum RequestType{
        REQUEST_SECTION,
        REQUEST_MOVIE_BY_SECTION,
        REQUEST_NOW_PLAYING
    }

    Observable<BaseResponse> sendRequest(BaseRequest requestType);

    RequestType getRequestType(RequestSectionList requestSectionList);
    RequestType getRequestType(RequestMovieBySection requestMovieBySection);
}
