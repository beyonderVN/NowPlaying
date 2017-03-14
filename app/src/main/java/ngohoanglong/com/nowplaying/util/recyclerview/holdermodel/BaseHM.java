package ngohoanglong.com.nowplaying.util.recyclerview.holdermodel;



import java.io.Serializable;

/**
 * Created by Long on 10/7/2016.
 */

public abstract class BaseHM implements Serializable, Visitable {
    private boolean fullSpan = false;
    boolean isCheck = false;
    public boolean isFullSpan() {
        return fullSpan;
    }

    public void setFullSpan(boolean fullSpan) {
        this.fullSpan = fullSpan;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
