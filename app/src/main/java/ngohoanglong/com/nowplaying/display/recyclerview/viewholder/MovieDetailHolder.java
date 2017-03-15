package ngohoanglong.com.nowplaying.display.recyclerview.viewholder;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vnwarriors.advancedui.appcore.common.DynamicHeightImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.MovieDetailHM;

/**
 * Created by Long on 10/5/2016.
 *
 */

public class MovieDetailHolder extends BaseViewHolder<MovieDetailHM> {
    private static final String TAG = "MovieViewHolder";
    @BindView(R.id.ivHeader)
    DynamicHeightImageView imageView;
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;

    public MovieDetailHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
    public MovieDetailHolder(View itemView, boolean fullspan) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public  void bind(MovieDetailHM item) {
        MovieDetailHM movieVM = item;
        imageView.setRatio(1.5);
        Picasso.with(imageView.getContext())
                .load("https://image.tmdb.org/t/p/w342"+movieVM.getMovie().getPosterPath())
                .into(imageView);
        tvOverview.setText(movieVM.getMovie().getOverview());
        tvTitle.setText(movieVM.getMovie().getTitle());
        tvReleaseDate.setText(Html.fromHtml("<b>Release Date: </b><small>"+movieVM.getMovie().getReleaseDate()+"<small>"));
        if(item.isFullSpan()){
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }

    }
}
