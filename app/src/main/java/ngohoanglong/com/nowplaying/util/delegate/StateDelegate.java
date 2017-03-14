package ngohoanglong.com.nowplaying.util.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Serializable;

import ngohoanglong.com.nowplaying.util.mvvm.PostViewModel;


/**
 * Created by Long on 2/27/2017.
 */

public abstract class StateDelegate<M extends BaseStateViewModel, S extends BaseState> extends BaseDelegate {
    private static final String TAG = "StateDelegate";
    private M viewModel;
    private S state;
    private String presentationModelKey;
    private static final String EXTRA_VIEW_MODEL_STATE = "viewModelState";

    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.viewModel = createViewModel();
        presentationModelKey = viewModel.getClass().getCanonicalName() + EXTRA_VIEW_MODEL_STATE;
        state = restorePresentationModel(getClass(), savedInstanceState);
        if (state == null) {
            state = createStateModel();
        }
        viewModel.returnInstanceState(state);
    }

    /**
     * Used by the delegate to persist current Presentation Model due to configuration change.
     *
     * @param outState
     */
    public void onSaveInstanceState(Bundle outState) {
        state = (S) viewModel.saveInstanceState();
//        Serializable potentialPresentationModel =
//                outState.getSerializable(presentationModelKey);
//        Log.d(TAG, "onSaveInstanceState: "+potentialPresentationModel);
        outState.putSerializable(presentationModelKey, state);
        if(state instanceof PostViewModel.PostsState) {
            Log.d(TAG, "onSaveInstanceState: "+presentationModelKey+": "+((PostViewModel.PostsState) state).getBaseHMs().size());
        }

    }



    public M getViewModel() {
        return viewModel;
    }

    public S getState() {
        return state;
    }

    @NonNull
    protected abstract M createViewModel();

    @NonNull
    protected abstract S createStateModel();

    private S restorePresentationModel(Class stateViewClass, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Serializable potentialPresentationModel =
                    savedInstanceState.getSerializable(presentationModelKey);
            try {
                return (S) potentialPresentationModel;
            } catch (ClassCastException ex) {
                throw new IllegalStateException(String.format(
                        "We expected a state saved in the bundle under the key: \"%s\", but was: %s",
                        presentationModelKey, potentialPresentationModel.toString()));
            }
        }
        return null;
    }
}
