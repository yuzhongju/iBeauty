package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.PackSourceBean;
import com.jueze.ibeauty.util.FileUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ShareUtil;
import com.jueze.ibeauty.util.ToastUtil;
import com.jueze.ibeauty.util.DisplayUtil;
import java.util.List;

public class PackedAdapter extends RecyclerView.Adapter<PackedAdapter.ViewHolder> {


    private Context mContext;
    private List<PackSourceBean> mDataList;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    public PackedAdapter(List<PackSourceBean> data) {
        this.mDataList = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mParent;
        TextView mTitle;
        TextView mSize;
        public ViewHolder(View view) {
            super(view);
            mParent = view.findViewById(R.id.item_parent);
            mTitle = view.findViewById(R.id.pack_src_title);
            mSize = view.findViewById(R.id.pack_src_size);
        }
    }

    @Override
    public PackedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pack_src, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PackedAdapter.ViewHolder holder, int position) {
        PackSourceBean data = mDataList.get(position);
        final String title = data.getTitle();
        String size = data.getSize();
        final String path = data.getPath();
        final int wz = holder.getPosition();

        holder.mTitle.setText(title);
        holder.mSize.setText(size);
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    onAction(title, path, wz);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void remove(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    public void removeAll() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public void setData(List<PackSourceBean> data) {
        this.mDataList = data;
    }


    private void onAction(final String zipFileName, final String zipFilePath, final int position) {
        View view = View.inflate(mContext, R.layout.dialog_packed_src, null);
        TextView packedTxt = view.findViewById(R.id.packed_txt);
        Button shareBtn = view.findViewById(R.id.packed_share);
        Button delBtn = view.findViewById(R.id.packed_delete);

        String color = mContext.getString(R.color.colorPrimary);
        ShapeUtil.set(shareBtn.getBackground(), color);
        ShapeUtil.set(delBtn.getBackground(), color);
        packedTxt.setText(zipFileName);

        builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setLayout(DisplayUtil.dip2px(240), ViewGroup.LayoutParams.WRAP_CONTENT);

        shareBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    ShareUtil.share(mContext, zipFilePath);
                }
            });
        delBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    if (FileUtil.deleteFile(zipFilePath)) {
                        remove(position);
                        ToastUtil.show("删除成功");
                    } else {
                        ToastUtil.show("删除失败");
                    }
                }
            });
    }
}
