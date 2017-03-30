package ngohoanglong.com.nowplaying.display.v2;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.util.Log;

import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ngohoanglong.com.nowplaying.data.MovieBoxService;
import ngohoanglong.com.nowplaying.data.model.Movie;
import ngohoanglong.com.nowplaying.data.request.BaseRequest;
import ngohoanglong.com.nowplaying.data.request.BaseRequestMovieList;
import ngohoanglong.com.nowplaying.data.request.RequestSectionList;
import ngohoanglong.com.nowplaying.data.response.BaseResponse;
import ngohoanglong.com.nowplaying.data.response.ResponseSection;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieHM;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.TrailerMovieHM;
import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.delegate.BaseState;
import ngohoanglong.com.nowplaying.util.delegate.BaseStateViewModel;
import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Long on 3/14/2017.
 */

public class MainViewModel extends BaseStateViewModel<MainViewModel.MainState> {
    private static final String TAG = "MainViewModel";
    protected PublishSubject<Boolean> refresh = PublishSubject.create();
    MovieBoxService service;
    List<Section> mSections;
    PublishSubject<Boolean> startUI = PublishSubject.create();

    Observable<Boolean> getStartUI(){
        return startUI.asObservable()
                .compose(withScheduler());
    }

    @Inject
    public MainViewModel(ThreadScheduler threadScheduler,
                         Resources resources,
                         MovieBoxService service,
                         List<Section> sections
    ) {
        super(threadScheduler, resources);
        this.service = service;
        this.mSections = sections;
    }

    public boolean isNeedLoadFirst() {
        return mSections == null || mSections.isEmpty();
    }

    @RxLogObservable
    public Observable<BaseResponse> loadFirst() {
             return service.sendRequest(new RequestSectionList())
                    .takeUntil(refresh)
                    .compose(withScheduler())
                    .doOnNext(baseResponse -> {
                        Log.d(TAG, "BaseResponse: " + (baseResponse.isSuccessfull() == BaseResponse.ResponseStatus.ISSUCCESSFULL));
                        ResponseSection responseSection = (ResponseSection) baseResponse;
                        List<Section> sections = new ArrayList<>();
                        for (int i = 0; i < responseSection.baseRequests.size(); i++
                                ) {
                            sections.add(new Section(responseSection.baseRequests.get(i),
                                            new ObservableArrayList<BaseHM>(),
                                            ((BaseRequestMovieList) responseSection.baseRequests.get(i)).getName(),
                                            responseSection.urlBackgroundList.get(i)
                                    )

                            );
                        }
                        if (sections.size() > 0) {
                            mSections.addAll(sections);
                        } else {
                            Log.d(TAG, "loadFirst: return zero");
                        }
                        Log.d(TAG, "call: " + sections.size());
                        Log.d(TAG, "call: getState()" + mSections.size());
                    })
                    ;

    }

    @Override
    public void bindViewModel() {
    }

    public static class MainState extends BaseState {

    }

    public class Section implements Serializable {
        BaseRequest baseRequest;
        ObservableArrayList<BaseHM> baseHMs;
        String title;
        String urlBackGround;

        public Section(BaseRequest baseRequest, ObservableArrayList<BaseHM> baseHMs, String title, String urlBackGround) {
            this.baseRequest = baseRequest;
            this.baseHMs = baseHMs;
            this.title = title;
            this.urlBackGround = urlBackGround;
        }

        public Section(BaseRequest baseRequest, ObservableArrayList<BaseHM> baseHMs) {
            this.baseRequest = baseRequest;
            this.baseHMs = baseHMs;
        }

        public Section(BaseRequest baseRequest, ObservableArrayList<BaseHM> baseHMs, String title) {
            this.baseRequest = baseRequest;
            this.baseHMs = baseHMs;
            this.title = title;
        }

        public BaseRequest getBaseRequest() {
            return baseRequest;
        }

        public void setBaseRequest(BaseRequest baseRequest) {
            this.baseRequest = baseRequest;
        }

        public ObservableArrayList<BaseHM> getBaseHMs() {
            return baseHMs;
        }

        public void setBaseHMs(ObservableArrayList<BaseHM> baseHMs) {
            this.baseHMs = baseHMs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public class Mapper {
        public BaseHM tranToMovieVM(Movie movie) {
            return new MovieHM(movie);
        }

        public BaseHM tranToMovieTrailerVM(Movie movie) {
            TrailerMovieHM trailerMovieHM = new TrailerMovieHM(movie);
            trailerMovieHM.setFullSpan(true);
            return trailerMovieHM;
        }

        public List<BaseHM> tranToVM(List<Movie> movieList) {
            List<BaseHM> list = new ArrayList<>();
            for (Movie item : movieList) {
                if (item.getVoteAverage() > 6) {
                    list.add(tranToMovieTrailerVM(item));
                } else {
                    list.add(tranToMovieVM(item));
                }
            }
            return list;
        }
    }
}
