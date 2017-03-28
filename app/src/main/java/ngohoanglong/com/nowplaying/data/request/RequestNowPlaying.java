package ngohoanglong.com.nowplaying.data.request;

import ngohoanglong.com.nowplaying.data.RequestFactory;

/**
 * Created by Long on 3/24/2017.
 */

public class RequestNowPlaying extends BaseRequest {

    int page = 1;
    String name;
    public RequestNowPlaying(int page) {
        this.page = page;
    }
    public RequestNowPlaying upPage() {
        page ++;
        return this;
    }
    public RequestNowPlaying(int page, String name) {
        this.page = page;
        this.name = name;
    }

    @Override
    public RequestFactory.RequestType getType(RequestFactory requestFactory) {
        return requestFactory.getRequestType(this);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RequestNowPlaying{");
        sb.append("page=").append(page);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
