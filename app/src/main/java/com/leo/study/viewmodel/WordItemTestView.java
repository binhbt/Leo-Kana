package com.leo.study.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.leo.study.Const;
import com.leo.study.R;
import com.leo.study.event.NotifyEvent;
import com.leo.study.model.TestOption;
import com.leo.study.model.Word;
import com.leo.study.ui.WordDetailActivity;
import com.vn.vega.adapter.multipleviewtype.BinderViewHolder;
import com.vn.vega.adapter.multipleviewtype.VegaDataBinder;
import com.vn.vega.base.holder.VegaViewHolder;

import org.w3c.dom.Text;

import butterknife.Bind;

/**
 * Created by binhbt on 3/1/2017.
 */
public class WordItemTestView   extends VegaDataBinder<Word> {
    public WordItemTestView(Word data) {
        super(data);
    }

    @Override
    public void bindViewHolder(BinderViewHolder holder, int position) {
        final PhotoViewHolder holder1 = (PhotoViewHolder) holder;
        String txt = data.getMean2();
        if (txt != null && txt.length() >0){
            if (data.getMean3() != null && data.getMean3().length() >0){
                txt+=" - "+ data.getMean3();
            }
        }else{
            txt = data.getMean2();
        }
        if (data.getOption() == TestOption.ENVI){
            txt = data.getMean1();
        }
        holder1.title.setText(txt);
        holder1.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder1.sendEvent(new NotifyEvent(data, NotifyEvent.Type.WORD_TEST_SELECTED));
            }
        });
    }

    @Override
    public BinderViewHolder newHolder(View parent) {
        return new PhotoViewHolder(parent);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.item_test_word;
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
