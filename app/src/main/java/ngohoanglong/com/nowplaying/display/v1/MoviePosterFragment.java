/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ngohoanglong.com.nowplaying.display.v1;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;
import com.vnwarriors.advancedui.appcore.common.DynamicHeightImageView;
import com.vnwarriors.advancedui.appcore.common.recyclerviewhelper.PlaceHolderDrawableHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.R;
import ngohoanglong.com.nowplaying.data.model.Movie;

import static android.content.ContentValues.TAG;


/**
 * Fragment implementation created to show a poster inside an ImageView widget.
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
public class MoviePosterFragment extends Fragment {

    @BindView(R.id.iv_thumbnail)
    DynamicHeightImageView thumbnailImageView;

    @Nullable
    @BindView(R.id.tvOverview)
    TextView tvOverview;
    @Nullable
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @Nullable
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @Nullable
    @BindView(R.id.tvPopularity)
    TextView tvPopularity;
    @Nullable
    @BindView(R.id.srbStar)
    SimpleRatingBar srbStart;


    private Movie movie;

    /**
     * Override method used to initialize the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_poster, container, false);
        ButterKnife.bind(this, view);
        if(movie!=null){
            setDetail(movie);
        }

        return view;
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }
    public void setDetail(Movie movie) {
        this.movie = movie;
        thumbnailImageView.setRatio(1.5);
        Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w342"+movie.getPosterPath())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(movie.getId()))
                    .into(thumbnailImageView);

        tvOverview.setText(movie.getOverview());
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(Html.fromHtml("<font color=\"BLUE\"><b>Release Date: </b></font>"+movie.getReleaseDate()+""));
        tvPopularity.setText(Html.fromHtml("<font color=\"BLUE\"><b>Popularity: </b></font>"+movie.getPopularity()+""));
        Log.d(TAG, "movieVM.getMovie().getVoteAverage() "+movie.getVoteAverage());

        SimpleRatingBar.AnimationBuilder builder = srbStart.getAnimationBuilder()
                .setRepeatCount(0)
                .setRepeatMode(ValueAnimator.INFINITE)
                .setInterpolator(new BounceInterpolator())
                .setRatingTarget(movie.getVoteAverage())
                ;
        builder.start();
    }
}