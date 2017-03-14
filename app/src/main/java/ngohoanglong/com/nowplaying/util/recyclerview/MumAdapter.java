package ngohoanglong.com.nowplaying.util.recyclerview;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ngohoanglong.com.nowplaying.util.recyclerview.holderfactory.HolderFactory;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.util.recyclerview.viewholder.BaseViewHolder;


/**
 * Created by Long on 10/5/2016.
 */

public class MumAdapter extends BaseRecyclerViewAdapter<BaseHM> {

    private Context context;
    public HolderFactory holderFactory ;

    public MumAdapter(Context context,ObservableArrayList<BaseHM> observableArrayList) {
        super(observableArrayList);
        this.context = context;
    }
    public MumAdapter(Context context, HolderFactory holderFactory, ObservableArrayList<BaseHM>  observableArrayList) {
        this(context,observableArrayList);
        this.holderFactory = holderFactory;
    }

    @Override
    public BaseViewHolder<BaseHM> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent != null) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return holderFactory.createHolder(viewType, view);
        }
        throw new RuntimeException("Parent is null");
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<BaseHM> holder, int position) {
        if(holder!=null){
            holder.bind(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getVMType(holderFactory);
    }
}
