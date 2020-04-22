package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.BrowserActivity;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.BookMarkBean;
import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {


    
    private Context mContext;
    private List<BookMarkBean.BookMark> mData;
    
    public BookMarkAdapter(List<BookMarkBean.BookMark> data){
        this.mData = data;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder{
        
        LinearLayout mParent;
        TextView mTitle, mUrl;
        public ViewHolder(View view){
            super(view);
            mParent = view.findViewById(R.id.item_parent);
            mTitle = view.findViewById(R.id.mark_title);
            mUrl = view.findViewById(R.id.mark_url);
        }
    }
    
    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false));
    }

    @Override
    public void onBindViewHolder(BookMarkAdapter.ViewHolder holder, int position) {
        BookMarkBean.BookMark data = mData.get(position);
        final String title = data.getName();
        final String url = data.getUrl();
        holder.mTitle.setText(title);
        holder.mUrl.setText(url);
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
					BrowserActivity.actionStart(mContext, title, url);
                }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    
    
    public void removeAll(){
        mData.clear();
        notifyDataSetChanged();
    }
}
