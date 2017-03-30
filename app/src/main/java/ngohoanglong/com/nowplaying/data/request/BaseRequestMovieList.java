package ngohoanglong.com.nowplaying.data.request;

/**
 * Created by Long on 3/29/2017.
 */

public abstract class BaseRequestMovieList extends BaseRequest {
    int page = 1;
    String name;
    public BaseRequestMovieList(int page) {
        this.page = page;
    }
    public BaseRequestMovieList upPage() {
        page ++;
        return this;
    }
    public BaseRequestMovieList(int page, String name) {
        this.page = page;
        this.name = name;
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
        final StringBuilder sb = new StringBuilder("BaseRequestMovieList{");
        sb.append("page=").append(page);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
