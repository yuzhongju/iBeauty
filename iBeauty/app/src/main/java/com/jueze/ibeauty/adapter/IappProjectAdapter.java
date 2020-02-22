package com.jueze.ibeauty.adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.IappProjectBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.util.FileHelper;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyToast;
import com.jueze.ibeauty.util.ZipUtil;
import com.jueze.ibeauty.util.dip2px;
import java.io.File;
import java.util.List;

public class IappProjectAdapter extends RecyclerView.Adapter<IappProjectAdapter.ViewHolder> {

    private static final int MSG_COMPRESS = 1;

    private Context mContext;
    private List<IappProjectBean> mDataList;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private MyProgressDialog mPd;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mParent;
        TextView mTitle, mYuv, mBm, mRemark;
        ImageView mIcon;
        View mView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            mParent = view.findViewById(R.id.item_parent);
            mTitle = view.findViewById(R.id.pro_title);
            mYuv = view.findViewById(R.id.pro_yuv);
            mBm = view.findViewById(R.id.pro_bm);
            mRemark = view.findViewById(R.id.pro_remark);
            mIcon = view.findViewById(R.id.pro_icon);
        }
    }

    public IappProjectAdapter(List<IappProjectBean> dataList) {
        this.mDataList = dataList;
    }


    @Override
    public IappProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false));
    }

    @Override
    public void onBindViewHolder(IappProjectAdapter.ViewHolder holder, int position) {
        mContext = holder.mView.getContext();
        IappProjectBean data = mDataList.get(position);

        final String title = data.getTitle();
        final String yuv = data.getYuv();
        String bm = data.getBm();
        String remark = data.getRemark();
        String icon = data.getIcon();
        final String path = data.getPath();
        if (remark.equals("")) {
            remark = "暂无备注";
        }
        holder.mTitle.setText(title);
        holder.mYuv.setText(yuv);
        holder.mBm.setText(bm);
        holder.mRemark.setText(remark);
        Bitmap bmp = BitmapFactory.decodeFile(icon);
        holder.mIcon.setImageBitmap(bmp);

        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    mContext = v.getContext();
                    View view = View.inflate(mContext, R.layout.dialog_backup_iapp, null);
                    final TextView titleView = view.findViewById(R.id.backup_title);
                    TextView pathView = view.findViewById(R.id.backup_path);
                    Button btnA = view.findViewById(R.id.backup_iapp);
                    Button btnB = view.findViewById(R.id.backup_zip);

                    String color = mContext.getString(R.color.colorPrimary);
                    MyShape.set(btnA.getBackground(), color);
                    MyShape.set(btnB.getBackground(), color);
                    titleView.setText(title + "_" + yuv);
                    pathView.setText(path);

                    builder = new AlertDialog.Builder(mContext);
                    builder.setView(view);
                    alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(dip2px.to(240), ViewGroup.LayoutParams.WRAP_CONTENT);
                    btnA.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View p1) {
                                mPd = new MyProgressDialog(mContext);
                                mPd.setMessage("压缩中...");
                                mPd.show();
                                new Thread(new Runnable(){

                                        @Override
                                        public void run() {
                                            boolean z = false;
                                            
                                            String dir = Environment.getExternalStorageDirectory() + "/iBeauty/backups/iapp/";
                                            String filename = titleView.getText() + ".iApp";
                                            String zipFileName = dir+filename;
                                            
                                            //复制到cache dir 遍历删除文件 压缩 删除cache
                                            File srcFile = new File(path);
                                            File cache = mContext.getCacheDir();
                                            String newFileName = cache+File.separator+srcFile.getName();
                                            
                                            try{

                                                FileHelper.copyDir(path,newFileName);
                                                for(File file : new File(newFileName).listFiles()){
                                                    if(yuv.equals("v3")){
                                                        if(file.getName().equals("bin")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                        if(file.getName().equals("files")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                    }
                                                    if(yuv.equals("v5")){
                                                        if(file.getName().equals("bin")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                    }
                                                }
                                                z = ZipUtil.toZip(newFileName,zipFileName);
                                            }catch(Exception e){}
                                            
                                            Message msg = mHandler.obtainMessage();
                                            msg.what = MSG_COMPRESS;
                                            msg.obj = z;
                                            mHandler.sendMessage(msg);
                                        }


                                    }).start();

                            }
                        });
                    btnB.setOnClickListener(new View.OnClickListener(){

                            @Override
                            public void onClick(View p1) {
                                mPd = new MyProgressDialog(mContext);
                                mPd.setMessage("压缩中...");
                                mPd.show();
                                new Thread(new Runnable(){

                                        @Override
                                        public void run() {
                                            

                                            boolean z = false;

                                            String dir = Environment.getExternalStorageDirectory() + "/iBeauty/backups/zip/";
                                            String filename = titleView.getText() + ".zip";
                                            String zipFileName = dir+filename;

                                            //复制到cache dir 遍历删除文件 压缩 删除cache
                                            File srcFile = new File(path);
                                            File cache = mContext.getCacheDir();
                                            String newFileName = cache+File.separator+srcFile.getName();

                                            try{

                                                FileHelper.copyDir(path,newFileName);
                                                for(File file : new File(newFileName).listFiles()){
                                                    if(yuv.equals("v3")){
                                                        if(file.getName().equals("bin")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                        if(file.getName().equals("files")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                    }
                                                    if(yuv.equals("v5")){
                                                        if(file.getName().equals("bin")){
                                                            FileHelper.deleteDir(file.getPath());
                                                        }
                                                    }
                                                }
                                                z = ZipUtil.toZip(newFileName,zipFileName);
                                            }catch(Exception e){}
                                            
                                            
                                            Message msg = mHandler.obtainMessage();
                                            msg.what = MSG_COMPRESS;
                                            msg.obj = z;
                                            mHandler.sendMessage(msg);
                                        }


                                    }).start();

                            }
                        });
                }
            });



    }

    
    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void removeAll() {
        mDataList.clear();
        notifyDataSetChanged();
    }
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COMPRESS:
                    alertDialog.dismiss();
                    boolean z = msg.obj;
                    mPd.dismiss();
                    if (z) {
                        MyToast.ts("压缩完成");
                    } else {
                        MyToast.ts("压缩失败");
                    }
                    break;
                default:

            }
        }

    };

}
