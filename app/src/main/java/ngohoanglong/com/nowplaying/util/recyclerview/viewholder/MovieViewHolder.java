package ngohoanglong.com.nowplaying.util.recyclerview.viewholder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vnwarriors.advancedui.appcore.common.DynamicHeightImageView;
import com.vnwarriors.advancedui.appcore.common.recyclerviewhelper.PlaceHolderDrawableHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.MovieHM;

import static ngohoanglong.com.nowplaying.R.id.wrap;

/**
 * Created by Long on 10/5/2016.
 *
 */

public class MovieViewHolder extends BaseViewHolder<MovieHM> {
    private static final String TAG = "MovieViewHolder";
    @BindView(wrap)
    CardView cardView;
    @BindView(R.id.ivBackground)
    DynamicHeightImageView imageView;
    @BindView(R.id.tvTitle)
    TextView des;

    public MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public  void bind(MovieHM item) {
        MovieHM movieVM = item;
        des.setText(movieVM.getMovie().getTitle());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View sharedView = imageView;
                ActivityOptions ops = ActivityOptions.makeSceneTransitionAnimation((Activity) itemView.getContext(),
                        sharedView,
                        itemView.getContext().getString(R.string.detail_image));
//                Navigator.navigateToDetailActivity(v.getContext(), movieVM,ops);
            }
        });
        imageView.setRatio(1.5);
//        Glide.with(itemView.getContext())
//                .load("https://image.tmdb.org/t/p/w342"+item.getMovie().getPosterPath())
//                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable())
//                .into(imageView);
        Picasso.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w342"+item.getMovie().getPosterPath())
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(item.getMovie().getId()))
                .into(imageView);


    }
}
