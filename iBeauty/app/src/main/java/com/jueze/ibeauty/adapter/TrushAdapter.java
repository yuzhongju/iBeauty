package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.TrushBean;
import java.util.List;

public class TrushAdapter extends ArrayAdapter<TrushBean.Data> {
    private TextView trushName, trushCate, trushRemark;
    private int resId;
    public TrushAdapter(Context context, int resId, List<TrushBean.Data> list) {
        super(context, resId, list);
        this.resId = resId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrushBean.Data data = getItem(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        trushName = view.findViewById(R.id.trush_name);
        trushCate = view.findViewById(R.id.trush_cate);
        trushRemark = view.findViewById(R.id.trush_remark);

        trushName.setText(data.getName());
        trushCate.setText(data.getCategory());
        String remark = data.getRemark();
        if (remark.equals("")) {
            remark = "无";
        }
        trushRemark.setText(remark);
        return view;
    }


}
