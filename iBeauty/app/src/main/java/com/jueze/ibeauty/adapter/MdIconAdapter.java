package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.MdIconBean;
import com.jueze.ibeauty.util.MyShape;
import java.util.List;
import java.io.File;
import android.os.Environment;
import com.jueze.ibeauty.util.FileHelper;
import com.jueze.ibeauty.util.MyToast;
public class MdIconAdapter extends RecyclerView.Adapter<MdIconAdapter.ViewHolder> {

    private List<MdIconBean> mIconDataList;
    private Context mContext;
    private BottomSheetDialog mBsd;
    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mParent;
        ImageView mIcon;
        TextView mName;

        public ViewHolder(View view) {
            super(view);
            mParent = view.findViewById(R.id.item_parent);
            mIcon = view.findViewById(R.id.md_icon_img);
            mName = view.findViewById(R.id.md_icon_name);
        }
    }
    public MdIconAdapter(List<MdIconBean> list) {
        this.mIconDataList = list;
    }

    @Override
    public MdIconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_icons, parent, false));
    }

    @Override
    public void onBindViewHolder(MdIconAdapter.ViewHolder holder, int position) {
        MdIconBean data = mIconDataList.get(position);
        String name = data.getIconName();
        final String path = data.getIconPath();
        final String bgColor = data.getBgColor();
        String nameColor = data.getNameColor();
        holder.mParent.setBackgroundColor(Color.parseColor(bgColor));
        holder.mIcon.setImageBitmap(BitmapFactory.decodeFile(path));
        holder.mName.setText(name);
        holder.mName.setTextColor(Color.parseColor(nameColor));
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    showDialog(bgColor, path);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mIconDataList.size();
    }

    public void removeAll() {
        mIconDataList.clear();
        notifyDataSetChanged();
    }

    private void showDialog(String bgColor, final String iconPath) {
        View view = View.inflate(mContext, R.layout.dialog_mdicons, null);
        RelativeLayout bg = view.findViewById(R.id.mdicon_bg_img);
        ImageView img = view.findViewById(R.id.mdicon_img);
        TextView toIapp = view.findViewById(R.id.mdicon_export_iapp);
        TextView toOther = view.findViewById(R.id.mdicon_export_other);

        MyShape.set(view, 15, 15, 0, 0, "#ffffff");
        MyShape.set(bg,10,10,10,10,bgColor);
        MyShape.set(toIapp,10,0,0,10,1,"#ffffff","#22000000");
        MyShape.set(toOther,0,10,10,0,1,"#ffffff","#22000000");
        img.setImageBitmap(BitmapFactory.decodeFile(iconPath));

        mBsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
        mBsd.setContentView(view);
        mBsd.show();

        toIapp.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String filename = new File(iconPath).getName();
                    String dir = Environment.getExternalStorageDirectory()+"/iApp/Userimg/";
                    if(FileHelper.copyFile(iconPath,dir+filename)){
                        MyToast.ts("导出成功");
                    }else{
                        MyToast.ts("到处失败");
                    }
                }
            });
        toOther.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String filename = new File(iconPath).getName();
                    String dir = Environment.getExternalStorageDirectory()+"/Download/";
                    if(FileHelper.copyFile(iconPath,dir+filename)){
                        MyToast.ts("导出成功");
                    }else{
                        MyToast.ts("到处失败");
                    }
                }
            });

    }

}
