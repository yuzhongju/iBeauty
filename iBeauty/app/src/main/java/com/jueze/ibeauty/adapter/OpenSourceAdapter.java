package com.jueze.ibeauty.adapter;
import android.widget.ArrayAdapter;
import com.jueze.ibeauty.bean.OpenSourceBean;
import android.content.Context;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.jueze.ibeauty.R;
import android.widget.TextView;

public class OpenSourceAdapter extends ArrayAdapter<OpenSourceBean>{
    private int itemID;
    public OpenSourceAdapter(Context context, int itemID, List<OpenSourceBean> list){
        super(context, itemID, list);
        this.itemID = itemID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpenSourceBean data = getItem(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(itemID, parent, false);
        TextView mTitle = view.findViewById(R.id.open_source_title);
        TextView mNot = view.findViewById(R.id.open_source_note);
        mTitle.setText(data.getTitle());
        mNot.setText(data.getNote());
        
        return view;
    }
    
    
}
