package ngohoanglong.com.nowplaying.util.mvvm;

import android.content.res.Resources;
import android.databinding.ObservableArrayList;

import java.util.List;

import ngohoanglong.com.nowplaying.util.ThreadScheduler;
import ngohoanglong.com.nowplaying.util.delegate.BaseState;
import ngohoanglong.com.nowplaying.util.delegate.BaseStateViewModel;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.LoadingMoreHM;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public abstract class PostViewModel extends BaseStateViewModel<PostViewModel.PostsState> {
    private static final String TAG = "PostViewModel";

    protected ObservableArrayList<BaseHM> posts;
    protected BehaviorSubject<Boolean> isLoadingMore = BehaviorSubject.create(false);

    public PostViewModel(ThreadScheduler threadScheduler,
                         Resources resources) {
        super(threadScheduler, resources);

    }

    public Observable<Boolean> getIsLoadingMore() {
        return isLoadingMore.asObservable().distinctUntilChanged()
                .doOnNext(aBoolean -> {
                    if (aBoolean){
                        showLoadingMore();
                    }else {
                        hideLoadingMore();
                    }
                });
    }
    public ObservableArrayList<BaseHM> getPosts() {
        return posts;
    }

    protected void removePost(int position) {
        posts.remove(position);
    }

    protected int indexOf(BaseHM post) {
        return posts.indexOf(post);
    }

    protected void updatePosts(List<BaseHM> posts) {
        if (this.posts == null) {
            this.posts = new ObservableArrayList<>();
        }
        this.posts.clear();
        this.posts.addAll(posts);
    }

    public void setPost(BaseHM post) {
        posts.set(indexOf(post), post);
    }

    public void addPost(BaseHM post) {
        posts.add(0, post);
    }

    protected void showLoadingMore() {
        this.posts.add(new LoadingMoreHM());
    }

    protected void hideLoadingMore() {
        if(posts.size()==0)return;
        if (posts.get(posts.size() - 1) instanceof LoadingMoreHM) {
            removePost(posts.size() - 1);
        }
    }
    @Override
    public PostsState saveInstanceState() {
        hideLoadingMore();
        return state;
    }
    @Override
    public void returnInstanceState(PostsState instanceState) {
        super.returnInstanceState(instanceState);
        posts = getState().getBaseHMs();
    }

    public static class PostsState extends BaseState {
        ObservableArrayList<BaseHM> baseHMs;

        public PostsState(ObservableArrayList<BaseHM> baseHMs) {
            this.baseHMs = baseHMs;
        }

        public ObservableArrayList<BaseHM> getBaseHMs() {
            return baseHMs;
        }

        public void setBaseHMs(ObservableArrayList<BaseHM> baseHMs) {
            this.baseHMs = baseHMs;
        }
    }

}