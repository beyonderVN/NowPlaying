package ngohoanglong.com.nowplaying.util.recyclerview.viewholder;

import android.animation.ValueAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.vnwarriors.advancedui.appcore.common.DynamicHeightImageView;
import com.vnwarriors.advancedui.appcore.common.recyclerviewhelper.PlaceHolderDrawableHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.TrailerMovieHM;

/**
 * Created by Long on 10/12/2016.
 *
 */
public class MovieTrailerItemHolder extends BaseViewHolder<TrailerMovieHM> {


    @BindView(R.id.ivBackground)
    DynamicHeightImageView imageView;
    @BindView(R.id.tvTitle)
    TextView des;
    @BindView(R.id.srbStar)
    SimpleRatingBar srbStart;

    public MovieTrailerItemHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void bind(TrailerMovieHM item) {
        TrailerMovieHM movieVM = item;
        des.setText(movieVM.getMovie().getTitle());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageView.setRatio(0.6);

        Picasso.with(itemView.getContext()).load("https://image.tmdb.org/t/p/w342"+movieVM.getMovie().getBackdropPath())
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(movieVM.getMovie().getId()))
                .into(imageView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Navigator.navigateToYTPlayActivity(v.getContext(),movieVM.getMovie().getId());
            }
        });
        SimpleRatingBar.AnimationBuilder builder = srbStart.getAnimationBuilder()
                .setRepeatCount(0)
                .setRepeatMode(ValueAnimator.INFINITE)
                .setInterpolator(new BounceInterpolator())

                .setRatingTarget(movieVM.getMovie().getVoteAverage())

                ;
        builder.start();
        if(item.isFullSpan()){
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
        }

    }
}
