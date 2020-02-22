package com.jueze.ibeauty.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.BookMarkBean;
import com.jueze.ibeauty.util.MyShape;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.jueze.ibeauty.util.MyClipBoard;
import com.jueze.ibeauty.util.MyToast;
import android.content.Intent;
import android.net.Uri;

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
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false));
    }

    @Override
    public void onBindViewHolder(BookMarkAdapter.ViewHolder holder, int position) {
        BookMarkBean.BookMark data = mData.get(position);
        final String name = data.getName();
        final String url = data.getUrl();
        holder.mTitle.setText(name);
        holder.mUrl.setText(url);
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    showMark(name,url);
                }
        });
    }
    
    private void showMark(final String title, final String url){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(url);
        builder.setPositiveButton("复制", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    MyClipBoard.write(url);
                    MyToast.ts("已写入剪切板");
                }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                }
            });
        builder.setNeutralButton("浏览器打开", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface p1, int p2) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    mContext.startActivity(intent);
                }
            });

        final AlertDialog dialog = builder.create();
        dialog.show();

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
