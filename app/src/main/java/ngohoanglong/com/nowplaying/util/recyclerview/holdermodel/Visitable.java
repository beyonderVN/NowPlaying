package ngohoanglong.com.nowplaying.util.recyclerview.holdermodel;


import ngohoanglong.com.nowplaying.util.recyclerview.holderfactory.ViewTypeFactory;

/**
 * Created by Long on 10/5/2016.
 */

public interface Visitable {
    int getVMType(ViewTypeFactory vmTypeFactory);
}
