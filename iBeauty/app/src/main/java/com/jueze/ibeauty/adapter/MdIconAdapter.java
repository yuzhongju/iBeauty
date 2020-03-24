package com.jueze.ibeauty.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.MdIconBean;
import com.jueze.ibeauty.util.FileUtil;
import com.jueze.ibeauty.util.ShapeUtil;
import com.jueze.ibeauty.util.ToastUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.jueze.ibeauty.util.MyString;
public class MdIconAdapter extends RecyclerView.Adapter<MdIconAdapter.ViewHolder> implements Filterable{

    private List<MdIconBean> mIconDataList, filterList;
	private String queryString;
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
    public MdIconAdapter(Context context, List<MdIconBean> list) {
		this.mContext = context;
        this.mIconDataList = list;
		this.filterList = list;
    }

    @Override
    public MdIconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_md_icons, parent, false));
    }

    @Override
    public void onBindViewHolder(MdIconAdapter.ViewHolder holder, int position) {
        MdIconBean data = filterList.get(position);
        String name = data.getIconName();
        final String path = data.getIconPath();
        final String bgColor = data.getBgColor();
        String nameColor = data.getNameColor();
        holder.mParent.setBackgroundColor(Color.parseColor(bgColor));
        holder.mIcon.setImageBitmap(BitmapFactory.decodeFile(path));
		
		if(TextUtils.isEmpty(queryString)){
			holder.mName.setText(name);
		}else{
			holder.mName.setText(MyString.highlightStr(Color.RED, name, queryString));
		}
		
        holder.mName.setTextColor(Color.parseColor(nameColor));
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    showDialog(bgColor, path);
                }
            });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


	@Override
	public Filter getFilter() {
		return new Filter(){

			@Override
			protected Filter.FilterResults performFiltering(CharSequence p1) {
				queryString = p1.toString();
				if (TextUtils.isEmpty(queryString)) {
					filterList = mIconDataList;
				} else {
					List<MdIconBean> filteredList = new ArrayList<>();
					for (int i=0; i < mIconDataList.size(); i++) {
						if (mIconDataList.get(i).getIconName().toLowerCase().contains(queryString.toLowerCase())) {
							filteredList.add(mIconDataList.get(i));
						}
					}
					filterList = filteredList;
				}
				FilterResults filterRes = new FilterResults();
				filterRes.values = filterList;
				return filterRes;
			}

			@Override
			protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
				filterList = (List<MdIconBean>) p2.values;
				notifyDataSetChanged();
			}
		};
	}

    private void showDialog(String bgColor, final String iconPath) {
        View view = View.inflate(mContext, R.layout.dialog_mdicons, null);
        RelativeLayout bg = view.findViewById(R.id.mdicon_bg_img);
        ImageView img = view.findViewById(R.id.mdicon_img);
        TextView toIapp = view.findViewById(R.id.mdicon_export_iapp);
        TextView toOther = view.findViewById(R.id.mdicon_export_other);

        ShapeUtil.set(view, 15, 15, 0, 0, "#ffffff");
        ShapeUtil.set(bg,10,10,10,10,bgColor);
        ShapeUtil.set(toIapp,10,0,0,10,1,"#ffffff","#22000000");
        ShapeUtil.set(toOther,0,10,10,0,1,"#ffffff","#22000000");
        img.setImageBitmap(BitmapFactory.decodeFile(iconPath));

        mBsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
        mBsd.setContentView(view);
        mBsd.show();

        toIapp.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String filename = new File(iconPath).getName();
                    String dir = Environment.getExternalStorageDirectory()+"/iApp/Userimg/";
                    if(FileUtil.copyFile(iconPath,dir+filename)){
                        ToastUtil.show("导出成功");
                    }else{
                        ToastUtil.show("到处失败");
                    }
                }
            });
        toOther.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    String filename = new File(iconPath).getName();
                    String dir = Environment.getExternalStorageDirectory()+"/Download/";
                    if(FileUtil.copyFile(iconPath,dir+filename)){
                        ToastUtil.show("导出成功");
                    }else{
                        ToastUtil.show("到处失败");
                    }
                }
            });
    }
}
