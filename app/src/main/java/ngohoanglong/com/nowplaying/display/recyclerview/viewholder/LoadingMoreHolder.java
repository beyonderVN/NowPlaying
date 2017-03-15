package ngohoanglong.com.nowplaying.display.recyclerview.viewholder;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.LoadingMoreHM;

/**
 * Created by Long on 10/5/2016.
 *
 */

public class LoadingMoreHolder extends BaseViewHolder<LoadingMoreHM> {
    private static final String TAG = "MovieViewHolder";
    ProgressBar progress;
    public LoadingMoreHolder(View itemView) {
        super(itemView);
        progress = (ProgressBar) itemView;
    }

    @Override
    public  void bind(LoadingMoreHM item) {

        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
        progress.setVisibility(View.VISIBLE );

    }
}
