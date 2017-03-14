package ngohoanglong.com.nowplaying.util.binding;

import android.databinding.BindingAdapter;
import android.widget.EditText;
import android.widget.ViewAnimator;

import ngohoanglong.com.nowplaying.util.binding.rxview.TextChange;
import ngohoanglong.com.nowplaying.util.binding.rxview.TextChangeAdapter;


/**
 * Created by nongdenchet on 11/22/16.
 */

public class BindingUtils {

    @BindingAdapter("textChange")
    public static void textChange(final EditText editText, final TextChange textChange) {
        editText.addTextChangedListener(new TextChangeAdapter() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textChange.onChange(charSequence.toString());
            }
        });
    }

    @BindingAdapter("pageState")
    public static void setStateViewAnimator(final ViewAnimator viewAnimator, final int state) {
        viewAnimator.setDisplayedChild(state);
    }
}
