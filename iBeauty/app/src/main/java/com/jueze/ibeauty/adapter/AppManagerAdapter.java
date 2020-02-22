package com.jueze.ibeauty.adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jueze.ibeauty.R;
import com.jueze.ibeauty.bean.AppDataBean;
import com.jueze.ibeauty.dialog.MyProgressDialog;
import com.jueze.ibeauty.util.FileHelper;
import com.jueze.ibeauty.util.MyClipBoard;
import com.jueze.ibeauty.util.MyShape;
import com.jueze.ibeauty.util.MyShare;
import com.jueze.ibeauty.util.MyToast;
import java.util.List;
import com.jueze.ibeauty.util.Mylog;

public class AppManagerAdapter extends RecyclerView.Adapter<AppManagerAdapter.ViewHolder> {

    private List<AppDataBean> mAppList;
    private Context mContext;
    private Activity mActivity;
    private BottomSheetDialog mBsd;
    private MyProgressDialog mPd;
    
    private ImageView mAppicon, mAppshare;
    private TextView mAppname, mAppbm, mAppqdl, mAppbb, mAppbbh, mAppuid, mAppflags, mAppfid, mApplud, mAppsjml, mAppywjml;
    private Button mAppqc;

    private String appName;
    private String packageName;
    private Drawable appIcon;
    private String appSize;
    private int versionCode;
    private String versionName;

    private String firstInstallDate;
    private String lastUpdateDate;

    private int targetSdkVersion;
    private int minSdkVersion;
    private String dataDir;
    private String sourceDir;
    private int uid;
    private int flags;

