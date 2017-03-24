package ngohoanglong.com.nowplaying.display.recyclerview;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ngohoanglong.com.nowplaying.display.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.display.recyclerview.viewholder.BaseViewHolder;
import ngohoanglong.com.nowplaying.display.recyclerview.holderfactory.HolderFactory;


/**
 * Created by Long on 10/5/2016.
 */

public class MumAdapter extends BaseRecyclerViewAdapter<BaseHM> {

    private Context context;
    public HolderFactory holderFactory ;
    OnSelectItemClickEvent onSelectItemClickEvent;


    public MumAdapter(Context context,ObservableArrayList<BaseHM> observableArrayList) {
        super(observableArrayList);
        this.context = context;
    }
    public MumAdapter(Context context, HolderFactory holderFactory,
                      ObservableArrayList<BaseHM>  observableArrayList) {
        this(context,observableArrayList);
        this.holderFactory = holderFactory;
    }
    public MumAdapter(Context context, HolderFactory holderFactory,
                      ObservableArrayList<BaseHM>  observableArrayList,
                      OnSelectItemClickEvent onSelectItemClickEvent) {
        this(context,holderFactory,observableArrayList);
        this.onSelectItemClickEvent = onSelectItemClickEvent;
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
            BaseHM baseHM = items.get(position);
            holder.bind(baseHM);
            if(onSelectItemClickEvent!=null){
                holder.itemView.setOnClickListener(v -> {
                    onSelectItemClickEvent.onItemClick(position, baseHM);
                });
            }
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

    public interface OnSelectItemClickEvent {
        void onItemClick(int pos, BaseHM baseHM);
    }
}
