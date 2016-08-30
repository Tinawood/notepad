package com.raisound.asrdemo_en.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raisound.asrdemo_en.R;

import java.util.ArrayList;

/**
 * Created by maolin on 2016/3/23.
 */
public class ChatMsgAdapter extends RecyclerView.Adapter<ChatMsgAdapter.MyHolder> {
    private Context mContext;
    private ArrayList<String> mMsgs;
    private LayoutInflater mInflater;

    public ChatMsgAdapter(Context context, ArrayList<String> list){
        mContext = context;
        mMsgs = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = null;
        View view = mInflater.inflate(R.layout.adapter_chart, parent, false);
        holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mChartMsgTv.setText(mMsgs.get(position));
    }

    @Override
    public int getItemCount() {
        return mMsgs.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        public TextView mChartMsgTv;

        public MyHolder(View itemView) {
            super(itemView);
            mChartMsgTv = (TextView) itemView.findViewById(R.id.chart_detail_tv);
        }
    }
}
