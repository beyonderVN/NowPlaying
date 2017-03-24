package ngohoanglong.com.nowplaying.data;

import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.RequestMovieBySection;
import ngohoanglong.com.nowplaying.data.request.RequestSection;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import rx.Observable;

/**
 * Created by Long on 3/24/2017.
 */

public interface RequestFactory {

    enum RequestType{
        REQUEST_SECTION,
        REQUEST_MOVIE_BY_SECTION
    }

    Observable<BaseResponse> sendRequest(BaseRequest requestType);

    RequestType getRequestType(RequestSection requestSection);
    RequestType getRequestType(RequestMovieBySection requestMovieBySection);
}
