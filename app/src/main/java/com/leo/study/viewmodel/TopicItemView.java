package com.leo.study.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.Topic;
import com.leo.study.ui.TopicDetailActivity;
import com.squareup.picasso.Picasso;
import com.vn.vega.adapter.multipleviewtype.BinderViewHolder;
import com.vn.vega.adapter.multipleviewtype.VegaDataBinder;
import com.vn.vega.base.holder.VegaViewHolder;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

/**
 * Created by binhbt on 2/28/2017.
 */
public class TopicItemView extends VegaDataBinder<Topic> {
    public TopicItemView(Topic data) {
        super(data);
    }

    @Override
    public void bindViewHolder(BinderViewHolder holder, int position) {
        final PhotoViewHolder holder1 = (PhotoViewHolder) holder;
        holder1.title.setText(data.getName());
        holder1.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder1.title.getContext(), TopicDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.TOPIC_ID, data.getId());
                i.putExtras(bundle);
                holder1.title.getContext().startActivity(i);
            }
        });
        holder1.item.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                holder1.sendEvent(new NotifyEvent(data.getId(), NotifyEvent.Type.TOPIC_DELETE));
                return true;
            }
        });
        holder1.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    holder1.sendEvent(new NotifyEvent(data, NotifyEvent.Type.TOPIC_SELECTED));
                }else{
                    holder1.sendEvent(new NotifyEvent(data, NotifyEvent.Type.TOPIC_REMOVED));
                }

            }
        });
    }

    @Override
    public BinderViewHolder newHolder(View parent) {
        return new PhotoViewHolder(parent);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_topic;
    }

    static class PhotoViewHolder extends VegaViewHolder {

        @Bind(R.id.name)
        TextView title;
        @Bind(R.id.card_view)View item;
        @Bind(R.id.checkBox)CheckBox checkBox;
        public PhotoViewHolder(View view) {
            super(view);
        }
    }
}
