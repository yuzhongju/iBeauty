package com.jueze.ibeauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jueze.ibeauty.BasicManualNoteActivity;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.ManualBean;
import java.util.List;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {

    private List<ManualBean> mList;
    private Context mContext;
    
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv, tv2;
        LinearLayout parent;
        public ViewHolder(View view){
            super(view);
            parent = view.findViewById(R.id.item_parent);
            tv = view.findViewById(R.id.name);
            tv2 = view.findViewById(R.id.content);
        }
    }
    
    public ManualAdapter(List<ManualBean> list){
        this.mList = list;
    }
    

    @Override
    public ManualAdapter.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        View view = LayoutInflater.from(p1.getContext()).inflate(R.layout.item_manual, p1, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManualAdapter.ViewHolder holder, int position) {

        ManualBean data = mList.get(position);
        final String title = data.getDocTitle();
        final String note = data.getDocContent();
        holder.tv.setText(title);
        holder.tv2.setText(note);
        holder.parent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    ManualBean manualBean = new ManualBean(title,note);
                    
                    Intent intent = new Intent(mContext, BasicManualNoteActivity.class);
                    intent.putExtra("data", new Gson().toJson(manualBean));
                    mContext.startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    
	public void refresh(List<ManualBean> data){
		mList.clear();
		this.mList = data;
		notifyDataSetChanged();
	}
    public void removeAll(){
        mList.clear();
        notifyDataSetChanged();
    }
}
