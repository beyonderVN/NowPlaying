package ngohoanglong.com.nowplaying.util.recyclerview;

import android.content.Context;
import android.databinding.ObservableArrayList;

import ngohoanglong.com.nowplaying.util.recyclerview.holderfactory.HolderFactory;
import ngohoanglong.com.nowplaying.util.recyclerview.holdermodel.BaseHM;
import ngohoanglong.com.nowplaying.util.recyclerview.viewholder.BaseViewHolder;


/**
 * Created by Long on 10/5/2016.
 */

public class SingleSelectedMumAdapter extends MumAdapter {


    private int selectedPosition = -1;
    OnSelectItemClickEvent onSelectItemClickEvent;
    public SingleSelectedMumAdapter(Context context,
                                    HolderFactory holderFactory,
                                    ObservableArrayList<BaseHM> observableArrayList,
                                    OnSelectItemClickEvent onSelectItemClickEvent) {
        super(context, holderFactory,observableArrayList);
        this.onSelectItemClickEvent = onSelectItemClickEvent;
    }
    @Override
    public void onBindViewHolder(BaseViewHolder<BaseHM> holder, int position) {
        if(holder!=null){
            BaseHM baseHM = items.get(position);
            baseHM.setCheck(position == selectedPosition);
            holder.bind(baseHM);
            holder.itemView.setOnClickListener(v -> {
                selectedPosition=position;
                onSelectItemClickEvent.onItemClick(position, baseHM);
                notifyDataSetChanged();

            });
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public interface OnSelectItemClickEvent {
        void onItemClick(int pos, BaseHM baseHM);
    }
}