    private String mainActivity;
    
    
    
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 1:
                    mPd.dismiss();
                    boolean z = msg.obj;
                    if(z){
                        MyToast.ts("提取成功");
                    }else{
                        MyToast.ts("提取失败");
                    }
                    break;
                case 2:
                    mPd.dismiss();
                    Object[] sz = (Object[]) msg.obj;
                    boolean z2 = sz[0];
                    String toFile = (String) sz[1];
                    if(z2){
                        MyShare.share(mContext,toFile);
                    }
                    break;
                default:
            }
        }
        
    };
    
    public AppManagerAdapter(List<AppDataBean> list){
        this.mAppList = list;
    }
    
    static class ViewHolder extends RecyclerView.ViewHolder{
        
        LinearLayout mParent;
        ImageView mIcon;
        TextView mTitle, mVersion, mSize;
        
        public ViewHolder(View view){
            super(view);
            mParent = view.findViewById(R.id.item_parent);
            mTitle = view.findViewById(R.id.app_title);
            mVersion = view.findViewById(R.id.app_version);
            mSize = view.findViewById(R.id.app_size);
            mIcon = view.findViewById(R.id.app_icon);
        }
    }
    
    
    @Override
    public AppManagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppManagerAdapter.ViewHolder holder, int position) {
        final AppDataBean data = mAppList.get(position);

        appName = data.getAppName();
        appIcon = data.getAppIcon();
        appSize = data.getAppSize();
        versionName = data.getVersionName();
        
        holder.mTitle.setText(appName);
        holder.mVersion.setText("v"+versionName);
        holder.mSize.setText(appSize);
        holder.mIcon.setImageDrawable(appIcon);
        
        holder.mParent.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mContext = view.getContext();
                    mActivity = (Activity) mContext;
                    handleDialog(data);
                }
        });
        
    }

    private void handleDialog(AppDataBean data){

        appName = data.getAppName();
        packageName = data.getPackageName();
        appIcon = data.getAppIcon();
        appSize = data.getAppSize();
        versionCode = data.getVersionCode();
        versionName = data.getVersionName();
        firstInstallDate = data.getFirstInstallDate();
        lastUpdateDate = data.getLastUpdateDate();
        targetSdkVersion = data.getTargetSdkVersion();
        minSdkVersion = data.getMinSdkVersion();
        dataDir = data.getDataDir();
        sourceDir = data.getSourceDir();
        uid = data.getUid();
        flags = data.getFlags();
        mainActivity = data.getMainActivity();
        if(mainActivity == null || mainActivity.equals("")){
            mainActivity = "无";
        }
        
        
        View dialogView = View.inflate(mContext, R.layout.dialog_app_info, null);
        
        mAppicon = dialogView.findViewById(R.id.appicon);
        mAppshare = dialogView.findViewById(R.id.appshare);
        mAppname = dialogView.findViewById(R.id.appname);
        mAppbm = dialogView.findViewById(R.id.appbm);
        mAppqdl = dialogView.findViewById(R.id.appqdl);
        mAppbb = dialogView.findViewById(R.id.appbb);
        mAppbbh = dialogView.findViewById(R.id.appbbh);
        mAppuid = dialogView.findViewById(R.id.appuid);
        mAppflags = dialogView.findViewById(R.id.appflags);
        mAppfid = dialogView.findViewById(R.id.appfid);
        mApplud = dialogView.findViewById(R.id.applud);
        mAppsjml = dialogView.findViewById(R.id.appdatadir);
        mAppywjml = dialogView.findViewById(R.id.appsourcedir);
        mAppqc = dialogView.findViewById(R.id.appqc);

        MyShape.set(dialogView,15,15,0,0,"#ffffff");
        MyShape.set(mAppqc.getBackground(),mContext.getString(R.color.colorPrimary));
        mAppicon.setImageDrawable(appIcon);
        mAppname.setText(appName);
        mAppbm.setText(packageName);
        mAppqdl.setText(mainActivity);
        mAppbb.setText(versionName);
        mAppbbh.setText(versionCode+"");
        mAppuid.setText(uid+"");
        mAppflags.setText(flags+"");
        mAppfid.setText(firstInstallDate);
        mApplud.setText(lastUpdateDate);
        mAppsjml.setText(dataDir);
        mAppywjml.setText(sourceDir);
        
        mBsd = new BottomSheetDialog(mContext, R.style.BottomSheetStyle);
        mBsd.setContentView(dialogView);
        mBsd.show();
        //分享
        mAppshare.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    mPd = new MyProgressDialog(mContext);
                    mPd.setMessage("正在创建...");
                    mPd.show();
                    new Thread(new Runnable(){

                            @Override
                            public void run() {
                                String toFile = mContext.getExternalCacheDir()+"/apks/"+appName+"_"+versionName+".apk";
                                boolean z = FileHelper.copyFile(sourceDir, toFile);
                                Message msg = mHandler.obtainMessage();
                                msg.what = 2;
                                msg.obj = new Object[]{z,toFile};
                                mHandler.sendMessage(msg);
                            }
                    }).start();
                }
        });
        //提取App
        mAppqc.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    
                    mPd = new MyProgressDialog(mContext);
                    mPd.setMessage("正在提取...");
                    mPd.show();
                    new Thread(new Runnable(){

                            @Override
                            public void run() {
                                String toFile = Environment.getExternalStorageDirectory()+"/iBeauty/apks/"+appName+"_"+versionName+".apk";
                                boolean z = FileHelper.copyFile(sourceDir, toFile);
                                Message msg = mHandler.obtainMessage();
                                msg.what = 1;
                                msg.obj = z;
                                mHandler.sendMessage(msg);
                               
                            }
                    }).start();
                }
        });
        
        mAppbm.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    MyClipBoard.write(packageName);
                    MyToast.ts("已写入剪切板");
                }
        });
        mAppqdl.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    MyClipBoard.write(mainActivity);
                    MyToast.ts("已写入剪切板");
                }
            });
        mAppuid.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    MyClipBoard.write(uid+"");
                    MyToast.ts("已写入剪切板");
                }
            });
        mAppflags.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View p1) {
                    MyClipBoard.write(flags+"");
                    MyToast.ts("已写入剪切板");
                }
            });
    }
    
    @Override
    public int getItemCount() {
        return mAppList.size();
    }
    
    public void removeAll(){
        mAppList.clear();
        notifyDataSetChanged();
    }
    
    
}
