package com.leo.study.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.Topic;
import com.leo.study.model.Word;
import com.leo.study.ui.TopicDetailActivity;
import com.leo.study.ui.WordDetailActivity;
import com.vn.vega.adapter.multipleviewtype.BinderViewHolder;
import com.vn.vega.adapter.multipleviewtype.VegaDataBinder;
import com.vn.vega.base.holder.VegaViewHolder;

import butterknife.Bind;

/**
 * Created by binhbt on 2/28/2017.
 */
public class WordItemView  extends VegaDataBinder<Word> {
    public WordItemView(Word data) {
        super(data);
    }

    @Override
    public void bindViewHolder(BinderViewHolder holder, int position) {
        final PhotoViewHolder holder1 = (PhotoViewHolder) holder;
        holder1.title.setText(data.getMean1());
        holder1.item.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                holder1.sendEvent(new NotifyEvent(data.getId(), NotifyEvent.Type.WORD_DELETE));
                return true;
            }
        });
        holder1.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder1.title.getContext(), WordDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(Const.WORD_ID, data.getId());
                i.putExtras(bundle);
                holder1.title.getContext().startActivity(i);
            }
        });
    }

    @Override
    public BinderViewHolder newHolder(View parent) {
        return new PhotoViewHolder(parent);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_word;
    }

    static class PhotoViewHolder extends VegaViewHolder {

        @Bind(R.id.name)
        TextView title;
        @Bind(R.id.card_view)View item;

        public PhotoViewHolder(View view) {
            super(view);
        }
    }
}
