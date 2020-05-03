package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static com.example.myapplication.R.id.tv_delete;
import static com.example.myapplication.R.id.tv_record;
import static com.example.myapplication.R.id.tv_record_data;


public class SeachRecordAdapter extends BaseRecycleAdapter<String> {
    public SeachRecordAdapter(List<String> datas, Context mContext) {
        super(datas, mContext);
    }

    @Override
    protected void bindData(final BaseRecycleAdapter.BaseViewHolder holder, final int position) {

        Button textView= (Button) holder.getView(R.id.tv_record);
        textView.setText(datas.get(position));

        holder.getView(tv_record).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null!=mRvItemOnclickListener){
                    mRvItemOnclickListener.RvItemOnclick(position);
                }
            }
        });
        holder.getView(tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null!=mRvItemOnclickListener_del){
                    mRvItemOnclickListener_del.RvItemOnclick_del(position);
                }
            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.search_item;
    }
}