package ngohoanglong.com.nowplaying.util.recyclerview.viewholder;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;



import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.MovieHM;

/**
 * Created by Long on 10/5/2016.
 *
 */

public class NoMoreItemHolder extends BaseViewHolder<MovieHM> {
    private static final String TAG = "NoMoreItemHolder";

    public NoMoreItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public  void bind(MovieHM item) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
        layoutParams.setFullSpan(true);
    }
}
