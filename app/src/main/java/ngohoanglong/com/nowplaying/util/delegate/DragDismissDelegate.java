package ngohoanglong.com.nowplaying.util.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.vnwarriors.advancedui.appcore.common.ElasticDragDismissFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import ngohoanglong.com.nowplaying.R;


/**
 * Created by Long on 11/15/2016.
 */

public class DragDismissDelegate extends BaseDelegate {
    @BindView(R.id.activity_detail)
    ElasticDragDismissFrameLayout draggableFrame;
    Activity activity;

    public DragDismissDelegate(Activity activity) {
        this.activity = activity;
    }

    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    @Override
    public void onCreate(Bundle bundle) {
        ButterKnife.bind(this, activity);
        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(activity);
        draggableFrame.addListener(
                new ElasticDragDismissFrameLayout.SystemChromeFader(activity) {
                    @Override
                    public void onDragDismissed() {
                        ((FragmentActivity) activity).supportFinishAfterTransition();
                    }
                });
    }

    @Override
    public void onResume() {
        draggableFrame.addListener(chromeFader);

    }

    @Override
    public void onPause() {
        draggableFrame.removeListener(chromeFader);
    }

}
