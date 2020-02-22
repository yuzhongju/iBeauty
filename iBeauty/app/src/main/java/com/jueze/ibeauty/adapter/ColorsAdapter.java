package com.jueze.ibeauty.adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.ColorsBean;
import com.jueze.ibeauty.util.MyClipBoard;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyToast;
import java.util.ArrayList;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ViewHolder> {

    
    private ArrayList<ColorsBean> mDataList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mParent;
        ImageView mIv;
        TextView mLevel, mColor;
        public ViewHolder(View view){
            super(view);
            mParent = view.findViewById(R.id.item_parent);
            mIv = view.findViewById(R.id.icon);
            mLevel = view.findViewById(R.id.level);
            mColor = view.findViewById(R.id.color);
        }
    }
    
    public ColorsAdapter(ArrayList<ColorsBean> dataList){
        this.mDataList = dataList;
    }
    

    @Override
    public ColorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colors,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorsAdapter.ViewHolder holder, int position) {
        ColorsBean data = mDataList.get(position);
        String level = data.getLevel();
        final String color = data.getColor();
        holder.mLevel.setText(level);
        holder.mColor.setText(color);
        MyShape.set(holder.mIv,100,100,100,100,color);
        holder.mParent.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View view) {
                    MyClipBoard.write(color);
                    MyToast.ts("已写入剪切板");
                }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    
}
