package com.jueze.ibeauty.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jueze.ibeauty.BasicManualNoteActivity;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.ManualBean;
import com.jueze.ibeauty.util.MyString;
import java.util.ArrayList;
import java.util.List;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> implements Filterable {

    private List<ManualBean> mList;
	private List<ManualBean> filterList;
	private String queryString;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv2;
        LinearLayout parent;
        public ViewHolder(View view) {
            super(view);
            parent = view.findViewById(R.id.item_parent);
            tv = view.findViewById(R.id.name);
            tv2 = view.findViewById(R.id.content);
        }
    }

    public ManualAdapter(Context context, List<ManualBean> list) {
		this.mContext = context;
        this.mList = list;
		this.filterList = list;
    }


    @Override
    public ManualAdapter.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
        View view = LayoutInflater.from(p1.getContext()).inflate(R.layout.item_manual, p1, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManualAdapter.ViewHolder holder, int position) {

        ManualBean data = filterList.get(position);
        final String title = data.getDocTitle();
        final String note = data.getDocContent();

		if (!TextUtils.isEmpty(queryString)) {
			SpannableString titleStr = MyString.highlightStr(Color.RED, title, queryString);
			SpannableString noteStr = MyString.highlightStr(Color.RED, note, queryString);
			holder.tv.setText(titleStr);
			holder.tv2.setText(noteStr);
		} else {
			holder.tv.setText(title);
			holder.tv2.setText(note);
		}
        holder.parent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    ManualBean manualBean = new ManualBean(title, note);

                    Intent intent = new Intent(mContext, BasicManualNoteActivity.class);
                    intent.putExtra("data", new Gson().toJson(manualBean));
                    mContext.startActivity(intent);
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
					filterList = mList;
				} else {
					List<ManualBean> filteredList = new ArrayList<>();
					for (int i=0; i < mList.size(); i++) {
						if (mList.get(i).getDocTitle().contains(queryString) || mList.get(i).getDocContent().contains(queryString)) {
							filteredList.add(mList.get(i));
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
				filterList = (List<ManualBean>) p2.values;
				notifyDataSetChanged();
			}
		};
	}


}
