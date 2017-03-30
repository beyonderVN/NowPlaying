package ngohoanglong.com.nowplaying.data.request;

import ngohoanglong.com.nowplaying.data.RequestFactory;

/**
 * Created by Long on 3/24/2017.
 */

public class RequestPopularList extends BaseRequestMovieList {

    public RequestPopularList(int page, String name) {
        super(page, name);
    }

    @Override
    public RequestFactory.RequestType getType(RequestFactory requestFactory) {
        return requestFactory.getRequestType(this);
    }

}
