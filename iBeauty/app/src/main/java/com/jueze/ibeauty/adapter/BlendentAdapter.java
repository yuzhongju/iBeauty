package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.BlendentBean;
import com.jueze.ibeauty.bean.ColorsBean;
import com.jueze.utils.ShapeUtil;
import java.util.ArrayList;

public class BlendentAdapter extends RecyclerView.Adapter<BlendentAdapter.ViewHolder> {

    private ArrayList<BlendentBean> dataList;
    private Context mContext;
    private BottomSheetDialog bsd;
    private ColorsAdapter adapter;

    private ArrayList<ColorsBean> mColorsList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mParent;
        ImageView mIv;
        TextView mName, mLevel, mColor;
        public ViewHolder(View view) {
            super(view);
            mIv = view.findViewById(R.id.icon);
            mParent = view.findViewById(R.id.item_parent);
            mName = view.findViewById(R.id.name);
            mLevel = view.findViewById(R.id.level);
            mColor = view.findViewById(R.id.color);
        }
    }

    public BlendentAdapter(ArrayList<BlendentBean> list) {
        this.dataList = list;
    }

    @Override
    public BlendentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int p2) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blendent, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BlendentAdapter.ViewHolder holder, int position) {
        BlendentBean data = dataList.get(position);
        final String name = data.getName();
        String level = String.valueOf(data.getLevel());
        String color = data.getColor();
        final String colors = data.getColors();
        holder.mName.setText(name);
        holder.mLevel.setText(level);
        holder.mColor.setText(color);
        ShapeUtil.set(holder.mIv, 100, 100, 100, 100, color);

        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    handleColors(name, colors);
                }
            });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void removeAll() {
        dataList.clear();
        notifyDataSetChanged();
    }

    private void handleColors(String name, String colors) {

        mColorsList = new ArrayList<>();

        String[] colorList = colors.split("\\|");
        for (String data : colorList) {
            data = data.trim();
            String level = data.substring(0, data.indexOf(" "));
            String color = data.substring(data.indexOf(" ") + 1);
            mColorsList.add(new ColorsBean(level, color));
        }


        View dialogView = View.inflate(mContext, R.layout.dialog_blendent, null);
        ShapeUtil.set(dialogView, 15, 15, 0, 0, "#ffffff");
        TextView mTitle = dialogView.findViewById(R.id.title);
        RecyclerView mRv = dialogView.findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRv.setLayoutManager(lm);
        adapter = new ColorsAdapter(mColorsList);
        mRv.setAdapter(adapter);

        bsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
        mTitle.setText(name);

        bsd.setContentView(dialogView);
        bsd.show();
    }
}
